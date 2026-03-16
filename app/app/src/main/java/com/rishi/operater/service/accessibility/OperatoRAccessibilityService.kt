package com.rishi.operater.service.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityEvent
import com.rishi.operater.service.accessibility.model.NodeInfoSummary

/**
 * Accessibility bridge for OperateR.
 *
 * Current behavior is read-only and observational:
 * - observe window/content changes
 * - aggregate semantic summary of the active window tree
 * - publish sanitized state for UI/runtime consumption
 */
class OperatoRAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        AccessibilityObservationStore.onServiceConnectionChanged(connected = true)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val root = rootInActiveWindow
        val packageName = event?.packageName?.toString() ?: root?.packageName?.toString()
        if (root == null) {
            AccessibilityObservationStore.updateSnapshot(
                foregroundPackageName = packageName,
                rootNodeAvailable = false,
                nodeSummary = NodeInfoSummary(),
            )
            return
        }

        val nodeSummary = summarizeTree(root)
        root.recycle()

        AccessibilityObservationStore.updateSnapshot(
            foregroundPackageName = packageName,
            rootNodeAvailable = true,
            nodeSummary = nodeSummary,
        )
    }

    override fun onInterrupt() {
        AccessibilityObservationStore.onServiceConnectionChanged(connected = false)
    }

    override fun onDestroy() {
        AccessibilityObservationStore.onServiceConnectionChanged(connected = false)
        super.onDestroy()
    }

    private fun summarizeTree(root: AccessibilityNodeInfo): NodeInfoSummary {
        var total = 0
        var clickable = 0
        var editable = 0
        var withText = 0
        val stack = ArrayDeque<AccessibilityNodeInfo>()
        stack.add(root)

        while (stack.isNotEmpty()) {
            val node = stack.removeLast()
            total += 1
            if (node.isClickable) clickable += 1
            if (node.isEditable) editable += 1
            if (!node.text.isNullOrBlank() || !node.contentDescription.isNullOrBlank()) {
                withText += 1
            }

            for (index in 0 until node.childCount) {
                val child = node.getChild(index) ?: continue
                stack.add(child)
            }

            if (node !== root) {
                node.recycle()
            }
        }

        return NodeInfoSummary(
            totalNodes = total,
            clickableNodes = clickable,
            editableNodes = editable,
            nodesWithText = withText,
        )
    }
}
