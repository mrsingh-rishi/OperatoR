package com.rishi.operater.ui.session

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    fun stopSession() {
        _uiState.value = _uiState.value.copy(
            sessionState = com.rishi.operater.agent.model.AgentSessionState.Stopped,
            currentAction = "Session stopped by user"
        )
    }
}
