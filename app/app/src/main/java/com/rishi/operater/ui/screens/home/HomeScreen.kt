package com.rishi.operater.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rishi.operater.data.model.AgentLifecycleStatus
import com.rishi.operater.data.model.PermissionStatus
import com.rishi.operater.data.model.ScreenCaptureCapabilityStatus

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = uiState.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = uiState.subtitle, style = MaterialTheme.typography.bodyLarge)

        Text(
            text = "Accessibility service: ${uiState.accessibilityStatus.toLabel()}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "Microphone permission: ${uiState.microphonePermissionStatus.toLabel()}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "Screen capture capability: ${uiState.screenCaptureCapabilityStatus.toLabel()}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "Agent session: ${uiState.agentLifecycleStatus.toLabel()}",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private fun PermissionStatus.toLabel(): String = when (this) {
    PermissionStatus.NOT_REQUESTED -> "Not requested"
    PermissionStatus.GRANTED -> "Enabled"
    PermissionStatus.DENIED -> "Disabled"
}

private fun ScreenCaptureCapabilityStatus.toLabel(): String = when (this) {
    ScreenCaptureCapabilityStatus.READY -> "Ready"
    ScreenCaptureCapabilityStatus.ACTIVE -> "Active"
    ScreenCaptureCapabilityStatus.UNAVAILABLE -> "Unavailable"
}

private fun AgentLifecycleStatus.toLabel(): String = when (this) {
    AgentLifecycleStatus.IDLE -> "Idle"
    AgentLifecycleStatus.RUNNING -> "Running"
    AgentLifecycleStatus.STOPPED -> "Stopped"
}
