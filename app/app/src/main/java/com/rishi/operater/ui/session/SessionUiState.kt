package com.rishi.operater.ui.session

import com.rishi.operater.agent.model.AgentSessionState

data class SessionUiState(
    val task: String = "No active task",
    val currentPackage: String = "Unknown",
    val currentAction: String = "Awaiting planner",
    val sessionState: AgentSessionState = AgentSessionState.Idle,
    val logs: List<String> = listOf(
        "Session initialized",
        "Awaiting user task",
        "Gemini Live + deterministic executor integration pending"
    )
)
