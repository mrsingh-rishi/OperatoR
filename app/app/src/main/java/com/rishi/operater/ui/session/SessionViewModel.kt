package com.rishi.operater.ui.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rishi.operater.agent.model.AgentSessionState
import com.rishi.operater.core.OperatoRRuntime
import com.rishi.operater.data.repository.SystemCapabilityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) : AndroidViewModel(application) {
    private val capabilityRepository = SystemCapabilityRepository(
        appContext = application.applicationContext,
        screenCaptureController = OperatoRRuntime.screenCaptureController,
    )

    private val _uiState = MutableStateFlow(
        SessionUiState(
            task = OperatoRRuntime.sessionRepository.currentTask.value ?: "No active task",
            sessionState = OperatoRRuntime.sessionManager.state.value,
            accessibilityStatus = capabilityRepository.getAccessibilityStatus(),
        )
    )
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        observeAccessibilitySnapshot()
        observeSessionState()
    }

    private fun observeAccessibilitySnapshot() {
        viewModelScope.launch {
            OperatoRRuntime.accessibilityReader.snapshot.collectLatest { snapshot ->
                _uiState.value = _uiState.value.copy(
                    currentPackage = snapshot.appState.foregroundPackageName ?: "Unknown",
                    rootNodeAvailable = snapshot.appState.rootNodeAvailable,
                    totalNodes = snapshot.nodeSummary.totalNodes,
                    nodesWithText = snapshot.nodeSummary.nodesWithText,
                    clickableNodes = snapshot.nodeSummary.clickableNodes,
                    editableNodes = snapshot.nodeSummary.editableNodes,
                    accessibilityStatus = capabilityRepository.getAccessibilityStatus(),
                )
            }
        }
    }

    private fun observeSessionState() {
        viewModelScope.launch {
            OperatoRRuntime.sessionManager.state.collectLatest { sessionState ->
                _uiState.value = _uiState.value.copy(sessionState = sessionState)
            }
        }
    }

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
