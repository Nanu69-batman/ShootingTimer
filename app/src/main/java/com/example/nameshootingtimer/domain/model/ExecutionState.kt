package com.example.nameshootingtimer.domain.model

sealed class ExecutionStatus {
    object Idle : ExecutionStatus()
    object Running : ExecutionStatus()
    object Paused : ExecutionStatus()
    object Finished : ExecutionStatus()
}

data class ExecutionState(
    val status: ExecutionStatus = ExecutionStatus.Idle,
    val activeTaskId: String? = null,
    val activeTaskName: String? = null,
    val remainingTimeMs: Long = 0,
    val currentCycle: Int = 1,
    val totalCycles: Int = 1
)
