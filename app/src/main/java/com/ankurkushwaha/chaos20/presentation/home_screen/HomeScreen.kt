package com.ankurkushwaha.chaos20.presentation.home_screen

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.ankurkushwaha.chaos20.R
import com.ankurkushwaha.chaos20.presentation.components.ChaosTopAppBar
import com.ankurkushwaha.chaos20.presentation.components.EmptyScreen
import com.ankurkushwaha.chaos20.presentation.components.SongList
import com.ankurkushwaha.chaos20.presentation.components.StoragePermissionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    musicViewModel: MusicViewModel,
    songDBViewModel: SongDBViewModel,
    homeViewModel: HomeViewModel,
    onSearchClick: () -> Unit = {},
) {
    val songs by homeViewModel.songs.collectAsState()
    val permissionsGranted by homeViewModel.permissionsGranted.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val errorMessage by homeViewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Check if all required permissions are granted
        val allGranted = permissions.all { it.value }
        homeViewModel.onPermissionResult(allGranted)
    }

    LaunchedEffect(Unit) {
        homeViewModel.checkPermissions()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopAppBar
//            ChaosTopAppBar(
//                scrollBehavior = scrollBehavior,
//                onSearchClick = onSearchClick,
//                onMenuClick = { musicViewModel.showChaosBottomSheet() }
//            )

            // Content
            if (permissionsGranted) {
                // Show song list when permissions are granted
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else if (songs.isNotEmpty()) {
                        SongList(
                            songs = songs,
                            onSongClick = { song ->
                                musicViewModel.playSong(song, context)
                                musicViewModel.setMusicList(songs)
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
                    } else if (errorMessage != null) {
                        Log.d("HomeScreen", errorMessage.toString())
                        EmptyScreen(
                            title = "No songs found",
                            painter = painterResource(R.drawable.undraw_compose)
                        )
                    } else {
                        EmptyScreen(
                            title = "No songs found",
                            painter = painterResource(R.drawable.undraw_compose)
                        )
                    }
                }
            } else {
                // Show permission screen when permissions are not granted
                StoragePermissionScreen(
                    onPermissionClick = {
                        permissionLauncher.launch(homeViewModel.getRequiredPermissions())
                    }
                )
            }
        }
    }
}
// Text(
//                            text = errorMessage ?: "Unknown error",
//                            color = MaterialTheme.colorScheme.error,
//                            modifier = Modifier
//                                .align(Alignment.Center)
//                                .padding(16.dp)
//                        )
//                        Text(
//                            text = "No songs found",
//                            modifier = Modifier
//                                .align(Alignment.Center)
//                                .padding(16.dp)
//                        )