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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ankurkushwaha.chaos20.R
import com.ankurkushwaha.chaos20.domain.model.Playlist
import com.ankurkushwaha.chaos20.domain.model.Song

@Composable
fun PlaylistCard(
    playlist: Playlist,
    isExpanded: Boolean,
    onToggleExpand: (playListId: Long) -> Unit,
    onSongClick: (Song) -> Unit,
    onPlaylistMenuClick: (Playlist, String) -> Unit,
    onSongMenuClick: (Song, String) -> Unit
) {
    val backgroundColor = if (isExpanded) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f) // subtle highlight
    } else {
        MaterialTheme.colorScheme.surface
    }

    var isMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onToggleExpand(playlist.id) }
            .padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Playlist image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Gray)
            ) {
                // Use first song image or fallback
                val imagePainter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(playlist.songs.firstOrNull()?.imageUri ?: R.drawable.chaos_bg)
                        .placeholder(R.drawable.chaos_bg)
                        .error(R.drawable.chaos_bg)
                        .build()
                )

                Image(
                    painter = imagePainter,
                    contentDescription = "Playlist Art",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Playlist details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = playlist.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Box {
                IconButton(onClick = { isMenuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu"
                    )
                }

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Rename Playlist") },
                        onClick = {
                            isMenuExpanded = false
                            onPlaylistMenuClick(playlist, "RENAME_PLAYLIST")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Playlist") },
                        onClick = {
                            isMenuExpanded = false
                            onPlaylistMenuClick(playlist, "DELETE_PLAYLIST")
                        }
                    )
                }
            }
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(modifier = Modifier.height(1.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(playlist.songs) { song ->
                    PlayListSongCard(
                        song = song,
                        onClick = { onSongClick(song) },
                        onMenuItemClick = { onSongMenuClick(song, it) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaylistCardPreview() {
    val dummySongs = listOf(
        Song(
            id = 1,
            title = "Sample Song 1",
            artist = "Artist 1",
            album = "Album 1",
            duration = 180000,
            path = "",
            imageUri = null
        ),
        Song(
            id = 2,
            title = "Sample Song 2",
            artist = "Artist 2",
            album = "Album 2",
            duration = 210000,
            path = "",
            imageUri = null
        )
    )

    val dummyPlaylist = Playlist(
        id = 1,
        name = "My Favorite Songs",
        createdAt = System.currentTimeMillis(),
        songs = dummySongs
    )

    PlaylistCard(
        playlist = dummyPlaylist,
        isExpanded = true,
        onToggleExpand = {},
        onSongClick = {},
        onPlaylistMenuClick = { _, _ -> },
        onSongMenuClick = { _, _ -> }
    )
}

//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "${playlist.songs.size} songs",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray
//                )
