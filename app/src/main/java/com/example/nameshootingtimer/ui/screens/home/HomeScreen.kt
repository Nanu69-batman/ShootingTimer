package com.example.nameshootingtimer.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nameshootingtimer.domain.model.Routine
import com.example.nameshootingtimer.ui.screens.routine.RoutineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToEditor: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val routine by viewModel.uiState.collectAsState()
    val savedRoutines by viewModel.savedRoutines.collectAsState()
    var showSavedRoutinesSheet by remember { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current Routine Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Current Routine",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${routine?.tasks?.size ?: 0} Tasks",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavigateToEditor,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Edit Routine")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Start Button
            Button(
                onClick = { /* TODO: Start execution */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("START SESSION", style = MaterialTheme.typography.titleLarge)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { 
                        viewModel.loadSavedRoutines()
                        showSavedRoutinesSheet = true 
                    },
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
                    onDeleteRoutine = { viewModel.deleteRoutine(it) }
                )
            }
        }
    }
}

@Composable
fun SavedRoutinesContent(
    routines: List<Routine>,
    onLoadRoutine: (Routine) -> Unit,
    onDeleteRoutine: (Routine) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
                            Row {
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
}
