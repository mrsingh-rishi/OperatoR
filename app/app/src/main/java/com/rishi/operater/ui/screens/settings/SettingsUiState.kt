package com.rishi.operater.ui.screens.settings

data class SettingsUiState(
    val title: String = "Settings",
    val audioCaptureEnabled: Boolean = false,
    val isScreenCaptureSupported: Boolean = false,
    val isScreenCaptureReady: Boolean = false,
)
