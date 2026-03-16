package com.rishi.operater.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rishi.operater.data.model.AgentLifecycleStatus
import com.rishi.operater.data.model.PermissionStatus
import com.rishi.operater.data.model.ScreenCaptureCapabilityStatus

@Composable
fun HomeScreen(
    onStartSession: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.refreshStatus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "OperatoR",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Foundation for safe, assisted task execution",
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = onStartSession, modifier = Modifier.weight(1f)) {
                Text("Start Session")
            }
            Button(onClick = onOpenSettings, modifier = Modifier.weight(1f)) {
                Text("Settings")
            }
        }

        StatusCard("Accessibility service", uiState.accessibilityStatus.toDisplayText())
        StatusCard("Screen capture", uiState.screenCaptureStatus.toDisplayText())
        StatusCard("Audio permission", uiState.audioPermissionStatus.toDisplayText())
        StatusCard("Session state", uiState.sessionStatus.toDisplayText())
    }
}

@Composable
private fun StatusCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun PermissionStatus.toDisplayText(): String = when (this) {
    PermissionStatus.NOT_REQUESTED -> "Not requested"
    PermissionStatus.GRANTED -> "Granted"
    PermissionStatus.DENIED -> "Denied"
}

private fun ScreenCaptureCapabilityStatus.toDisplayText(): String = when (this) {
    ScreenCaptureCapabilityStatus.READY -> "Ready (not capturing)"
    ScreenCaptureCapabilityStatus.ACTIVE -> "Active"
    ScreenCaptureCapabilityStatus.UNAVAILABLE -> "Unavailable"
}

private fun AgentLifecycleStatus.toDisplayText(): String = when (this) {
    AgentLifecycleStatus.IDLE -> "Idle"
    AgentLifecycleStatus.RUNNING -> "Running"
    AgentLifecycleStatus.STOPPED -> "Stopped"
}
