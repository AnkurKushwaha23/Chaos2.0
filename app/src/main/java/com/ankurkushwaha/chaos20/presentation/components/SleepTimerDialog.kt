package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SleepTimerDialog(
    isVisible: Boolean,
    initialMinutes: Long = 23,
    initialEnabled: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (enabled: Boolean, minutes: Long) -> Unit
) {
    var minutes by remember { mutableStateOf(initialMinutes) }
    var isEnabled by remember { mutableStateOf(initialEnabled) }

    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    // Title
                    Text(
                        text = "Sleep Timer",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Enable Sleep Timer",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { isEnabled = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Minutes display
                    Text(
                        text = "$minutes minutes",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Slider
                    Slider(
                        value = minutes.toFloat(),
                        onValueChange = { minutes = it.toLong() },
                        valueRange = 5f..90f,
                        steps = 16,  // (90-5)/5 - 1 = 23 steps for 5-minute increments
                        enabled = isEnabled,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Confirm button
                    Button(
                        onClick = { onConfirm(isEnabled, minutes) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SleepTimerDialogPreview() {
    MaterialTheme {
        SleepTimerDialog(
            isVisible = true,
            initialMinutes = 23,
            initialEnabled = true,
            onDismiss = { },
            onConfirm = { _, _ -> }
        )
    }
}