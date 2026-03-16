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
            text = if (uiState.isAccessibilityEnabled) {
                "Accessibility bridge is enabled."
            } else {
                "Accessibility bridge is not enabled yet."
            },
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
