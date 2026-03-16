package com.rishi.operater.ui.screens.session

import androidx.lifecycle.ViewModel
import com.rishi.operater.session.AgentSessionManager

class SessionViewModel(
    private val sessionManager: AgentSessionManager = AgentSessionManager(),
) : ViewModel() {
    val uiState = SessionUiState(
        status = if (sessionManager.isSessionActive()) "Active" else "Idle",
    )
}
