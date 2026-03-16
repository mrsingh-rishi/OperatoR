package com.rishi.operater.service.accessibility.model

/**
 * Lightweight aggregate of visible semantics from the active accessibility tree.
 *
 * This intentionally avoids retaining raw [android.view.accessibility.AccessibilityNodeInfo]
 * instances so the data can be safely consumed by other layers.
 */
data class NodeInfoSummary(
    val totalNodes: Int = 0,
    val clickableNodes: Int = 0,
    val editableNodes: Int = 0,
    val nodesWithText: Int = 0,
)

