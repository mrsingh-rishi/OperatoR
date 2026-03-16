package com.rishi.operater.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rishi.operater.core.OperatoRRuntime
import com.rishi.operater.data.repository.SystemCapabilityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val capabilityRepository = SystemCapabilityRepository(
        appContext = application.applicationContext,
        screenCaptureController = OperatoRRuntime.screenCaptureController
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        refreshStatus()
        observeSessionStatus()
    }

    fun refreshStatus() {
        val sessionState = OperatoRRuntime.sessionManager.state.value
        _uiState.value = HomeUiState(
            accessibilityStatus = capabilityRepository.getAccessibilityStatus(),
            screenCaptureStatus = capabilityRepository.getScreenCaptureCapabilityStatus(),
            audioPermissionStatus = capabilityRepository.getMicrophonePermissionStatus(),
            sessionStatus = capabilityRepository.getAgentLifecycleStatus(sessionState)
        )
    }

    private fun observeSessionStatus() {
        viewModelScope.launch {
            OperatoRRuntime.sessionManager.state.collectLatest { sessionState ->
                _uiState.value = _uiState.value.copy(
                    sessionStatus = capabilityRepository.getAgentLifecycleStatus(sessionState)
                )
            }
        }
    }
}
