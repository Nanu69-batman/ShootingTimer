package com.example.nameshootingtimer.data.repository

import com.example.nameshootingtimer.data.database.dao.RoutineDao
import com.example.nameshootingtimer.data.database.entity.RoutineEntity
import com.example.nameshootingtimer.data.database.entity.TaskEntity
import com.example.nameshootingtimer.domain.model.Routine
import com.example.nameshootingtimer.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao
) : RoutineRepository {

    override fun getSavedRoutines(): Flow<List<Routine>> {
        return routineDao.getSavedRoutines().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getWorkspaceRoutine(): Flow<Routine?> {
        return routineDao.getWorkspaceRoutine().map { it?.toDomainModel() }
    }

    override suspend fun saveRoutine(routine: Routine) {
        val routineEntity = RoutineEntity.fromDomainModel(routine)
        val taskEntities = routine.tasks.map { TaskEntity.fromDomainModel(routine.id, it) }
        routineDao.upsertRoutine(routineEntity, taskEntities)
    }

    override suspend fun deleteRoutine(routineId: String) {
        routineDao.deleteRoutineCompletely(routineId)
    }

    override suspend fun loadRoutineToWorkspace(routineId: String) {
        routineDao.copyRoutine(routineId, Routine.WORKSPACE_ID)
    }
}
