package com.example.nameshootingtimer.data.database.dao

import androidx.room.*
import com.example.nameshootingtimer.data.database.entity.RoutineEntity
import com.example.nameshootingtimer.data.database.entity.RoutineWithTasks
import com.example.nameshootingtimer.data.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Transaction
    @Query("SELECT * FROM routines WHERE isCurrent = 0 ORDER BY createdAt DESC")
    fun getSavedRoutines(): Flow<List<RoutineWithTasks>>

    @Transaction
    @Query("SELECT * FROM routines WHERE isCurrent = 1 LIMIT 1")
    fun getCurrentRoutine(): Flow<RoutineWithTasks?>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :routineId")
    fun getRoutineById(routineId: String): Flow<RoutineWithTasks?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE routineId = :routineId")
    suspend fun deleteTasksForRoutine(routineId: String)

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity)

    @Transaction
    suspend fun updateRoutineWithTasks(routine: RoutineEntity, tasks: List<TaskEntity>) {
        insertRoutine(routine)
        deleteTasksForRoutine(routine.id)
        insertTasks(tasks)
    }

    @Query("UPDATE routines SET isCurrent = 0 WHERE isCurrent = 1")
    suspend fun clearCurrentFlag()

    @Transaction
    suspend fun setCurrentRoutine(routineId: String) {
        clearCurrentFlag()
        // Assuming the routine already exists, otherwise we'd need to fetch and re-insert or just update the flag
        // However, the project says "the application always contains one editable Current Routine"
        // So we might need to handle the case where we 'load' a saved routine into current.
    }
}
