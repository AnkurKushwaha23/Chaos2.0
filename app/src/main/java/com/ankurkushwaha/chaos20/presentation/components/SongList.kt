package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.ankurkushwaha.chaos20.domain.model.Song

@Composable
fun SongList(
//    songs: LazyPagingItems<Song>,
    songs: List<Song>,
    onSongClick: (Song) -> Unit,
    onMenuItemClick: (Song, String) -> Unit
) {
    LazyColumn {
        items(songs) { song ->
            SongCard(
                song = song,
                onClick = { onSongClick(song) },
                onMenuItemClick = { action -> onMenuItemClick(song, action) }
            )
        }
    }
}
// Using itemsIndexed from Paging to properly handle paging data
//            count = songs.itemCount,
//            key = { index ->
//                songs[index]?.id ?: index
//            }