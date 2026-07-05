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

    override fun getCurrentRoutine(): Flow<Routine?> {
        return routineDao.getCurrentRoutine().map { it?.toDomainModel() }
    }

    override fun getRoutineById(id: String): Flow<Routine?> {
        return routineDao.getRoutineById(id).map { it?.toDomainModel() }
    }

    override suspend fun saveRoutine(routine: Routine) {
        val routineEntity = RoutineEntity.fromDomainModel(routine)
        val taskEntities = routine.tasks.map { TaskEntity.fromDomainModel(routine.id, it) }
        routineDao.updateRoutineWithTasks(routineEntity, taskEntities)
    }

    override suspend fun updateCurrentRoutine(routine: Routine) {
        val routineEntity = RoutineEntity.fromDomainModel(routine.copy(isCurrent = true))
        val taskEntities = routine.tasks.map { TaskEntity.fromDomainModel(routine.id, it) }
        routineDao.updateRoutineWithTasks(routineEntity, taskEntities)
    }

    override suspend fun deleteRoutine(routine: Routine) {
        routineDao.deleteRoutine(RoutineEntity.fromDomainModel(routine))
    }

    override suspend fun clearCurrentRoutine() {
        routineDao.clearCurrentFlag()
    }
}
