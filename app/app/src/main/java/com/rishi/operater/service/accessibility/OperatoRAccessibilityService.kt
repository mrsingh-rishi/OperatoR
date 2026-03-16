package com.rishi.operater.service.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityEvent
import com.rishi.operater.service.accessibility.model.NodeInfoSummary

/**
 * Accessibility bridge in observation-only mode.
 *
 * This service only reads semantic state from the active window. It intentionally does not
 * perform actions, gestures, or text entry.
 */
class OperatoRAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow
        val foregroundPackageName = event?.packageName?.toString() ?: rootNode?.packageName?.toString()
        val rootNodeAvailable = rootNode != null
        val summary = summarizeNodes(rootNode)

        AccessibilityObservationStore.updateSnapshot(
            foregroundPackageName = foregroundPackageName,
            rootNodeAvailable = rootNodeAvailable,
            lastEventType = event?.eventType,
            nodeSummary = summary,
        )

        rootNode?.recycle()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        AccessibilityObservationStore.onServiceConnectionChanged(connected = true)
    }

    override fun onInterrupt() {
        AccessibilityObservationStore.onServiceConnectionChanged(connected = false)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        AccessibilityObservationStore.onServiceConnectionChanged(connected = false)
        return super.onUnbind(intent)
    }

    private fun summarizeNodes(rootNode: AccessibilityNodeInfo?): NodeInfoSummary {
        if (rootNode == null) {
            return NodeInfoSummary()
        }

        var totalNodes = 0
        var clickableNodes = 0
        var editableNodes = 0
        var nodesWithText = 0

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(rootNode)

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            totalNodes += 1

            if (node.isClickable) {
                clickableNodes += 1
            }
            if (node.isEditable) {
                editableNodes += 1
            }
            if (!node.text.isNullOrBlank() || !node.contentDescription.isNullOrBlank()) {
                nodesWithText += 1
            }

            for (index in 0 until node.childCount) {
                node.getChild(index)?.let { queue.add(it) }
            }
        }

        return NodeInfoSummary(
            totalNodes = totalNodes,
            clickableNodes = clickableNodes,
            editableNodes = editableNodes,
            nodesWithText = nodesWithText,
        )
    }
}
