package com.ankurkushwaha.chaos20.presentation.fav_screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.ankurkushwaha.chaos20.R
import com.ankurkushwaha.chaos20.presentation.components.ChaosTopAppBar
import com.ankurkushwaha.chaos20.presentation.components.EmptyScreen
import com.ankurkushwaha.chaos20.presentation.components.FavSongList
import com.ankurkushwaha.chaos20.presentation.home_screen.MusicViewModel
import com.ankurkushwaha.chaos20.presentation.home_screen.SongDBViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    musicViewModel: MusicViewModel,
    songDBViewModel: SongDBViewModel,
    onSearchClick: () -> Unit = {},
) {
    val isLoading by songDBViewModel.isLoading.collectAsState()
    val favSongs by songDBViewModel.favSongs.collectAsState()
    val errorMessage by songDBViewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            ChaosTopAppBar(
                scrollBehavior = scrollBehavior,
                title = "Favorites",
                onSearchClick = onSearchClick,
                onMenuClick = { musicViewModel.showChaosBottomSheet() }
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
            } else if (favSongs.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // This weight will push the song list to fill available space
                    Box(modifier = Modifier.weight(1f)) {
                        FavSongList(
                            songs = favSongs,
                            onSongClick = { song ->
                                musicViewModel.playSong(song, context)
                                musicViewModel.setMusicList(favSongs)
                                musicViewModel.showPlayer()
                            },
                            onMenuItemClick = { song, action ->
                                when (action) {
                                    "PLAY_NEXT" -> {
                                        musicViewModel.queueNextSong(song)
                                    }

                                    "ADD_TO_PLAYLIST" -> {
                                        songDBViewModel.setSongToPlaylistSheet(song)
                                        songDBViewModel.showPlaylistSheet()
                                    }

                                    "DETAILS" -> {
                                        musicViewModel.showSongDetail(song)
                                    }
                                }
                            }
                        )
                    }
                }
            } else if (errorMessage != null) {
                Log.d("FavoriteScreen", errorMessage.toString())
                EmptyScreen(
                    title = "No favorite songs found",
                    painter = painterResource(R.drawable.undraw_compose)
                )
            } else {
                EmptyScreen(
                    title = "No favorite songs found",
                    painter = painterResource(R.drawable.undraw_compose)
                )
            }
        }
    }
}

// Text(
//                    text = errorMessage ?: "Unknown error",
//                    color = MaterialTheme.colorScheme.error,
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .padding(16.dp)
//                )