package com.rishi.operater.service.accessibility.model

/**
 * Read-only semantic snapshot captured from the currently active window.
 */
data class ScreenSemanticSnapshot(
    val appState: CurrentAppState = CurrentAppState(),
    val screenModel: ScreenModel = ScreenModel(),
    val capturedAtMillis: Long = System.currentTimeMillis(),
)
