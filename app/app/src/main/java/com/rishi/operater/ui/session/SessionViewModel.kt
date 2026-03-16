package com.rishi.operater.ui.session

import androidx.lifecycle.ViewModel
import com.rishi.operater.agent.model.AgentSessionState
import com.rishi.operater.core.OperatoRRuntime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        SessionUiState(
            task = OperatoRRuntime.sessionRepository.currentTask.value ?: "No active task",
            sessionState = OperatoRRuntime.sessionManager.state.value
        )
    )
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    fun stopSession() {
        OperatoRRuntime.sessionManager.stop()
        OperatoRRuntime.sessionRepository.setCurrentTask(null)
        _uiState.value = _uiState.value.copy(
            task = "No active task",
            sessionState = AgentSessionState.Stopped,
            currentAction = "Session stopped by user"
        )
    }
}
