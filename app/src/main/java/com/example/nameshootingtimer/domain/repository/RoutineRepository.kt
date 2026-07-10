package com.example.nameshootingtimer.domain.repository

import com.example.nameshootingtimer.domain.model.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getSavedRoutines(): Flow<List<Routine>>
    fun getWorkspaceRoutine(): Flow<Routine?>
    suspend fun saveRoutine(routine: Routine)
    suspend fun deleteRoutine(routineId: String)
    suspend fun loadRoutineToWorkspace(routineId: String)
}
