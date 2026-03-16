package com.rishi.operater.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rishi.operater.core.OperatoRRuntime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = OperatoRRuntime.screenCaptureController
        .permissionState
        .map { permissionState ->
            SettingsUiState(
                isScreenCaptureSupported = permissionState.isSupported,
                isScreenCaptureReady = permissionState.isPermissionGranted,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = SettingsUiState(),
        )
}
