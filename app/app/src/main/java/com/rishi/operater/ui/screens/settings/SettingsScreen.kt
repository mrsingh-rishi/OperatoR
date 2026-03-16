package com.rishi.operater.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = uiState.title, style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Screen capture: ${if (uiState.screenCaptureEnabled) \"Enabled\" else \"Disabled\"}",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "Audio capture: ${if (uiState.audioCaptureEnabled) \"Enabled\" else \"Disabled\"}",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
