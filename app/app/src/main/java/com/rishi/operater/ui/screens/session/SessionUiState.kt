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
    val ocrLines: List<String> = emptyList(),
    val ocrStatus: String = "Not run yet",
    val combinedScreenSummary: String = "None",
    val clickableSummary: String = "None",
    val editableSummary: String = "None",
    val focusedNodeSummary: String = "None",
    val isScreenCaptureSupported: Boolean = false,
    val isScreenCaptureReady: Boolean = false,
    val isCapturePipelineReady: Boolean = false,
    val isFrameCaptureInProgress: Boolean = false,
    val captureCount: Int = 0,
    val lastFrameSummary: String = "No frame captured yet",
)
