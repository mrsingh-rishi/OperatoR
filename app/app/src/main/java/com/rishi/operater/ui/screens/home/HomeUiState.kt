package com.rishi.operater.ui.screens.home

import com.rishi.operater.data.model.AgentLifecycleStatus
import com.rishi.operater.data.model.PermissionStatus
import com.rishi.operater.data.model.ScreenCaptureCapabilityStatus

data class HomeUiState(
    val title: String = "OperateR",
    val subtitle: String = "AI-assisted mobile task operations start here.",
    val accessibilityStatus: PermissionStatus = PermissionStatus.DENIED,
    val microphonePermissionStatus: PermissionStatus = PermissionStatus.NOT_REQUESTED,
    val screenCaptureCapabilityStatus: ScreenCaptureCapabilityStatus = ScreenCaptureCapabilityStatus.UNAVAILABLE,
    val agentLifecycleStatus: AgentLifecycleStatus = AgentLifecycleStatus.IDLE,
)
