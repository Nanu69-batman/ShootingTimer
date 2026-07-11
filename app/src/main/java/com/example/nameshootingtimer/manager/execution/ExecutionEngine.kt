package com.example.nameshootingtimer.manager.execution

import com.example.nameshootingtimer.domain.model.ExecutionState
import com.example.nameshootingtimer.domain.model.ExecutionStatus
import com.example.nameshootingtimer.domain.model.Routine
import com.example.nameshootingtimer.domain.model.Task
import com.example.nameshootingtimer.manager.audio.AudioManager
import com.example.nameshootingtimer.manager.vibration.VibrationManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.coroutineContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExecutionEngine @Inject constructor(
    private val audioManager: AudioManager,
    private val vibrationManager: VibrationManager
) {
    private val _state = MutableStateFlow(ExecutionState())
    val state: StateFlow<ExecutionState> = _state.asStateFlow()

    private var executionJob: Job? = null
    private var currentRoutine: Routine? = null
    
    // Configurable from UI/Settings later
    var repeatCount: Int = 1
    var infiniteLoop: Boolean = false

    fun start(routine: Routine) {
        currentRoutine = routine
        _state.value = ExecutionState(
            status = ExecutionStatus.Running,
            totalCycles = if (infiniteLoop) -1 else repeatCount
        )
        
        executionJob?.cancel()
        executionJob = CoroutineScope(Dispatchers.Default).launch {
            runRoutine()
        }
    }

    fun pause() {
        if (_state.value.status is ExecutionStatus.Running) {
            _state.value = _state.value.copy(status = ExecutionStatus.Paused)
            executionJob?.cancel()
        }
    }

    fun resume() {
        if (_state.value.status is ExecutionStatus.Paused) {
            _state.value = _state.value.copy(status = ExecutionStatus.Running)
            executionJob = CoroutineScope(Dispatchers.Default).launch {
                runRoutine(resume = true)
            }
        }
    }

    fun stop() {
        _state.value = ExecutionState(status = ExecutionStatus.Idle)
        executionJob?.cancel()
        audioManager.stopAll()
        vibrationManager.stop()
    }

    private suspend fun runRoutine(resume: Boolean = false) {
        val routine = currentRoutine ?: return
        
        var cycle = _state.value.currentCycle
        
        while (infiniteLoop || cycle <= repeatCount) {
            _state.value = _state.value.copy(currentCycle = cycle)
            
            // If resuming, we might want to pick up from where we left off
            // For now, let's keep it simple and restart the routine or specific task
            
            for (task in routine.tasks) {
                // Skip if we are resuming and already passed this task (logic TBD)
                
                executeTask(task)
                
                if (!coroutineContext.isActive) break
            }
            
            if (!coroutineContext.isActive) break
            cycle++
        }
        
        if (coroutineContext.isActive) {
            _state.value = _state.value.copy(status = ExecutionStatus.Finished)
        }
    }

    private suspend fun executeTask(task: Task) {
        _state.value = _state.value.copy(
            activeTaskId = task.id,
            activeTaskName = task.customName
        )
        
        when (task) {
            is Task.WaitTask -> {
                runTimer(task.durationMs)
            }
            is Task.BeepTask -> {
                if (task.vibrate) vibrationManager.vibrate(100) // Small vibration for each beep start?
                
                when (task.playbackMode) {
                    Task.PlaybackMode.TIMED -> {
                        val duration = task.durationMs ?: 0L
                        val startTime = System.currentTimeMillis()
                        while (System.currentTimeMillis() - startTime < duration) {
                            audioManager.playSound(task.soundId)
                            if (task.vibrate) vibrationManager.vibrate(100)
                            delay(task.gapMs)
                            if (!coroutineContext.isActive) break
                        }
                    }
                    Task.PlaybackMode.REPEAT_COUNT -> {
                        repeat(task.repeatCount) {
                            audioManager.playSound(task.soundId)
                            if (task.vibrate) vibrationManager.vibrate(100)
                            delay(task.gapMs)
                            if (!coroutineContext.isActive) return@repeat
                        }
                    }
                    Task.PlaybackMode.MANUAL_STOP -> {
                        _state.value = _state.value.copy(remainingTimeMs = -1)
                        val job = CoroutineScope(coroutineContext).launch {
                            while (isActive) {
                                audioManager.playSound(task.soundId)
                                if (task.vibrate) vibrationManager.vibrate(100)
                                delay(task.gapMs)
                            }
                        }
                        suspendUntilManualStop()
                        job.cancel()
                        audioManager.stopAll()
                    }
                }
            }
            is Task.VibrateTask -> {
                if (task.playbackMode == Task.PlaybackMode.TIMED) {
                    vibrationManager.vibrate(task.durationMs ?: 0L)
                    runTimer(task.durationMs ?: 0L)
                } else {
                    _state.value = _state.value.copy(remainingTimeMs = -1)
                    vibrationManager.startContinuous()
                    suspendUntilManualStop()
                    vibrationManager.stop()
                }
            }
            is Task.PlaySoundTask -> {
                if (task.vibrate) vibrationManager.vibrate(200)

                when (task.playbackMode) {
                    Task.PlaybackMode.TIMED -> {
                        audioManager.playSound(task.soundId)
                        runTimer(task.durationMs ?: 0L)
                    }
                    Task.PlaybackMode.REPEAT_COUNT -> {
                        repeat(task.repeatCount) {
                            audioManager.playSound(task.soundId)
                            // We need to know the duration of the sound to gap it properly
                            // For now, let's assume a fixed gap or wait for sound to finish if we had that info
                            delay(1000) // Placeholder gap
                            if (!coroutineContext.isActive) return@repeat
                        }
                    }
                    Task.PlaybackMode.MANUAL_STOP -> {
                        _state.value = _state.value.copy(remainingTimeMs = -1)
                        audioManager.playSound(task.soundId, loop = true)
                        suspendUntilManualStop()
                        audioManager.stopAll()
                    }
                }
            }
        }
    }

    private suspend fun runTimer(durationMs: Long) {
        var remaining = durationMs
        val step = 100L
        while (remaining > 0) {
            _state.value = _state.value.copy(remainingTimeMs = remaining)
            delay(step)
            remaining -= step
        }
        _state.value = _state.value.copy(remainingTimeMs = 0)
    }

    private var manualStopCompletable: CompletableDeferred<Unit>? = null

    private suspend fun suspendUntilManualStop() {
        manualStopCompletable = CompletableDeferred()
        manualStopCompletable?.await()
        manualStopCompletable = null
    }

    fun onManualStop() {
        manualStopCompletable?.complete(Unit)
    }
}
