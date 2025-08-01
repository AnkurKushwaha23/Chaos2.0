package com.ankurkushwaha.chaos20.presentation.album_screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.ankurkushwaha.chaos20.R
import com.ankurkushwaha.chaos20.presentation.components.ChaosTopAppBar
import com.ankurkushwaha.chaos20.presentation.components.EmptyScreen
import com.ankurkushwaha.chaos20.presentation.components.PlaylistCard
import com.ankurkushwaha.chaos20.presentation.home_screen.MusicViewModel
import com.ankurkushwaha.chaos20.presentation.home_screen.SongDBViewModel
import kotlin.toString

/**
 * @author Ankur Kushwaha;
 * created at 22 July 2025 16:10
 */


@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    songDBViewModel: SongDBViewModel,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
//    scrollBehavior: TopAppBarScrollBehavior,
) {
    val isLoading by songDBViewModel.isLoading.collectAsState()
    val playlists by songDBViewModel.playlists.collectAsState()
    val errorMessage by songDBViewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    var expandedPlaylistIds by remember { mutableStateOf(setOf<Long>()) }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            ChaosTopAppBar(
                title = "Albums",
                onSearchClick = onSearchClick,
                onMenuClick = onMenuClick
//                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValue ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (playlists.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(playlists) { playlist ->
                            val isExpanded = expandedPlaylistIds.contains(playlist.id)
                            PlaylistCard(
                                playlist = playlist,
                                isExpanded = isExpanded,
                                onToggleExpand = { playlistId ->
                                    if (isExpanded) {
                                        expandedPlaylistIds = expandedPlaylistIds - playlistId
                                    } else {
                                        if (playlist.songs.isEmpty()) {
                                            songDBViewModel.loadPlaylistWithSongs(playlistId)
                                        }
                                        expandedPlaylistIds = expandedPlaylistIds + playlistId
                                    }
                                },
                                onSongClick = { song ->
                                    musicViewModel.playSong(song, context)
                                    musicViewModel.setMusicList(playlist.songs)
                                    musicViewModel.showPlayer()
                                },
                                onPlaylistMenuClick = { playlist, action ->
                                    when (action) {
                                        "RENAME_PLAYLIST" -> {
                                            songDBViewModel.setCurrentPlaylist(playlist)
                                            songDBViewModel.showRenamePlaylistDialog()
                                        }

                                        "DELETE_PLAYLIST" -> {
                                            songDBViewModel.deletePlaylist(playlist)
                                        }
                                    }
                                },
                                onSongMenuClick = { song, action ->
                                    when (action) {
                                        "PLAY_NEXT" -> {
                                            musicViewModel.queueNextSong(song)
                                        }

                                        "ADD_TO_PLAYLIST" -> {
                                            songDBViewModel.setSongToPlaylistSheet(song)
                                            songDBViewModel.showPlaylistSheet()
                                        }

                                        "REMOVE_FROM_PLAYLIST" -> {
                                            songDBViewModel.removeSongFromPlaylist(
                                                playlist.id,
                                                song.id
                                            )
                                        }

                                        "DETAILS" -> {
                                            musicViewModel.showSongDetail(song)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            } else if (errorMessage != null) {
                Log.d("PlaylistScreen", errorMessage.toString())
                EmptyScreen(
                    title = "No Playlist found",
                    painter = painterResource(R.drawable.undraw_playlist)
                )
            } else {
                EmptyScreen(
                    title = "No Playlist found",
                    painter = painterResource(R.drawable.undraw_playlist)
                )
            }
        }
    }
}