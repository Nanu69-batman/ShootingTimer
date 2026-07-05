package com.example.nameshootingtimer.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Task {
    abstract val id: String
    abstract val name: String
    abstract val orderIndex: Int

    @Serializable
    enum class PlaybackMode {
        TIMED,
        MANUAL_STOP
    }

    @Serializable
    data class WaitTask(
        override val id: String,
        override val name: String = "Wait",
        override val orderIndex: Int,
        val durationMs: Long
    ) : Task()

    @Serializable
    data class BeepTask(
        override val id: String,
        override val name: String = "Beep",
        override val orderIndex: Int,
        val soundId: String,
        val playbackMode: PlaybackMode,
        val durationMs: Long? = null // Null if MANUAL_STOP
    ) : Task()

    @Serializable
    data class VibrateTask(
        override val id: String,
        override val name: String = "Vibrate",
        override val orderIndex: Int,
        val playbackMode: PlaybackMode,
        val durationMs: Long? = null
    ) : Task()

    @Serializable
    data class PlaySoundTask(
        override val id: String,
        override val name: String = "Play Sound",
        override val orderIndex: Int,
        val soundId: String,
        val playbackMode: PlaybackMode,
        val durationMs: Long? = null
    ) : Task()
}
