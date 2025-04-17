package com.ankurkushwaha.chaos20.presentation.playlist_screen

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ankurkushwaha.chaos20.domain.model.Playlist
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.ankurkushwaha.chaos20.domain.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistBottomSheet(
    playlists: List<Playlist> = emptyList(),
    song: Song? = null,
    onAddPlaylistClick: () -> Unit,
    onFavouritesClick: (song: Song) -> Unit,
    onPlaylistClick: (playlistId: Long, song: Song) -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
            onDismissRequest = {
                onDismiss()
            },
            dragHandle = null,
            sheetState = sheetState
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                // First item: Add to playlist option
                item {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        text = "Add to playlist",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
//                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Divider between "Add to playlist" and the playlist items
                item {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                }

                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddPlaylistClick() }
                            .padding(16.dp),
                        textAlign = TextAlign.Start,
                        text = "New",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
//                        color = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFavouritesClick(song!!) }
                            .padding(16.dp),
                        textAlign = TextAlign.Start,
                        text = "Favourites",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
//                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Playlist items
                items(playlists) { playlist ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlaylistClick(playlist.id, song!!) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        Icon(
//                            imageVector = Icons.Default.PlayArrow,
//                            contentDescription = "Playlist",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = playlist.name,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
//                            color = MaterialTheme.colorScheme.primary
                        )
//                        if (playlist.songs.size > 0) {
//                            Text(
//                                text = "${playlist.songs.size} songs",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant
//                            )
//                        }
                    }
                }

                // Add some padding at the bottom for better UX
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}


//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { onAddPlaylistClick() }
//                            .padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Add,
//                            contentDescription = "Add playlist",
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                        Spacer(modifier = Modifier.run { width(16.dp) })
//
//                    }