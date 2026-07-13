package com.example.nameshootingtimer.ui.screens.routine.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.nameshootingtimer.domain.model.Task

@OptIn(ExperimentalMaterial3Api::class)
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
            FilterChip(
                selected = selectedMode == Task.PlaybackMode.REPEAT_COUNT,
                onClick = { onModeSelected(Task.PlaybackMode.REPEAT_COUNT) },
                label = { Text("Repeat X") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundSelector(
    soundId: String,
    onSoundIdChanged: (String) -> Unit
) {
    val sounds = listOf("default", "beep", "notification", "alarm", "ringtone", "bell", "buzzer", "whistle")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = soundId,
            onValueChange = {},
            readOnly = true,
            label = { Text("Sound") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sounds.forEach { sound ->
                DropdownMenuItem(
                    text = { Text(sound) },
                    onClick = {
                        onSoundIdChanged(sound)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun IntegerInput(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    var textValue by remember(value) { mutableStateOf(value.toString()) }
    OutlinedTextField(
        value = textValue,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                textValue = newValue
                newValue.toIntOrNull()?.let { onValueChange(it) }
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun VibrateToggle(
    vibrate: Boolean,
    onVibrateChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Vibrate")
        Switch(checked = vibrate, onCheckedChange = onVibrateChanged)
    }
}
