package com.ankurkushwaha.chaos20.presentation.player_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ankurkushwaha.chaos20.R
import com.ankurkushwaha.chaos20.domain.model.Song
import com.ankurkushwaha.chaos20.presentation.components.MarqueeText
import com.ankurkushwaha.chaos20.presentation.home_screen.MusicViewModel
import com.ankurkushwaha.chaos20.presentation.home_screen.SongDBViewModel
import com.ankurkushwaha.chaos20.utils.formatDuration
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerBottomSheet(
    viewModel: MusicViewModel,
    onBackClick: () -> Unit,
    onMoreOptionsClick: (Song, String) -> Unit,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    // Effect to handle show/hide
    LaunchedEffect(isVisible) {
        if (isVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    // Effect to notify parent when sheet is hidden via gesture
    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            onDismiss()
        }
    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            dragHandle = null, // Remove the default drag handle for a cleaner look
            containerColor = Color.Transparent, // Use transparent background
            modifier = Modifier.fillMaxSize()
        ) {
            // Call the player screen content
            PlayerScreen(
                viewModel = viewModel,
                onBackClick = {
                    scope.launch {
                        sheetState.hide()
                        onBackClick()
                        onDismiss()
                    }
                },
                onMoreOptionsClick = onMoreOptionsClick
            )
        }
    }
}

@Composable
fun PlayerScreen(
    viewModel: MusicViewModel,
    songDBViewModel: SongDBViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onMoreOptionsClick: (Song, String) -> Unit,
) {
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isShuffleOn by viewModel.isShuffleOn.collectAsState()
    val isRepeatOn by viewModel.isRepeatSongOn.collectAsState()
    val currentDuration by viewModel.currentDuration.collectAsState()
    val maxDuration by viewModel.maxDuration.collectAsState()
    var isMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(currentSong?.id) {
        songDBViewModel.isSongFavoriteAsFlow(currentSong!!.id)
    }

    val isFav by songDBViewModel.isFav.collectAsState()

    // Create blurred background
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background with blur effect
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentSong?.imageUri ?: R.drawable.chaos_bg)
                .placeholder(R.drawable.chaos_bg) // This adds the placeholder
                .error(R.drawable.chaos_bg) // Optional: shows this if image loading fails
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 20.dp) // Apply blur effect
        )

        // Semi-transparent overlay for better readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top app bar
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Row {
//                    IconButton(onClick = onShareClick) {
//                        Icon(
//                            imageVector = Icons.Default.Share,
//                            contentDescription = "Share",
//                            tint = Color.White
//                        )
//                    }

                    IconButton(onClick = {
                        currentSong?.let { songDBViewModel.toggleFavoriteSong(it) }
                    }) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFav) MaterialTheme.colorScheme.primary else Color.White
                        )
                    }

//                    IconButton(onClick = onMoreOptionsClick) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert,
//                            contentDescription = "More Options",
//                            tint = Color.White
//                        )
//                    }
                    Box {
                        IconButton(onClick = { isMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Details") },
                                onClick = {
                                    isMenuExpanded = false
                                    onMoreOptionsClick(currentSong!!, "DETAILS")
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Add to Playlist") },
                                onClick = {
                                    isMenuExpanded = false
                                    onMoreOptionsClick(currentSong!!, "ADD_TO_PLAYLIST")
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Album art
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentSong?.imageUri ?: R.drawable.chaos_bg)
                        .placeholder(R.drawable.chaos_bg)
                        .error(R.drawable.chaos_bg)
                        .build(),
                    contentDescription = "Album Art",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Song title and artist
//            MarqueeText(
//                text = currentSong?.title ?: "Unknown",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(40.dp)
//                    .padding(vertical = 4.dp),
//                color = Color.White,
//                fontSize = 24.sp,
//                scrollSpeed = 10 // Adjust speed (lower = faster)
//            )

            Text(
                text = currentSong?.title ?: "Unknown",
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = currentSong?.artist ?: "Unknown Artist",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Seek bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = if (maxDuration > 0) currentDuration.toFloat() / maxDuration.toFloat() else 0f,
                    onValueChange = { position ->
                        // Convert the position (0f to 1f) to milliseconds
                        val newPositionMs = (position * maxDuration).toInt()
                        viewModel.seekTo(newPositionMs)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatDuration(currentDuration.toLong()),
                        color = Color.White
                    )

                    Text(
                        text = formatDuration(maxDuration.toLong()),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Playback controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = viewModel::toggleShuffle) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shuffle),
                        contentDescription = "Shuffle",
                        tint = if (isShuffleOn) MaterialTheme.colorScheme.primary else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = viewModel::playPrevious) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_previous),
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.size(64.dp)
                ) {
                    IconButton(
                        onClick = viewModel::playPause,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = if (isPlaying) painterResource(id = R.drawable.ic_pause) else painterResource(
                                id = R.drawable.ic_play
                            ),
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                IconButton(onClick = viewModel::playNext) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_next),
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                IconButton(onClick = viewModel::toggleRepeat) {
                    Icon(
                        painter = if (isRepeatOn) painterResource(id = R.drawable.ic_repeat_one) else painterResource(
                            id = R.drawable.ic_repeat
                        ),
                        contentDescription = "Repeat",
                        tint = if (isRepeatOn) MaterialTheme.colorScheme.primary else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}