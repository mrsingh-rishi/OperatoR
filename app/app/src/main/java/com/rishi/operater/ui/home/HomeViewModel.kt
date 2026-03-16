package com.rishi.operater.ui.home

import androidx.lifecycle.ViewModel
import com.rishi.operater.core.OperatoRRuntime
import com.rishi.operater.data.model.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeUiState(
            accessibilityStatus = PermissionStatus.NOT_REQUESTED,
            screenCaptureStatus = if (OperatoRRuntime.screenCaptureController.isCapturing) {
                PermissionStatus.GRANTED
            } else {
                PermissionStatus.NOT_REQUESTED
            },
            audioPermissionStatus = if (OperatoRRuntime.audioController.isRecording) {
                PermissionStatus.GRANTED
            } else {
                PermissionStatus.NOT_REQUESTED
            },
            sessionState = OperatoRRuntime.sessionManager.state.value
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
