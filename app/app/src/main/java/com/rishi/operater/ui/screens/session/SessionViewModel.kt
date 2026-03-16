package com.rishi.operater.ui.screens.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rishi.operater.agent.model.AgentSessionState
import com.rishi.operater.core.OperatoRRuntime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SessionViewModel : ViewModel() {
    val uiState: StateFlow<SessionUiState> = OperatoRRuntime.accessibilityReader.snapshot.map { snapshot ->
        SessionUiState(
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
            accessibilityConnected = snapshot.appState.accessibilityServiceConnected,
            foregroundPackageName = snapshot.appState.foregroundPackageName,
            rootNodeAvailable = snapshot.appState.rootNodeAvailable,
            totalNodes = snapshot.nodeSummary.totalNodes,
            clickableNodes = snapshot.nodeSummary.clickableNodes,
            editableNodes = snapshot.nodeSummary.editableNodes,
            nodesWithText = snapshot.nodeSummary.nodesWithText,
        )
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = SessionUiState(),
        )
}
