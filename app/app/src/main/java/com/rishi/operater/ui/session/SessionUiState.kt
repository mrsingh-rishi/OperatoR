package com.rishi.operater.ui.session

import com.rishi.operater.agent.model.AgentSessionState
import com.rishi.operater.data.model.PermissionStatus

data class SessionUiState(
    val task: String = "No active task",
    val currentPackage: String = "Unknown",
    val rootNodeAvailable: Boolean = false,
    val totalNodes: Int = 0,
    val nodesWithText: Int = 0,
    val clickableNodes: Int = 0,
    val editableNodes: Int = 0,
    val accessibilityStatus: PermissionStatus = PermissionStatus.DENIED,
    val currentAction: String = "Awaiting planner",
    val sessionState: AgentSessionState = AgentSessionState.Idle,
    val logs: List<String> = listOf(
        "Session initialized",
        "Accessibility observer is read-only",
        "Awaiting user task",
    )
)
