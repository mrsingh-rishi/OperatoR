package com.rishi.operater.ui.screens.session

data class SessionUiState(
    val title: String = "Session",
    val status: String = "Idle",
    val details: String = "No active operator session.",
    val accessibilityConnected: Boolean = false,
    val foregroundPackageName: String? = null,
    val rootNodeAvailable: Boolean = false,
    val totalNodes: Int = 0,
    val clickableNodes: Int = 0,
    val editableNodes: Int = 0,
    val nodesWithText: Int = 0,
)
