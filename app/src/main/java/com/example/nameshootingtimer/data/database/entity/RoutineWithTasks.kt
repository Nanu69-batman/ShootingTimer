package com.example.nameshootingtimer.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.nameshootingtimer.domain.model.Routine

data class RoutineWithTasks(
    @Embedded
    val routine: RoutineEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "routineId"
    )
    val tasks: List<TaskEntity>
) {
    fun toDomainModel(): Routine {
        return routine.toDomainModel().copy(
            tasks = tasks.map { it.toDomainModel() }.sortedBy { it.orderIndex }
        )
    }
}
