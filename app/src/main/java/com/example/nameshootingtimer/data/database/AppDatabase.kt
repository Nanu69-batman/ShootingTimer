package com.example.nameshootingtimer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nameshootingtimer.data.database.dao.RoutineDao
import com.example.nameshootingtimer.data.database.entity.RoutineEntity
import com.example.nameshootingtimer.data.database.entity.TaskEntity

@Database(
    entities = [RoutineEntity::class, TaskEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    companion object {
        const val DATABASE_NAME = "shooting_timer_db"
    }
}
