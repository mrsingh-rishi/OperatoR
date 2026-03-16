package com.rishi.operater.ui.screens.session

import androidx.lifecycle.ViewModel
import com.rishi.operater.agent.model.AgentSessionState
import com.rishi.operater.core.OperatoRRuntime

class SessionViewModel : ViewModel() {
    val uiState = SessionUiState(
        status = when (OperatoRRuntime.sessionManager.state.value) {
            AgentSessionState.Idle,
            AgentSessionState.Stopped,
            AgentSessionState.Error,
            -> "Idle"

            AgentSessionState.Preparing,
            AgentSessionState.Running,
            AgentSessionState.AwaitingConfirmation,
            -> "Active"
        },
    )
}
