package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankurkushwaha.chaos20.domain.model.Song
import com.ankurkushwaha.chaos20.utils.formatDuration

@Composable
fun DetailDialog(
    song: Song,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(text = "OK")
            }
        },
        title = {
            Text(
                text = song.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Artist: ${song.artist}",
                    fontSize = 15.sp
                )
                Text(
                    text = "Album: ${song.album}",
                    fontSize = 15.sp
                )
                Text(
                    text = "Location: ${song.path}",
                    fontSize = 15.sp
                )
                Text(
                    text = "Duration: ${formatDuration(song.duration)}",
                    fontSize = 15.sp
                )
            }
        }
    )
}