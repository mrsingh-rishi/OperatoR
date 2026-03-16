package com.rishi.operater.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setConfirmationMode(value: Boolean) = _uiState.update { it.copy(confirmationMode = value) }
    fun setDebugMode(value: Boolean) = _uiState.update { it.copy(debugMode = value) }
    fun setMultimodalCaptureEnabled(value: Boolean) =
        _uiState.update { it.copy(multimodalCaptureEnabled = value) }

    fun setAudioReplyEnabled(value: Boolean) = _uiState.update { it.copy(audioReplyEnabled = value) }
    fun setLocalStorageEnabled(value: Boolean) = _uiState.update { it.copy(localStorageEnabled = value) }
}
