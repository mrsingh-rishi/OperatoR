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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    onStartSession: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "OperateR",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "AI-assisted mobile task operator foundation",
            style = MaterialTheme.typography.bodyLarge
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onStartSession) {
                Text("Start Session")
            }
            Button(onClick = onOpenSettings) {
                Text("Settings")
            }
        }

        StatusCard(title = "Accessibility Service", value = uiState.accessibilityStatus.name)
        StatusCard(title = "Screen Capture", value = uiState.screenCaptureStatus.name)
        StatusCard(title = "Audio Permission", value = uiState.audioPermissionStatus.name)
        StatusCard(title = "Agent Session State", value = uiState.sessionState.name)
    }
}

@Composable
private fun StatusCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
