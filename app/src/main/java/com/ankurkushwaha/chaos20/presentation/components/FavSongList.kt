package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.ankurkushwaha.chaos20.domain.model.Song

@Composable
fun FavSongList(
    songs: List<Song>,
//    songs: LazyPagingItems<Song>,
    onSongClick: (Song) -> Unit,
    onMenuItemClick: (Song, String) -> Unit
) {
    LazyColumn {
        // Using itemsIndexed from Paging to properly handle paging data
        items(songs) { favSong ->
            SongCard(
                song = favSong,
                onClick = { onSongClick(favSong) },
                onMenuItemClick = { action -> onMenuItemClick(favSong, action) }
            )
        }
    }
}