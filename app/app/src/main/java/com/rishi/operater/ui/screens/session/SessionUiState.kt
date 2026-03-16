package com.rishi.operater.ui.screens.session

data class SessionUiState(
    val title: String = "Session",
    val status: String = "Idle",
    val details: String = "No active operator session.",
    val accessibilityConnected: Boolean = false,
    val foregroundPackageName: String? = null,
    val rootNodeAvailable: Boolean = false,
    val screenPackageName: String? = null,
    val visibleTextLabels: List<String> = emptyList(),
    val clickableSummary: String = "None",
    val editableSummary: String = "None",
    val focusedNodeSummary: String = "None",
    val isScreenCaptureSupported: Boolean = false,
    val isScreenCaptureReady: Boolean = false,
)
