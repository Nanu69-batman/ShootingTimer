package com.example.nameshootingtimer.manager.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager as AndroidAudioManager
import android.media.Ringtone
import android.media.RingtoneManager
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
    
    // Track active resources for termination
    private var activeStreamIds = mutableSetOf<Int>()
    private var activeRingtone: Ringtone? = null

    fun playSound(soundId: String, loop: Boolean = false) {
        when (soundId) {
            "beep", "default" -> {
                toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200) // 200ms standard duration
            }
            "notification" -> {
                playSystemSound(RingtoneManager.TYPE_NOTIFICATION)
            }
            "alarm" -> {
                playSystemSound(RingtoneManager.TYPE_ALARM)
            }
            "ringtone" -> {
                playSystemSound(RingtoneManager.TYPE_RINGTONE)
            }
            else -> {
                val id = soundIds[soundId]
                if (id != null) {
                    val streamId = soundPool.play(id, 1f, 1f, 1, if (loop) -1 else 0, 1f)
                    if (streamId != 0) activeStreamIds.add(streamId)
                } else {
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
                }
            }
        }
    }

    private fun playSystemSound(type: Int) {
        try {
            stopActiveRingtone()
            val uri = RingtoneManager.getDefaultUri(type)
            val ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone?.let {
                it.audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
                activeRingtone = it
                it.play()
            }
        } catch (e: Exception) {
            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
        }
    }

    private fun stopActiveRingtone() {
        activeRingtone?.let {
            if (it.isPlaying) it.stop()
        }
        activeRingtone = null
    }

    fun stopAll() {
        // Stop SoundPool streams
        activeStreamIds.forEach { soundPool.stop(it) }
        activeStreamIds.clear()
        
        // Stop Ringtones
        stopActiveRingtone()
        
        // Stop Tones
        toneGenerator.stopTone()
    }
    
    fun release() {
        soundPool.release()
        toneGenerator.release()
    }
}
