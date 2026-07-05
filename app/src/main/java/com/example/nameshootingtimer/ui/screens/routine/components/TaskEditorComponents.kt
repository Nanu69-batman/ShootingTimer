package com.example.nameshootingtimer.ui.screens.routine.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.nameshootingtimer.domain.model.Task

@Composable
fun DurationPicker(
    label: String,
    durationMs: Long,
    onDurationChanged: (Long) -> Unit
) {
    var textValue by remember(durationMs) { mutableStateOf((durationMs / 1000).toString()) }

    OutlinedTextField(
        value = textValue,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                textValue = newValue
                newValue.toLongOrNull()?.let { onDurationChanged(it * 1000) }
            }
        },
        label = { Text(label) },
        suffix = { Text("sec") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PlaybackModeSelector(
    selectedMode: Task.PlaybackMode,
    onModeSelected: (Task.PlaybackMode) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Playback Mode",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedMode == Task.PlaybackMode.TIMED,
                onClick = { onModeSelected(Task.PlaybackMode.TIMED) },
                label = { Text("Timed") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = selectedMode == Task.PlaybackMode.MANUAL_STOP,
                onClick = { onModeSelected(Task.PlaybackMode.MANUAL_STOP) },
                label = { Text("Manual Stop") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SoundSelectorPlaceholder(
    soundId: String,
    onSoundIdChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = soundId,
        onValueChange = onSoundIdChanged,
        label = { Text("Sound ID") },
        placeholder = { Text("e.g. bell, buzzer") },
        modifier = Modifier.fillMaxWidth()
    )
}
