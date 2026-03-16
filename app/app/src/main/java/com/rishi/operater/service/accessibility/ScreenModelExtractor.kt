package com.rishi.operater.service.accessibility

import android.view.accessibility.AccessibilityNodeInfo
import com.rishi.operater.service.accessibility.model.NodeCollectionSummary
import com.rishi.operater.service.accessibility.model.NodeDescriptor
import com.rishi.operater.service.accessibility.model.ScreenModel

/**
 * Builds a compact [ScreenModel] from the active accessibility tree.
 */
class ScreenModelExtractor(
    private val maxTextLabels: Int = 24,
    private val maxNodeSamples: Int = 8,
    private val maxLabelLength: Int = 80,
) {
    fun extract(
        rootNode: AccessibilityNodeInfo?,
        fallbackPackageName: String?,
    ): ScreenModel {
        if (rootNode == null) {
            return ScreenModel(packageName = fallbackPackageName)
        }

        var packageName: String? = fallbackPackageName
        val visibleTextLabels = LinkedHashSet<String>()
        var clickableCount = 0
        var editableCount = 0
        val clickableSamples = mutableListOf<NodeDescriptor>()
        val editableSamples = mutableListOf<NodeDescriptor>()
        var focusedNode: NodeDescriptor? = null

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(rootNode)

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            try {
                if (packageName.isNullOrBlank()) {
                    packageName = node.packageName?.toString()
                }

                val label = deriveLabel(node)
                if (node.isVisibleToUser && !label.isNullOrBlank() && visibleTextLabels.size < maxTextLabels) {
                    visibleTextLabels.add(label)
                }

                if (node.isClickable) {
                    clickableCount += 1
                    if (clickableSamples.size < maxNodeSamples) {
                        clickableSamples.add(buildDescriptor(node, label))
                    }
                }

                if (node.isEditable) {
                    editableCount += 1
                    if (editableSamples.size < maxNodeSamples) {
                        editableSamples.add(buildDescriptor(node, label))
                    }
                }

                if (focusedNode == null && node.isFocused) {
                    focusedNode = buildDescriptor(node, label)
                }

                for (index in 0 until node.childCount) {
                    node.getChild(index)?.let(queue::add)
                }
            } finally {
                node.recycle()
            }
        }

        return ScreenModel(
            packageName = packageName,
            visibleTextLabels = visibleTextLabels.toList(),
            clickableNodes = NodeCollectionSummary(
                totalCount = clickableCount,
                samples = clickableSamples,
            ),
            editableNodes = NodeCollectionSummary(
                totalCount = editableCount,
                samples = editableSamples,
            ),
            focusedNode = focusedNode,
        )
    }

    private fun deriveLabel(node: AccessibilityNodeInfo): String? {
        val text = node.text?.toString()?.trim().orEmpty()
        val contentDescription = node.contentDescription?.toString()?.trim().orEmpty()
        val raw = when {
            text.isNotEmpty() -> text
            contentDescription.isNotEmpty() -> contentDescription
            else -> ""
        }

        if (raw.isBlank()) {
            return null
        }

        return raw
            .replace("\n", " ")
            .trim()
            .take(maxLabelLength)
    }

    private fun buildDescriptor(node: AccessibilityNodeInfo, label: String?): NodeDescriptor {
        return NodeDescriptor(
            label = label,
            className = node.className?.toString(),
            viewIdResourceName = node.viewIdResourceName,
        )
    }
}
