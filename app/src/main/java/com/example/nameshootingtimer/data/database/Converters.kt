package com.example.nameshootingtimer.data.database

import androidx.room.TypeConverter
import com.example.nameshootingtimer.domain.model.Task
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromTask(task: Task): String {
        return Json.encodeToString(task)
    }

    @TypeConverter
    fun toTask(taskJson: String): Task {
        return Json.decodeFromString(taskJson)
    }
}
