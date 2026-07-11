package com.example.nameshootingtimer.ui.screens.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nameshootingtimer.domain.model.Routine
import com.example.nameshootingtimer.domain.model.Task
import com.example.nameshootingtimer.domain.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val repository: RoutineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<Routine?>(null)
    val uiState: StateFlow<Routine?> = _uiState.asStateFlow()

    private val _isModified = MutableStateFlow(false)
    val isModified: StateFlow<Boolean> = _isModified.asStateFlow()

    private val _lastSyncedName = MutableStateFlow<String?>(null)
    val lastSyncedName: StateFlow<String?> = _lastSyncedName.asStateFlow()

    val savedRoutines: StateFlow<List<Routine>> = repository.getSavedRoutines()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.getWorkspaceRoutine().collectLatest { routine ->
                if (routine == null) {
                    val workspace = Routine.createWorkspace()
                    repository.saveRoutine(workspace)
                    _uiState.value = workspace
                } else {
                    _uiState.value = routine
                    // When first opening, we don't necessarily know if it's "synced" with a library entry
                    // but we can check if a name matches to set initial state
                    val currentName = routine.name
                    if (savedRoutines.value.any { it.name.equals(currentName, ignoreCase = true) }) {
                         _lastSyncedName.value = currentName
                    }
                }
            }
        }
    }

    private fun markAsModified() {
        _isModified.value = true
    }

    fun addTask(taskType: String) {
        val current = _uiState.value ?: return
        val newOrderIndex = current.tasks.size
        val newTaskId = UUID.randomUUID().toString()
        
        val newTask = when (taskType) {
            "Wait" -> Task.WaitTask(newTaskId, customName = "Wait", orderIndex = newOrderIndex, durationMs = 30000)
            "Beep" -> Task.BeepTask(newTaskId, customName = "Beep", orderIndex = newOrderIndex, soundId = "default", playbackMode = Task.PlaybackMode.TIMED, durationMs = 1000)
            "Vibrate" -> Task.VibrateTask(newTaskId, customName = "Vibrate", orderIndex = newOrderIndex, playbackMode = Task.PlaybackMode.TIMED, durationMs = 1000)
            "Play Sound" -> Task.PlaySoundTask(newTaskId, customName = "Play Sound", orderIndex = newOrderIndex, soundId = "default", playbackMode = Task.PlaybackMode.TIMED, durationMs = 3000)
            else -> return
        }

        val updated = current.copy(tasks = current.tasks + newTask)
        _uiState.value = updated
        markAsModified()
        viewModelScope.launch { repository.saveRoutine(updated) }
    }

    fun removeTask(taskId: String) {
        val current = _uiState.value ?: return
        val updatedTasks = current.tasks.filter { it.id != taskId }
            .mapIndexed { index, task -> updateTaskOrder(task, index) }
        val updated = current.copy(tasks = updatedTasks)
        _uiState.value = updated
        markAsModified()
        viewModelScope.launch { repository.saveRoutine(updated) }
    }

    fun updateTask(updatedTask: Task) {
        val current = _uiState.value ?: return
        val updatedTasks = current.tasks.map { 
            if (it.id == updatedTask.id) updatedTask else it
        }
        val updated = current.copy(tasks = updatedTasks)
        _uiState.value = updated
        markAsModified()
        viewModelScope.launch { repository.saveRoutine(updated) }
    }

    fun finalizeReorder(finalTasks: List<Task>) {
        val current = _uiState.value ?: return
        val updatedTasks = finalTasks.mapIndexed { index, task -> updateTaskOrder(task, index) }
        val updated = current.copy(tasks = updatedTasks)
        _uiState.value = updated
        markAsModified()
        viewModelScope.launch { repository.saveRoutine(updated) }
    }

    private fun updateTaskOrder(task: Task, index: Int): Task = when(task) {
        is Task.WaitTask -> task.copy(orderIndex = index)
        is Task.BeepTask -> task.copy(orderIndex = index)
        is Task.VibrateTask -> task.copy(orderIndex = index)
        is Task.PlaySoundTask -> task.copy(orderIndex = index)
    }

    fun clearRoutine() {
        val current = _uiState.value ?: return
        val updated = current.copy(tasks = emptyList())
        _uiState.value = updated
        markAsModified()
        viewModelScope.launch { repository.saveRoutine(updated) }
    }

    fun createNewRoutine() {
        val newRoutine = Routine(
            id = Routine.WORKSPACE_ID,
            name = "New Routine",
            tasks = emptyList(),
            isCurrent = true
        )
        _uiState.value = newRoutine
        _isModified.value = false
        _lastSyncedName.value = null
        viewModelScope.launch { repository.saveRoutine(newRoutine) }
    }

    fun checkNameConflict(name: String): Boolean {
        return savedRoutines.value.any { it.name.equals(name, ignoreCase = true) }
    }

    fun saveRoutine() {
        val current = _uiState.value ?: return
        val existingMatch = savedRoutines.value.find { it.name.equals(current.name, ignoreCase = true) }
        
        if (existingMatch != null) {
            viewModelScope.launch {
                val newTasks = current.tasks.map { it.withId(UUID.randomUUID().toString()) }
                val routineToSave = current.copy(id = existingMatch.id, tasks = newTasks, isCurrent = false)
                repository.saveRoutine(routineToSave)
                _isModified.value = false
                _lastSyncedName.value = current.name
            }
        }
    }

    fun saveCurrentRoutineAs(name: String) {
        val current = _uiState.value ?: return
        viewModelScope.launch {
            val existingMatch = savedRoutines.value.find { it.name.equals(name, ignoreCase = true) }
            val targetId = existingMatch?.id ?: UUID.randomUUID().toString()
            
            val newTasks = current.tasks.map { it.withId(UUID.randomUUID().toString()) }
            val newRoutine = current.copy(id = targetId, name = name, tasks = newTasks, isCurrent = false)
            repository.saveRoutine(newRoutine)
            
            val updatedWorkspace = current.copy(name = name)
            _uiState.value = updatedWorkspace
            _isModified.value = false
            _lastSyncedName.value = name
            repository.saveRoutine(updatedWorkspace)
        }
    }

    fun loadRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.loadRoutineToWorkspace(routine.id)
            _isModified.value = false
            _lastSyncedName.value = routine.name
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            if (_lastSyncedName.value == routine.name) {
                _lastSyncedName.value = null
            }
            repository.deleteRoutine(routine.id)
        }
    }

    fun renameRoutine(routine: Routine, newName: String) {
        viewModelScope.launch {
            repository.saveRoutine(routine.copy(name = newName))
        }
    }
}
