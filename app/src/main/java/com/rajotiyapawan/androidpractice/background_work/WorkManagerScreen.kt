package com.rajotiyapawan.androidpractice.background_work

/**
 * Created by Pawan Rajotiya on 17-11-2025.
 */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WorkManagerScreen(
    modifier: Modifier,
    viewModel: WorkManagerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val workProgress by viewModel.workProgress.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "WorkManager Example",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Section
        workProgress?.let { progress ->
            ProgressSection(progress)
        }

        // Buttons Section
        ButtonsSection(
            onOneTimeWork = { viewModel.scheduleOneTimeWork() },
            onProgressWork = { viewModel.startProgressWork() },
            onDelayedWork = { viewModel.scheduleDelayedWork() },
            onCancelWork = { viewModel.cancelAllWork() }
        )

        // Status Section
        StatusSection(uiState)

        Spacer(modifier = Modifier.height(32.dp))

        // Info Section
        InfoSection()
    }
}

@Composable
private fun ProgressSection(progress: WorkProgress) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Work in Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            LinearProgressIndicator(
            progress = { progress.progress },
            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )

            Text(
                text = "${progress.percentage}% (${progress.current}/${progress.max})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun ButtonsSection(
    onOneTimeWork: () -> Unit,
    onProgressWork: () -> Unit,
    onDelayedWork: () -> Unit,
    onCancelWork: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilledTonalButton(
            onClick = onOneTimeWork,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Schedule One-Time Work",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        FilledTonalButton(
            onClick = onProgressWork,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Start Work with Progress",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        FilledTonalButton(
            onClick = onDelayedWork,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Schedule Delayed Work (1 min)",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        OutlinedButton(
            onClick = onCancelWork,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cancel All Work",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun StatusSection(uiState: WorkManagerUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (uiState) {
                is WorkManagerUiState.Idle -> {
                    Text(
                        text = "No work scheduled",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                is WorkManagerUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                is WorkManagerUiState.Success -> {
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                is WorkManagerUiState.Error -> {
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "How to test:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "• One-Time Work: Shows notification after 5 seconds\n" +
                        "• Progress Work: Shows progress updates every second\n" +
                        "• Delayed Work: Shows notification after 1 minute\n" +
                        "• Cancel: Stops all scheduled work",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}