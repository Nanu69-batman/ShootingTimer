package com.example.nameshootingtimer.ui.screens.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nameshootingtimer.domain.model.Routine
import com.example.nameshootingtimer.domain.model.Task
import com.example.nameshootingtimer.domain.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val repository: RoutineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<Routine?>(null)
    val uiState: StateFlow<Routine?> = _uiState.asStateFlow()

    init {
        loadCurrentRoutine()
    }

    private fun loadCurrentRoutine() {
        viewModelScope.launch {
            repository.getCurrentRoutine().collectLatest { routine ->
                if (routine == null) {
                    val newRoutine = Routine(
                        id = UUID.randomUUID().toString(),
                        name = "Current Routine",
                        isCurrent = true
                    )
                    repository.updateCurrentRoutine(newRoutine)
                } else {
                    _uiState.value = routine
                }
            }
        }
    }

    fun addTask(taskType: String) {
        val currentRoutine = _uiState.value ?: return
        val newOrderIndex = currentRoutine.tasks.size
        val newTaskId = UUID.randomUUID().toString()
        
        val newTask = when (taskType) {
            "Wait" -> Task.WaitTask(newTaskId, orderIndex = newOrderIndex, durationMs = 30000)
            "Beep" -> Task.BeepTask(newTaskId, orderIndex = newOrderIndex, soundId = "default", playbackMode = Task.PlaybackMode.TIMED, durationMs = 1000)
            "Vibrate" -> Task.VibrateTask(newTaskId, orderIndex = newOrderIndex, playbackMode = Task.PlaybackMode.TIMED, durationMs = 1000)
            "Play Sound" -> Task.PlaySoundTask(newTaskId, orderIndex = newOrderIndex, soundId = "default", playbackMode = Task.PlaybackMode.TIMED, durationMs = 3000)
            else -> return
        }

        val updatedTasks = currentRoutine.tasks + newTask
        saveUpdatedRoutine(currentRoutine.copy(tasks = updatedTasks))
    }

    fun removeTask(taskId: String) {
        val currentRoutine = _uiState.value ?: return
        val updatedTasks = currentRoutine.tasks.filter { it.id != taskId }
            .mapIndexed { index, task -> 
                when(task) {
                    is Task.WaitTask -> task.copy(orderIndex = index)
                    is Task.BeepTask -> task.copy(orderIndex = index)
                    is Task.VibrateTask -> task.copy(orderIndex = index)
                    is Task.PlaySoundTask -> task.copy(orderIndex = index)
                }
            }
        saveUpdatedRoutine(currentRoutine.copy(tasks = updatedTasks))
    }

    fun updateTask(updatedTask: Task) {
        val currentRoutine = _uiState.value ?: return
        val updatedTasks = currentRoutine.tasks.map { 
            if (it.id == updatedTask.id) updatedTask else it
        }
        saveUpdatedRoutine(currentRoutine.copy(tasks = updatedTasks))
    }

    fun finalizeReorder(finalTasks: List<Task>) {
        val currentRoutine = _uiState.value ?: return
        val updatedTasks = finalTasks.mapIndexed { index, task ->
            when(task) {
                is Task.WaitTask -> task.copy(orderIndex = index)
                is Task.BeepTask -> task.copy(orderIndex = index)
                is Task.VibrateTask -> task.copy(orderIndex = index)
                is Task.PlaySoundTask -> task.copy(orderIndex = index)
            }
        }
        saveUpdatedRoutine(currentRoutine.copy(tasks = updatedTasks))
    }

    fun clearRoutine() {
        val currentRoutine = _uiState.value ?: return
        saveUpdatedRoutine(currentRoutine.copy(tasks = emptyList()))
    }

    fun saveCurrentRoutineAs(name: String) {
        val currentRoutine = _uiState.value ?: return
        viewModelScope.launch {
            val savedRoutine = currentRoutine.copy(
                id = UUID.randomUUID().toString(),
                name = name,
                isCurrent = false,
                createdAt = System.currentTimeMillis()
            )
            repository.saveRoutine(savedRoutine)
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }

    private val _savedRoutines = MutableStateFlow<List<Routine>>(emptyList())
    val savedRoutines: StateFlow<List<Routine>> = _savedRoutines.asStateFlow()

    fun loadSavedRoutines() {
        viewModelScope.launch {
            repository.getSavedRoutines().collectLatest { routines ->
                _savedRoutines.value = routines
            }
        }
    }

    fun loadRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.updateCurrentRoutine(routine.copy(isCurrent = true))
        }
    }

    private fun saveUpdatedRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.updateCurrentRoutine(routine)
        }
    }
}
