package com.example.nameshootingtimer.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.nameshootingtimer.domain.model.Task

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineId")]
)
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val routineId: String,
    val orderIndex: Int,
    val taskData: Task // This will be handled by a TypeConverter
) {
    fun toDomainModel(): Task = taskData

    companion object {
        fun fromDomainModel(routineId: String, task: Task): TaskEntity = TaskEntity(
            id = task.id,
            routineId = routineId,
            orderIndex = task.orderIndex,
            taskData = task
        )
    }
}
