package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ankurkushwaha.chaos20.R
import com.ankurkushwaha.chaos20.presentation.home_screen.MusicViewModel

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    viewModel: MusicViewModel,
    onMiniPlayerClick: () -> Unit,
) {
    val songState = viewModel.currentSong.collectAsState()
    val song = songState.value
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentProgress by viewModel.currentDuration.collectAsState()
    val maxProgress by viewModel.maxDuration.collectAsState()

    Box (
        modifier = Modifier.fillMaxWidth()
            .background(NavigationBarDefaults.containerColor)
            .clickable { onMiniPlayerClick() }
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (song != null) {
                // Song image
//            Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(song.imageUri ?: R.drawable.chaos_bg)
                                .placeholder(R.drawable.chaos_bg) // This adds the placeholder
                                .error(R.drawable.chaos_bg) // Optional: shows this if image loading fails
                                .build()
                        ),
                        contentDescription = "Album Art",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.width(3.dp))

                // Song details
                Column(
                    modifier = Modifier
                        .weight(0.80f)
                        .padding(vertical = 4.dp, horizontal = 3.dp)
                ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = song.artist ?: "Unknown Artist",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                PlayPauseButtonWithProgress(
                    isPlaying = isPlaying,
                    currentProgress = currentProgress.toFloat(),
                    maxProgress = maxProgress.toFloat(),
                    onClick = viewModel::playPause
                )

                IconButton(
                    onClick = viewModel::playNext,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_next),
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PlayPauseButtonWithProgress(
    isPlaying: Boolean,
    currentProgress: Float, // Value between 0.0f and 1.0f representing the song progress
    maxProgress: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(35.dp)
    ) {
        // Circular progress indicator
        val progress = if (maxProgress > 0) currentProgress / maxProgress else 0f

        CircularProgressIndicator(
            progress = { progress }, // Set progress dynamically
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.primary,  // Covered progress part in black
            trackColor = MaterialTheme.colorScheme.primaryContainer // Remaining progress part in white
        )

        // Play/Pause IconButton
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(35.dp)
        ) {
            Icon(
                painter = if (isPlaying) painterResource(id = R.drawable.ic_pause) else painterResource(
                    id = R.drawable.ic_play
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSongCard() {
//    MiniPlayer(
//        song = Song(
//            id = 1L,
//            title = "Blinding Lights",
//            artist = "The Weeknd",
//            album = "After Hours",
//            duration = 200000,
//            path = "",
//            imageUri = ""
//        ),
//        onMiniPlayerClick = {},
//        onPlayPauseClick = {
//
//        }
//    )
//}