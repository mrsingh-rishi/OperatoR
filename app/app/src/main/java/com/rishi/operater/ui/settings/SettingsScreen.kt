package com.rishi.operater.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        SettingRow(
            title = "Confirmation mode",
            checked = uiState.confirmationMode,
            onCheckedChange = viewModel::setConfirmationMode
        )
        SettingRow(
            title = "Debug mode",
            checked = uiState.debugMode,
            onCheckedChange = viewModel::setDebugMode
        )
        SettingRow(
            title = "Multimodal capture enabled",
            checked = uiState.multimodalCaptureEnabled,
            onCheckedChange = viewModel::setMultimodalCaptureEnabled
        )
        SettingRow(
            title = "Audio reply enabled",
            checked = uiState.audioReplyEnabled,
            onCheckedChange = viewModel::setAudioReplyEnabled
        )
        SettingRow(
            title = "Local storage enabled",
            checked = uiState.localStorageEnabled,
            onCheckedChange = viewModel::setLocalStorageEnabled
        )

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
