package com.rishi.operater.ui.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SessionScreen(
    onBack: () -> Unit,
    viewModel: SessionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Session", style = MaterialTheme.typography.headlineMedium)
        Text("Task: ${uiState.task}")
        Text("Current app: ${uiState.currentPackage}")
        Text("Current action: ${uiState.currentAction}")
        Text("State: ${uiState.sessionState.name}")
        Text(
            text = "Gemini Live execution is intentionally deferred; this layer keeps session contracts ready.",
            style = MaterialTheme.typography.bodySmall
        )

        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.logs) { log ->
                    Text(text = "• $log")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = viewModel::stopSession, modifier = Modifier.weight(1f)) {
                Text("Stop")
            }
            Button(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Back")
            }
        }
    }
}
