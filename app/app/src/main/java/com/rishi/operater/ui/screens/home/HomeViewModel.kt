package com.rishi.operater.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rishi.operater.core.OperatoRRuntime
import com.rishi.operater.data.repository.SystemCapabilityRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val capabilityRepository = SystemCapabilityRepository(
        appContext = application.applicationContext,
        screenCaptureController = OperatoRRuntime.screenCaptureController,
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeCapabilities()
    }

    private fun observeCapabilities() {
        val capabilityRefreshTicker = flow {
            emit(Unit)
            while (true) {
                delay(1_000)
                emit(Unit)
            }
        }

        viewModelScope.launch {
            combine(
                OperatoRRuntime.sessionManager.state,
                capabilityRefreshTicker,
            ) { sessionState, _ ->
                HomeUiState(
                    accessibilityStatus = capabilityRepository.getAccessibilityStatus(),
                    microphonePermissionStatus = capabilityRepository.getMicrophonePermissionStatus(),
                    screenCaptureCapabilityStatus = capabilityRepository.getScreenCaptureCapabilityStatus(),
                    agentLifecycleStatus = capabilityRepository.getAgentLifecycleStatus(sessionState),
                )
            }.collectLatest { state ->
                _uiState.value = state
            }
        }
    }
}
