package com.rishi.operater.ui.home

import com.rishi.operater.agent.model.AgentSessionState
import com.rishi.operater.data.model.PermissionStatus

data class HomeUiState(
    val accessibilityStatus: PermissionStatus = PermissionStatus.NOT_REQUESTED,
    val screenCaptureStatus: PermissionStatus = PermissionStatus.NOT_REQUESTED,
    val audioPermissionStatus: PermissionStatus = PermissionStatus.NOT_REQUESTED,
    val sessionState: AgentSessionState = AgentSessionState.Idle
)
