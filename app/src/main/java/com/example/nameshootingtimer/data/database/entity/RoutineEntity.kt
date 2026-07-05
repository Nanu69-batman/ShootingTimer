package com.example.nameshootingtimer.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nameshootingtimer.domain.model.Routine

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val isCurrent: Boolean,
    val createdAt: Long
) {
    fun toDomainModel(): Routine = Routine(
        id = id,
        name = name,
        isCurrent = isCurrent,
        createdAt = createdAt
    )

    companion object {
        fun fromDomainModel(routine: Routine): RoutineEntity = RoutineEntity(
            id = routine.id,
            name = routine.name,
            isCurrent = routine.isCurrent,
            createdAt = routine.createdAt
        )
    }
}
