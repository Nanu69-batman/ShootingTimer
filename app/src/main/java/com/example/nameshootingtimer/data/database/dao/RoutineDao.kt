package com.example.nameshootingtimer.data.database.dao

import androidx.room.*
import com.example.nameshootingtimer.data.database.entity.RoutineEntity
import com.example.nameshootingtimer.data.database.entity.RoutineWithTasks
import com.example.nameshootingtimer.data.database.entity.TaskEntity
import com.example.nameshootingtimer.domain.model.Routine
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface RoutineDao {
    @Transaction
    @Query("SELECT * FROM routines WHERE id != :workspaceId ORDER BY createdAt DESC")
    fun getSavedRoutines(workspaceId: String = Routine.WORKSPACE_ID): Flow<List<RoutineWithTasks>>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :workspaceId LIMIT 1")
    fun getWorkspaceRoutine(workspaceId: String = Routine.WORKSPACE_ID): Flow<RoutineWithTasks?>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutineById(id: String): RoutineWithTasks?

    @Upsert
    suspend fun upsertRoutineEntity(routine: RoutineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE routineId = :routineId")
    suspend fun deleteTasksForRoutine(routineId: String)

    @Query("DELETE FROM routines WHERE id = :routineId")
    suspend fun deleteRoutineEntity(routineId: String)

    @Transaction
    suspend fun upsertRoutine(routine: RoutineEntity, tasks: List<TaskEntity>) {
        upsertRoutineEntity(routine)
        deleteTasksForRoutine(routine.id)
        insertTasks(tasks)
    }

    @Transaction
    suspend fun deleteRoutineCompletely(routineId: String) {
        deleteTasksForRoutine(routineId)
        deleteRoutineEntity(routineId)
    }

    @Transaction
    suspend fun copyRoutine(fromId: String, toId: String) {
        val source = getRoutineById(fromId) ?: return
        val newRoutine = source.routine.copy(id = toId)
        val newTasks = source.tasks.map { it.copy(id = UUID.randomUUID().toString(), routineId = toId) }
        upsertRoutine(newRoutine, newTasks)
    }
}
