package com.example.nameshootingtimer.manager.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager as AndroidAudioManager
import android.media.SoundPool
import android.media.ToneGenerator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val toneGenerator = ToneGenerator(AndroidAudioManager.STREAM_NOTIFICATION, 100)

    // Placeholder for sound IDs
    private val soundIds = mutableMapOf<String, Int>()

    fun playSound(soundId: String, loop: Boolean = false) {
        if (soundId == "default" || !soundIds.containsKey(soundId)) {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP)
            return
        }
        val id = soundIds[soundId] ?: return
        soundPool.play(id, 1f, 1f, 1, if (loop) -1 else 0, 1f)
    }

    fun stopAll() {
        soundPool.autoPause()
        toneGenerator.stopTone()
    }
    
    fun release() {
        soundPool.release()
        toneGenerator.release()
    }
}
