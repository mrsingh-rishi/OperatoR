package com.rishi.operater.ui.settings

data class SettingsUiState(
    val confirmationMode: Boolean = true,
    val debugMode: Boolean = false,
    val multimodalCaptureEnabled: Boolean = false,
    val audioReplyEnabled: Boolean = false,
    val localStorageEnabled: Boolean = true
)
