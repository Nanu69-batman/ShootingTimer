package com.example.nameshootingtimer.domain.repository

import com.example.nameshootingtimer.domain.model.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getSavedRoutines(): Flow<List<Routine>>
    fun getCurrentRoutine(): Flow<Routine?>
    fun getRoutineById(id: String): Flow<Routine?>
    suspend fun saveRoutine(routine: Routine)
    suspend fun updateCurrentRoutine(routine: Routine)
    suspend fun deleteRoutine(routine: Routine)
    suspend fun clearCurrentRoutine()
}
