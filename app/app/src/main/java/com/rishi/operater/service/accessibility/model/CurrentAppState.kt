package com.rishi.operater.service.accessibility.model

/**
 * Current high-level app/window observation from the accessibility layer.
 */
data class CurrentAppState(
    val accessibilityServiceConnected: Boolean = false,
    val foregroundPackageName: String? = null,
    val rootNodeAvailable: Boolean = false,
)

