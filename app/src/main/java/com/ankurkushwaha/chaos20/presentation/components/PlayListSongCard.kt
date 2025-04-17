package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ankurkushwaha.chaos20.R
import com.ankurkushwaha.chaos20.domain.model.Song
import com.ankurkushwaha.chaos20.utils.formatDuration

@Composable
fun PlayListSongCard(
    song: Song,
    onClick: () -> Unit,
    onMenuItemClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Song image
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(song.imageUri ?: R.drawable.chaos_bg)
                        .placeholder(R.drawable.chaos_bg)
                        .error(R.drawable.chaos_bg)
                        .build()
                ),
                contentDescription = "Album Art",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Song details
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = song.artist ?: "Unknown Artist",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Duration
        Text(
            text = formatDuration(song.duration),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 4.dp)
        )

        // Menu
        PlaylistSongMenu(
            modifier = Modifier.size(40.dp),
            onMenuItemClick = onMenuItemClick
        )
    }
}

@Composable
fun PlaylistSongMenu(
    modifier: Modifier,
    onMenuItemClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More Options"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Play Next") },
                onClick = {
                    onMenuItemClick("PLAY_NEXT")
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Add to Playlist") },
                onClick = {
                    onMenuItemClick("ADD_TO_PLAYLIST")
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Remove from Playlist") },
                onClick = {
                    onMenuItemClick("REMOVE_FROM_PLAYLIST")
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Details") },
                onClick = {
                    onMenuItemClick("DETAILS")
                    expanded = false
                }
            )
        }
    }
}


//@Preview(showBackground = true)
@Composable
fun PreviewSongCard2() {
    SongCard(
        song = Song(
            id = 1L,
            title = "Blinding Lights",
            artist = "The Weeknd",
            album = "After Hours",
            duration = 200000,
            path = "",
            imageUri = ""
        ),
        onClick = { /* Handle click */ },
        onMenuItemClick = { /* Handle menu item click */ }
    )
}