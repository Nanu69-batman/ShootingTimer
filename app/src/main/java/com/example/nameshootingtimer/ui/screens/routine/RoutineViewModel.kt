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

    val savedRoutines: StateFlow<List<Routine>> = repository.getSavedRoutines()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var originalRoutineId: String? = null

    init {
        viewModelScope.launch {
            repository.getWorkspaceRoutine().collectLatest { routine ->
                if (routine == null) {
                    val workspace = Routine.createWorkspace()
                    repository.saveRoutine(workspace)
                    _uiState.value = workspace
                } else {
                    _uiState.value = routine
                }
            }
        }
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
        viewModelScope.launch { repository.saveRoutine(updated) }
    }

    fun removeTask(taskId: String) {
        val current = _uiState.value ?: return
        val updatedTasks = current.tasks.filter { it.id != taskId }
            .mapIndexed { index, task -> updateTaskOrder(task, index) }
        val updated = current.copy(tasks = updatedTasks)
        _uiState.value = updated
        viewModelScope.launch { repository.saveRoutine(updated) }
    }

    fun updateTask(updatedTask: Task) {
        val current = _uiState.value ?: return
        val updatedTasks = current.tasks.map { 
            if (it.id == updatedTask.id) updatedTask else it
        }
        val updated = current.copy(tasks = updatedTasks)
        _uiState.value = updated
        viewModelScope.launch { repository.saveRoutine(updated) }
    }

    fun finalizeReorder(finalTasks: List<Task>) {
        val current = _uiState.value ?: return
        val updatedTasks = finalTasks.mapIndexed { index, task -> updateTaskOrder(task, index) }
        val updated = current.copy(tasks = updatedTasks)
        _uiState.value = updated
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
        viewModelScope.launch { repository.saveRoutine(updated) }
    }

    fun saveRoutine() {
        val current = _uiState.value ?: return
        viewModelScope.launch {
            val targetId = originalRoutineId
            if (targetId != null) {
                // Generate new IDs for tasks to ensure this library entry is independent
                val newTasks = current.tasks.map { it.withId(UUID.randomUUID().toString()) }
                val routineToSave = current.copy(id = targetId, tasks = newTasks, isCurrent = false)
                repository.saveRoutine(routineToSave)
            }
        }
    }

    fun saveCurrentRoutineAs(name: String) {
        val current = _uiState.value ?: return
        viewModelScope.launch {
            val newId = UUID.randomUUID().toString()
            // Generate new IDs for tasks to ensure this library entry is independent
            val newTasks = current.tasks.map { it.withId(UUID.randomUUID().toString()) }
            val newRoutine = current.copy(id = newId, name = name, tasks = newTasks, isCurrent = false)
            repository.saveRoutine(newRoutine)
            
            // Switch workspace to track this new identity
            originalRoutineId = newId
            val updatedWorkspace = current.copy(name = name)
            _uiState.value = updatedWorkspace
            repository.saveRoutine(updatedWorkspace)
        }
    }

    fun loadRoutine(routine: Routine) {
        viewModelScope.launch {
            originalRoutineId = routine.id
            repository.loadRoutineToWorkspace(routine.id)
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.deleteRoutine(routine.id)
        }
    }

    fun renameRoutine(routine: Routine, newName: String) {
        viewModelScope.launch {
            repository.saveRoutine(routine.copy(name = newName))
        }
    }
}
