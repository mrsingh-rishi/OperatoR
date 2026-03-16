package com.rishi.operater.service.accessibility.model

/**
 * Compact descriptor for an observed accessibility node.
 */
data class NodeDescriptor(
    val label: String? = null,
    val className: String? = null,
    val viewIdResourceName: String? = null,
)

/**
 * Compact summary for interactive node categories.
 */
data class NodeCollectionSummary(
    val totalCount: Int = 0,
    val samples: List<NodeDescriptor> = emptyList(),
)

/**
 * Structured, read-only model of the currently visible screen.
 */
data class ScreenModel(
    val packageName: String? = null,
    val visibleTextLabels: List<String> = emptyList(),
    val clickableNodes: NodeCollectionSummary = NodeCollectionSummary(),
    val editableNodes: NodeCollectionSummary = NodeCollectionSummary(),
    val focusedNode: NodeDescriptor? = null,
)
