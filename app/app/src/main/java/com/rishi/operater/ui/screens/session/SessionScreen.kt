package com.rishi.operater.ui.screens.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SessionScreen(
    uiState: SessionUiState,
    onRequestScreenCapturePermission: () -> Unit,
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
        Text(
            text = "Screen capture supported: ${if (uiState.isScreenCaptureSupported) "Yes" else "No"}",
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = "Screen capture ready: ${if (uiState.isScreenCaptureReady) "Yes" else "No"}",
            style = MaterialTheme.typography.bodyLarge,
        )
        if (uiState.isScreenCaptureSupported && !uiState.isScreenCaptureReady) {
            Button(onClick = onRequestScreenCapturePermission) {
                Text(text = "Grant screen capture permission")
            }
        }

        Text(
            text = "Accessibility connected: ${if (uiState.accessibilityConnected) "Yes" else "No"}",
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = "Foreground app: ${uiState.foregroundPackageName ?: "Unknown"}",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "Root node available: ${if (uiState.rootNodeAvailable) "Yes" else "No"}",
            style = MaterialTheme.typography.bodyLarge,
        )

        Text(
            text = "Screen model package: ${uiState.screenPackageName ?: "Unknown"}",
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = "Clickable: ${uiState.clickableSummary}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "Editable: ${uiState.editableSummary}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "Focused node: ${uiState.focusedNodeSummary}",
            style = MaterialTheme.typography.bodyMedium,
        )

        val labelsText = if (uiState.visibleTextLabels.isEmpty()) {
            "None"
        } else {
            uiState.visibleTextLabels.take(8).joinToString(separator = " • ")
        }

        Text(
            text = "Visible text labels: $labelsText",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
