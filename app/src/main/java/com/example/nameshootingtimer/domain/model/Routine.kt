package com.example.nameshootingtimer.domain.model

data class Routine(
    val id: String,
    val name: String,
    val tasks: List<Task> = emptyList(),
    val isCurrent: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
