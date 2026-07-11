package com.example.nameshootingtimer.ui.screens.execution

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nameshootingtimer.domain.model.ExecutionStatus

@Composable
fun ExecutionScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExecutionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.status) {
        if (state.status is ExecutionStatus.Idle) {
            onNavigateBack()
        }
    }

    if (state.remainingTimeMs == -1L) {
        ManualStopScreen(
            onStop = { viewModel.onManualStop() }
        )
    } else {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (state.status is ExecutionStatus.Paused) "PAUSED" else "RUNNING",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (state.status is ExecutionStatus.Paused) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = state.activeTaskName ?: "Preparing...",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Text(
                    text = String.format("%.1f", state.remainingTimeMs / 1000f),
                    style = MaterialTheme.typography.displayLarge
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.stop() },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = "Stop", modifier = Modifier.size(48.dp))
                    }
                    
                    Spacer(modifier = Modifier.width(32.dp))
                    
                    FloatingActionButton(
                        onClick = {
                            if (state.status is ExecutionStatus.Running) viewModel.pause() else viewModel.resume()
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = if (state.status is ExecutionStatus.Running) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Toggle Pause"
                        )
                    }
                }
                
                if (state.totalCycles > 1 || state.totalCycles == -1) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Cycle: ${state.currentCycle} / ${if (state.totalCycles == -1) "∞" else state.totalCycles}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun ManualStopScreen(
    onStop: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onStop,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxSize()
                    .aspectRatio(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Text(
                    "STOP",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}
