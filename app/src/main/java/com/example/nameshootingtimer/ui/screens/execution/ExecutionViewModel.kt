package com.example.nameshootingtimer.ui.screens.execution

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nameshootingtimer.domain.model.ExecutionState
import com.example.nameshootingtimer.domain.model.ExecutionStatus
import com.example.nameshootingtimer.domain.model.Routine
import com.example.nameshootingtimer.manager.execution.ExecutionEngine
import com.example.nameshootingtimer.manager.execution.ExecutionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExecutionViewModel @Inject constructor(
    application: Application,
    private val engine: ExecutionEngine
) : AndroidViewModel(application) {

    val state: StateFlow<ExecutionState> = engine.state

    init {
        viewModelScope.launch {
            state.collectLatest { 
                if (it.status is ExecutionStatus.Running) {
                    ExecutionService.start(getApplication())
                } else if (it.status is ExecutionStatus.Idle || it.status is ExecutionStatus.Finished) {
                    ExecutionService.stop(getApplication())
                }
            }
        }
    }

    fun start(routine: Routine) {
        engine.start(routine)
    }

    fun pause() {
        engine.pause()
    }

    fun resume() {
        engine.resume()
    }

    fun stop() {
        engine.stop()
    }

    fun onManualStop() {
        engine.onManualStop()
    }
}
