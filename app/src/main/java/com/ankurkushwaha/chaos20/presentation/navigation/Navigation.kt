package com.ankurkushwaha.chaos20.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ankurkushwaha.chaos20.presentation.fav_screen.FavoriteScreen
import com.ankurkushwaha.chaos20.presentation.home_screen.HomeScreen
import com.ankurkushwaha.chaos20.presentation.home_screen.HomeViewModel
import com.ankurkushwaha.chaos20.presentation.home_screen.MusicViewModel
import com.ankurkushwaha.chaos20.presentation.home_screen.SongDBViewModel
import com.ankurkushwaha.chaos20.presentation.player_screen.PlayerScreen
import com.ankurkushwaha.chaos20.presentation.playlist_screen.PlaylistScreen
import com.ankurkushwaha.chaos20.presentation.search_screen.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    musicViewModel: MusicViewModel,
    songDBViewModel: SongDBViewModel,
    bottomNavViewModel: BottomNavViewModel,
    homeViewModel: HomeViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Home) {
        composable<Screen.Home> {
            LaunchedEffect(key1 = true) {
                bottomNavViewModel.updateCurrentScreen(Screen.Home)
            }
            HomeScreen(
                scrollBehavior = scrollBehavior,
                musicViewModel = musicViewModel,
                songDBViewModel = songDBViewModel,
                homeViewModel = homeViewModel
            )
        }

//        composable<Screen.Player> {
//            PlayerScreen(
//                viewModel = musicViewModel, onBackClick = {
//                    navController.popBackStack()
//                },
//                onMoreOptionsClick = { song, action ->
//                    when (action) {
//                        "ADD_TO_PLAYLIST" -> {
//                            songDBViewModel.setSongToPlaylistSheet(song)
//                            songDBViewModel.showPlaylistSheet()
//                        }
//
//                        "DETAILS" -> {
//                            musicViewModel.showSongDetail(song)
//                        }
//                    }
//                }
//            )
//        }

        composable<Screen.Search> {
            LaunchedEffect(key1 = true) {
                bottomNavViewModel.updateCurrentScreen(Screen.Search)
            }
            SearchScreen(musicViewModel = musicViewModel, homeViewModel = homeViewModel)
        }

        composable<Screen.Favorite> {
            LaunchedEffect(key1 = true) {
                bottomNavViewModel.updateCurrentScreen(Screen.Favorite)
            }
            FavoriteScreen(
                scrollBehavior = scrollBehavior,
                musicViewModel = musicViewModel,
                songDBViewModel = songDBViewModel
            )
        }

        composable<Screen.Playlist> {
            LaunchedEffect(key1 = true) {
                bottomNavViewModel.updateCurrentScreen(Screen.Playlist)
            }
            PlaylistScreen(
                scrollBehavior = scrollBehavior,
                musicViewModel = musicViewModel,
                songDBViewModel = songDBViewModel
            )
        }
    }
}