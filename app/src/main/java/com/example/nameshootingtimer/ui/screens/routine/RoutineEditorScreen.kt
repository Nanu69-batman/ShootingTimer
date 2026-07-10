package com.example.nameshootingtimer.ui.screens.routine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nameshootingtimer.domain.model.Task
import com.example.nameshootingtimer.ui.screens.routine.components.DurationPicker
import com.example.nameshootingtimer.ui.screens.routine.components.PlaybackModeSelector
import com.example.nameshootingtimer.ui.screens.routine.components.ReorderableTaskColumn
import com.example.nameshootingtimer.ui.screens.routine.components.SoundSelectorPlaceholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineEditorScreen(
    onNavigateBack: () -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val routine by viewModel.uiState.collectAsState()
    var showAddTaskSheet by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var showClearConfirmation by remember { mutableStateOf(false) }
    var showSaveAsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(routine?.name ?: "Routine Editor", style = MaterialTheme.typography.titleMedium)
                        if (routine?.name == "Current Routine") {
                            Text("Unsaved Workspace", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (routine?.name == "Current Routine") {
                            showSaveAsDialog = true
                        } else {
                            viewModel.saveRoutine()
                        }
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                    IconButton(onClick = { showSaveAsDialog = true }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Save As")
                    }
                    IconButton(onClick = { showClearConfirmation = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddTaskSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            routine?.let { currentRoutine ->
                ReorderableTaskColumn(
                    tasks = currentRoutine.tasks,
                    onDragSettle = { finalTasks -> viewModel.finalizeReorder(finalTasks) },
                    taskContent = { task, isDragging ->
                        TaskCard(
                            task = task,
                            isDragging = isDragging,
                            onDelete = { viewModel.removeTask(task.id) },
                            onClick = { editingTask = task }
                        )
                    }
                )
            }
        }

        if (showClearConfirmation) {
            AlertDialog(
                onDismissRequest = { showClearConfirmation = false },
                title = { Text("Clear Routine?") },
                text = { Text("This will remove all tasks from the current routine.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearRoutine()
                            showClearConfirmation = false
                        }
                    ) {
                        Text("Clear")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showSaveAsDialog) {
            var name by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showSaveAsDialog = false },
                title = { Text("Save Routine As") },
                text = {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Routine Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                viewModel.saveCurrentRoutineAs(name)
                                showSaveAsDialog = false
                            }
                        },
                        enabled = name.isNotBlank()
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSaveAsDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showAddTaskSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddTaskSheet = false }
            ) {
                AddTaskContent(
                    onTaskSelected = { type ->
                        viewModel.addTask(type)
                        showAddTaskSheet = false
                    }
                )
            }
        }

        if (editingTask != null) {
            ModalBottomSheet(
                onDismissRequest = { editingTask = null }
            ) {
                EditTaskContent(
                    task = editingTask!!,
                    onTaskUpdated = { updatedTask ->
                        viewModel.updateTask(updatedTask)
                        editingTask = null
                    }
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    isDragging: Boolean,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isDragging, onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isDragging) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Drag Handle",
                tint = if (isDragging) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.customName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${getTaskTypeIcon(task)} ${task.name} • ${getTaskDescription(task)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun EditTaskContent(
    task: Task,
    onTaskUpdated: (Task) -> Unit
) {
    var currentTask by remember(task) { mutableStateOf(task) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Edit Task",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = currentTask.customName,
            onValueChange = { newName ->
                currentTask = when (val t = currentTask) {
                    is Task.WaitTask -> t.copy(customName = newName)
                    is Task.BeepTask -> t.copy(customName = newName)
                    is Task.VibrateTask -> t.copy(customName = newName)
                    is Task.PlaySoundTask -> t.copy(customName = newName)
                }
            },
            label = { Text("Task Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        when (val t = currentTask) {
            is Task.WaitTask -> {
                DurationPicker(
                    label = "Wait Duration",
                    durationMs = t.durationMs,
                    onDurationChanged = { currentTask = t.copy(durationMs = it) }
                )
            }
            is Task.BeepTask -> {
                SoundSelectorPlaceholder(
                    soundId = t.soundId,
                    onSoundIdChanged = { currentTask = t.copy(soundId = it) }
                )
                PlaybackModeSelector(
                    selectedMode = t.playbackMode,
                    onModeSelected = { currentTask = t.copy(playbackMode = it) }
                )
                if (t.playbackMode == Task.PlaybackMode.TIMED) {
                    DurationPicker(
                        label = "Beep Duration",
                        durationMs = t.durationMs ?: 1000L,
                        onDurationChanged = { currentTask = t.copy(durationMs = it) }
                    )
                }
            }
            is Task.VibrateTask -> {
                PlaybackModeSelector(
                    selectedMode = t.playbackMode,
                    onModeSelected = { currentTask = t.copy(playbackMode = it) }
                )
                if (t.playbackMode == Task.PlaybackMode.TIMED) {
                    DurationPicker(
                        label = "Vibration Duration",
                        durationMs = t.durationMs ?: 1000L,
                        onDurationChanged = { currentTask = t.copy(durationMs = it) }
                    )
                }
            }
            is Task.PlaySoundTask -> {
                SoundSelectorPlaceholder(
                    soundId = t.soundId,
                    onSoundIdChanged = { currentTask = t.copy(soundId = it) }
                )
                PlaybackModeSelector(
                    selectedMode = t.playbackMode,
                    onModeSelected = { currentTask = t.copy(playbackMode = it) }
                )
                if (t.playbackMode == Task.PlaybackMode.TIMED) {
                    DurationPicker(
                        label = "Sound Duration",
                        durationMs = t.durationMs ?: 3000L,
                        onDurationChanged = { currentTask = t.copy(durationMs = it) }
                    )
                }
            }
        }

        Button(
            onClick = { onTaskUpdated(currentTask) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun AddTaskContent(
    onTaskSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Add Task",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        val taskTypes = listOf("Wait", "Beep", "Vibrate", "Play Sound")
        taskTypes.forEach { type ->
            ListItem(
                headlineContent = { Text(type) },
                modifier = Modifier.clickable { onTaskSelected(type) }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

private fun getTaskTypeIcon(task: Task): String = when (task) {
    is Task.WaitTask -> "⏱"
    is Task.BeepTask -> "🔊"
    is Task.VibrateTask -> "📳"
    is Task.PlaySoundTask -> "🎵"
}

private fun getTaskDescription(task: Task): String = when (task) {
    is Task.WaitTask -> "${task.durationMs / 1000} seconds"
    is Task.BeepTask -> "Sound: ${task.soundId}, Mode: ${task.playbackMode}"
    is Task.VibrateTask -> "Mode: ${task.playbackMode}"
    is Task.PlaySoundTask -> "Sound: ${task.soundId}, Mode: ${task.playbackMode}"
}
