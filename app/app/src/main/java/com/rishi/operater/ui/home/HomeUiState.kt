package com.rishi.operater.ui.home

import com.rishi.operater.data.model.AgentLifecycleStatus
import com.rishi.operater.data.model.PermissionStatus
import com.rishi.operater.data.model.ScreenCaptureCapabilityStatus

data class HomeUiState(
    val accessibilityStatus: PermissionStatus = PermissionStatus.NOT_REQUESTED,
    val screenCaptureStatus: ScreenCaptureCapabilityStatus = ScreenCaptureCapabilityStatus.UNAVAILABLE,
    val audioPermissionStatus: PermissionStatus = PermissionStatus.NOT_REQUESTED,
    val sessionStatus: AgentLifecycleStatus = AgentLifecycleStatus.IDLE
)
