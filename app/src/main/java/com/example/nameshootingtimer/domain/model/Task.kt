package com.example.nameshootingtimer.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Task {
    abstract val id: String
    abstract val name: String // This is the task type name (Wait, Beep, etc.)
    abstract val customName: String // User defined heading
    abstract val orderIndex: Int

    abstract fun withId(newId: String): Task

    @Serializable
    enum class PlaybackMode {
        TIMED,
        MANUAL_STOP,
        REPEAT_COUNT
    }

    @Serializable
    data class WaitTask(
        override val id: String,
        override val name: String = "Wait",
        override val customName: String = "Wait",
        override val orderIndex: Int,
        val durationMs: Long
    ) : Task() {
        override fun withId(newId: String) = copy(id = newId)
    }

    @Serializable
    data class BeepTask(
        override val id: String,
        override val name: String = "Beep",
        override val customName: String = "Beep",
        override val orderIndex: Int,
        val soundId: String,
        val playbackMode: PlaybackMode,
        val durationMs: Long? = null,
        val repeatCount: Int = 1,
        val gapMs: Long = 500,
        val vibrate: Boolean = false
    ) : Task() {
        override fun withId(newId: String) = copy(id = newId)
    }

    @Serializable
    data class VibrateTask(
        override val id: String,
        override val name: String = "Vibrate",
        override val customName: String = "Vibrate",
        override val orderIndex: Int,
        val playbackMode: PlaybackMode,
        val durationMs: Long? = null
    ) : Task() {
        override fun withId(newId: String) = copy(id = newId)
    }

    @Serializable
    data class PlaySoundTask(
        override val id: String,
        override val name: String = "Play Sound",
        override val customName: String = "Play Sound",
        override val orderIndex: Int,
        val soundId: String,
        val playbackMode: PlaybackMode,
        val durationMs: Long? = null,
        val repeatCount: Int = 1,
        val vibrate: Boolean = false
    ) : Task() {
        override fun withId(newId: String) = copy(id = newId)
    }
}
