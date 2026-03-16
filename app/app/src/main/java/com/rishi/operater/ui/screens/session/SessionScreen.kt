package com.rishi.operater.ui.screens.session

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
fun SessionScreen(
    uiState: SessionUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = uiState.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Status: ${uiState.status}", style = MaterialTheme.typography.titleMedium)
        Text(text = uiState.details, style = MaterialTheme.typography.bodyLarge)
    }
}
