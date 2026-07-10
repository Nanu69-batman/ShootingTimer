package com.example.nameshootingtimer.domain.model

data class Routine(
    val id: String,
    val name: String,
    val tasks: List<Task> = emptyList(),
    val isCurrent: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val WORKSPACE_ID = "WORKSPACE_ID"
        
        fun createWorkspace() = Routine(
            id = WORKSPACE_ID,
            name = "Current Routine",
            isCurrent = true
        )
    }
}
