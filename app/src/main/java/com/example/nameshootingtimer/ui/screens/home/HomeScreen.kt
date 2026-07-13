package com.example.nameshootingtimer.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nameshootingtimer.domain.model.ExecutionStatus
import com.example.nameshootingtimer.domain.model.Routine
import com.example.nameshootingtimer.domain.model.Task
import com.example.nameshootingtimer.ui.screens.routine.RoutineViewModel
import com.example.nameshootingtimer.ui.screens.execution.ExecutionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToEditor: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: RoutineViewModel = hiltViewModel(),
    executionViewModel: ExecutionViewModel = hiltViewModel()
) {
    val routine by viewModel.uiState.collectAsState()
    val savedRoutines by viewModel.savedRoutines.collectAsState()
    val executionState by executionViewModel.state.collectAsState()
    
    var showSavedRoutinesSheet by remember { mutableStateOf(false) }

    val isRunning = executionState.status !is ExecutionStatus.Idle && executionState.status !is ExecutionStatus.Finished
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shooting Timer") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header / Current Routine Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = routine?.name ?: "Current Routine",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "${routine?.tasks?.size ?: 0} Tasks",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (!isRunning) {
                        Button(onClick = onNavigateToEditor) {
                            Text("Edit")
                        }
                    }
                }

                // Dynamic Task List
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    routine?.let { currentRoutine ->
                        ExecutionTaskList(
                            tasks = currentRoutine.tasks,
                            activeTaskIndex = executionState.activeTaskIndex,
                            remainingTimeMs = executionState.remainingTimeMs
                        )
                    }
                }

                // Execution Controls
                ExecutionControls(
                    status = executionState.status,
                    onStart = { routine?.let { executionViewModel.start(it) } },
                    onPause = { executionViewModel.pause() },
                    onResume = { executionViewModel.resume() },
                    onStop = { executionViewModel.stop() },
                    enabled = routine != null && routine!!.tasks.isNotEmpty()
                )

                // Secondary Navigation
                if (!isRunning) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showSavedRoutinesSheet = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Saved Routines")
                        }
                        OutlinedButton(
                            onClick = onNavigateToGallery,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Gallery")
                        }
                    }
                }
            }

            // Manual Stop Overlay
            if (executionState.remainingTimeMs == -1L) {
                ManualStopOverlay(onStop = { executionViewModel.onManualStop() })
            }
        }

        if (showSavedRoutinesSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSavedRoutinesSheet = false }
            ) {
                SavedRoutinesContent(
                    routines = savedRoutines,
                    onLoadRoutine = { 
                        viewModel.loadRoutine(it)
                        showSavedRoutinesSheet = false
                    },
                    onDeleteRoutine = { viewModel.deleteRoutine(it) },
                    onRenameRoutine = { r, newName -> viewModel.renameRoutine(r, newName) }
                )
            }
        }
    }
}

@Composable
fun ExecutionTaskList(
    tasks: List<Task>,
    activeTaskIndex: Int,
    remainingTimeMs: Long
) {
    val listState = rememberLazyListState()

    // Auto-scroll to active task
    LaunchedEffect(activeTaskIndex) {
        if (activeTaskIndex >= 0) {
            // Try to keep the active task centered
            val centerIndex = (activeTaskIndex - 2).coerceAtLeast(0)
            listState.animateScrollToItem(centerIndex)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 100.dp) // Large padding to allow centering
    ) {
        itemsIndexed(tasks) { index, task ->
            val isActive = index == activeTaskIndex
            val isCompleted = activeTaskIndex != -1 && index < activeTaskIndex
            
            val scale by animateFloatAsState(if (isActive) 1.08f else 1f)
            val alpha by animateFloatAsState(if (isCompleted) 0.4f else 1f)
            
            TaskExecutionCard(
                task = task,
                isActive = isActive,
                remainingTimeMs = if (isActive) remainingTimeMs else null,
                modifier = Modifier
                    .scale(scale)
                    .alpha(alpha)
            )
        }
    }
}

@Composable
fun TaskExecutionCard(
    task: Task,
    isActive: Boolean,
    remainingTimeMs: Long?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 4.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.customName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            if (isActive && remainingTimeMs != null && remainingTimeMs >= 0) {
                Text(
                    text = String.format("%.1f", remainingTimeMs / 1000f),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun ExecutionControls(
    status: ExecutionStatus,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (status is ExecutionStatus.Idle || status is ExecutionStatus.Finished) {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.large,
                enabled = enabled
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("START SESSION", style = MaterialTheme.typography.titleLarge)
            }
        } else {
            // Running or Paused
            IconButton(
                onClick = onStop,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.Stop, contentDescription = "Stop", modifier = Modifier.size(48.dp))
            }
            
            FloatingActionButton(
                onClick = { if (status is ExecutionStatus.Running) onPause() else onResume() },
                modifier = Modifier.weight(1f).height(64.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (status is ExecutionStatus.Running) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Toggle Pause"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (status is ExecutionStatus.Running) "PAUSE" else "RESUME")
                }
            }
        }
    }
}

@Composable
fun ManualStopOverlay(onStop: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onStop,
                modifier = Modifier.fillMaxWidth(0.8f).aspectRatio(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                shape = androidx.compose.foundation.shape.CircleShape,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 12.dp)
            ) {
                Text("STOP", style = MaterialTheme.typography.displayMedium)
            }
        }
    }
}

@Composable
fun SavedRoutinesContent(
    routines: List<Routine>,
    onLoadRoutine: (Routine) -> Unit,
    onDeleteRoutine: (Routine) -> Unit,
    onRenameRoutine: (Routine, String) -> Unit
) {
    var renamingRoutine by remember { mutableStateOf<Routine?>(null) }
    
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = "Saved Routines",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (routines.isEmpty()) {
            Text(
                text = "No saved routines yet.",
                modifier = Modifier.padding(vertical = 32.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(routines) { routine ->
                    ListItem(
                        headlineContent = { Text(routine.name) },
                        supportingContent = { Text("${routine.tasks.size} tasks") },
                        trailingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { renamingRoutine = routine }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Rename")
                                }
                                IconButton(onClick = { onDeleteRoutine(routine) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                                Button(onClick = { onLoadRoutine(routine) }) {
                                    Text("Load")
                                }
                            }
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

    if (renamingRoutine != null) {
        var newName by remember { mutableStateOf(renamingRoutine?.name ?: "") }
        AlertDialog(
            onDismissRequest = { renamingRoutine = null },
            title = { Text("Rename Routine") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newName.isNotBlank()) {
                            onRenameRoutine(renamingRoutine!!, newName)
                            renamingRoutine = null
                        }
                    },
                    enabled = newName.isNotBlank()
                ) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = { renamingRoutine = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
