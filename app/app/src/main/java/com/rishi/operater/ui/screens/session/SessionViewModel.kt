package com.rishi.operater.ui.screens.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rishi.operater.agent.model.AgentSessionState
import com.rishi.operater.core.OperatoRRuntime
import com.rishi.operater.service.accessibility.model.NodeCollectionSummary
import com.rishi.operater.service.accessibility.model.NodeDescriptor
import com.rishi.operater.service.projection.FrameCaptureState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel : ViewModel() {
    val uiState: StateFlow<SessionUiState> = combine(
        OperatoRRuntime.accessibilityReader.snapshot,
        OperatoRRuntime.screenModelRepository.latestScreenModel,
        OperatoRRuntime.screenCaptureController.permissionState,
        OperatoRRuntime.screenCaptureController.frameCaptureState,
    ) { snapshot, screenModel, screenCaptureState, frameCaptureState ->
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
            screenPackageName = screenModel.packageName,
            visibleTextLabels = screenModel.visibleTextLabels,
            clickableSummary = summarizeCollection(screenModel.clickableNodes),
            editableSummary = summarizeCollection(screenModel.editableNodes),
            focusedNodeSummary = summarizeNode(screenModel.focusedNode),
            isScreenCaptureSupported = screenCaptureState.isSupported,
            isScreenCaptureReady = screenCaptureState.isPermissionGranted,
            isCapturePipelineReady = frameCaptureState.isPipelineReady,
            isFrameCaptureInProgress = frameCaptureState.isCaptureInProgress,
            captureCount = frameCaptureState.captureCount,
            lastFrameSummary = summarizeFrame(frameCaptureState),
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = SessionUiState(),
        )

    fun captureFrame() {
        viewModelScope.launch {
            OperatoRRuntime.screenCaptureController.captureFrame()
        }
    }

    private fun summarizeCollection(summary: NodeCollectionSummary): String {
        if (summary.totalCount == 0) {
            return "None"
        }

        val samples = summary.samples
            .take(3)
            .joinToString(separator = " · ") { summarizeNode(it) }

        return if (samples.isBlank()) {
            "${summary.totalCount} nodes"
        } else {
            "${summary.totalCount} nodes ($samples)"
        }
    }

    private fun summarizeNode(node: NodeDescriptor?): String {
        if (node == null) {
            return "None"
        }

        return node.label
            ?: node.viewIdResourceName
            ?: node.className
            ?: "Unnamed node"
    }

    private fun summarizeFrame(state: FrameCaptureState): String {
        val metadata = state.lastFrameMetadata
        if (metadata == null) {
            return state.lastFailureReason ?: "No frame captured yet"
        }

        return "${metadata.width}x${metadata.height}, rowStride=${metadata.rowStride}, " +
            "pixelStride=${metadata.pixelStride}, ts=${metadata.timestampNanos}"
    }
}
