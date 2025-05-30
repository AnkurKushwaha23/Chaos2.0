package com.ankurkushwaha.chaos20

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ankurkushwaha.chaos20.presentation.components.BottomNavigation
import com.ankurkushwaha.chaos20.presentation.components.DetailDialog
import com.ankurkushwaha.chaos20.presentation.components.DrawerContent
import com.ankurkushwaha.chaos20.presentation.components.InputDialog
import com.ankurkushwaha.chaos20.presentation.components.MiniPlayer
import com.ankurkushwaha.chaos20.presentation.components.SleepTimerDialog
import com.ankurkushwaha.chaos20.presentation.home_screen.HomeViewModel
import com.ankurkushwaha.chaos20.presentation.home_screen.MusicViewModel
import com.ankurkushwaha.chaos20.presentation.home_screen.SleepTimerViewModel
import com.ankurkushwaha.chaos20.presentation.home_screen.SongDBViewModel
import com.ankurkushwaha.chaos20.presentation.navigation.BottomNavViewModel
import com.ankurkushwaha.chaos20.presentation.navigation.Navigation
import com.ankurkushwaha.chaos20.presentation.player_screen.PlayerBottomSheet
import com.ankurkushwaha.chaos20.presentation.playlist_screen.PlaylistBottomSheet
import com.ankurkushwaha.chaos20.services.MusicService
import com.ankurkushwaha.chaos20.ui.theme.Chaos20Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var musicService: MusicService? = null
    private var musicBinder: IBinder? = null
    private var isBound = false

    val musicViewModel: MusicViewModel by viewModels()
    val songDBViewModel: SongDBViewModel by viewModels()
    val bottomNavViewModel: BottomNavViewModel by viewModels()
    val homeViewModel: HomeViewModel by viewModels()
    val sleepTimerViewModel: SleepTimerViewModel by viewModels()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val serviceBinder = binder as MusicService.MusicBinder
            musicService = serviceBinder.getService()
            musicBinder = binder
            isBound = true
            musicViewModel.setMusicService(musicService)
            musicViewModel.setMusicBinder(musicBinder)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            musicService = null
            musicViewModel.setMusicService(null)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            Chaos20Theme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                val snackbarHostState = remember { SnackbarHostState() }


                // Track current screen
                val currentScreen by bottomNavViewModel.currentScreen.collectAsState()

                // State for player bottom sheet
                val showPlayerSheet by musicViewModel.showPlayerSheet.collectAsState()
//                val showChaosSheet by musicViewModel.showChaosSheet.collectAsState()
                val showMiniPlayer by musicViewModel.showMiniPlayer.collectAsState()
                val showDetailDialog by musicViewModel.detailDialogState.collectAsState()

                val showPlaylistSheet by songDBViewModel.showPlaylistSheet.collectAsState()
                val showNewPlaylistDialog by songDBViewModel.newPlaylistDialogState.collectAsState()
                val showRenamePlaylistDialog by songDBViewModel.renamePlaylistDialogState.collectAsState()
                val songForPlaylistSheet by songDBViewModel.songForPlaylistSheet.collectAsState()
                val playlists by songDBViewModel.playlists.collectAsState()
                val currentPlaylist by songDBViewModel.currentPlaylist.collectAsState()

                val showSleepDialog by sleepTimerViewModel.showSleepDialog.collectAsState()
                val timerEnabled by sleepTimerViewModel.timerEnabled.collectAsState()
                val timerMinutes by sleepTimerViewModel.timerMinutes.collectAsState()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier.width(250.dp)
                        ) {
                            DrawerContent(
                                onSleepTimerClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            close()
                                        }
                                    }
                                    sleepTimerViewModel.showSleepDialog()
                                },
                                onReportBugClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            close()
                                        }
                                    }
                                    getMail("Chaos : Bug Report")
                                },
                                onSuggestionsClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            close()
                                        }
                                    }
                                    getMail("Chaos : Suggestions")
                                },
                                onShareClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            close()
                                        }
                                    }
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Check out this Android App: $APP_LINK"
                                        )
                                        type = "text/plain"
                                    }
                                    val chooser =
                                        Intent.createChooser(shareIntent, "Share article via")
                                    startActivity(chooser)
                                },
                                onAboutDeveloperClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            close()
                                        }
                                    }
                                    val intent =
                                        Intent(Intent.ACTION_VIEW, Uri.parse(PORTFOLIO_LINK))
                                    startActivity(intent)
                                }
                            )
                        }
                    },
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        },
                        bottomBar = {
                            BottomNavigation(
                                navController = navController,
                                currentScreen = currentScreen
                            )
                        }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                Navigation(
                                    navController = navController,
                                    musicViewModel = musicViewModel,
                                    songDBViewModel = songDBViewModel,
                                    homeViewModel = homeViewModel,
                                    bottomNavViewModel = bottomNavViewModel,
                                    onMenuClick = {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    }
//                                    scrollBehavior = scrollBehavior
                                )

                                SleepTimerDialog(
                                    isVisible = showSleepDialog,
                                    initialMinutes = timerMinutes,
                                    initialEnabled = timerEnabled,
                                    onDismiss = { sleepTimerViewModel.hideSleepDialog() },
                                    onConfirm = { enabled, minutes ->
                                        sleepTimerViewModel.onSleepTimerConfirm(enabled, minutes)
                                    }
                                )

                                PlayerBottomSheet(
                                    viewModel = musicViewModel,
                                    isVisible = showPlayerSheet,
                                    onDismiss = { musicViewModel.hidePlayer() },
                                    onBackClick = { musicViewModel.hidePlayer() },
                                    onMoreOptionsClick = { song, action ->
                                        when (action) {
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

                                PlaylistBottomSheet(
                                    playlists = playlists,
                                    song = songForPlaylistSheet,
                                    onAddPlaylistClick = {
                                        songDBViewModel.showNewPlaylistDialog()
                                    },
                                    onFavouritesClick = { song ->
                                        songDBViewModel.toggleFavoriteSong(song)
                                        songDBViewModel.hidePlaylistSheet()
                                    },
                                    onPlaylistClick = { playlistId, song ->
                                        songDBViewModel.addSongToPlaylist(
                                            playlistId = playlistId,
                                            song = song
                                        )
                                        songDBViewModel.hidePlaylistSheet()
                                    },
                                    onDismiss = {
                                        songDBViewModel.hidePlaylistSheet()
                                    },
                                    isVisible = showPlaylistSheet
                                )

                            }

                            if (showMiniPlayer) {
                                MiniPlayer(
                                    viewModel = musicViewModel,
                                    onMiniPlayerClick = { musicViewModel.showPlayer() },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            if (showDetailDialog != null) {
                                DetailDialog(
                                    song = showDetailDialog!!,
                                    onConfirm = musicViewModel::hideSongDetail,
                                    onDismiss = musicViewModel::hideSongDetail
                                )
                            }

                            if (showNewPlaylistDialog) {
                                InputDialog(
                                    onConfirm = { playlistName ->
                                        if (playlistName.isNotBlank() && playlistName.isNotEmpty()) {
                                            songDBViewModel.createNewPlaylist(playlistName)
                                            songDBViewModel.hideNewPlaylistDialog()
                                        }
                                    },
                                    onDismissRequest = {
                                        songDBViewModel.hideNewPlaylistDialog()
                                    }
                                )
                            }

                            if (showRenamePlaylistDialog && currentPlaylist != null) {
                                InputDialog(
                                    title = "Rename Playlist",
                                    initialText = currentPlaylist!!.name,
                                    onConfirm = { playlistName ->
                                        if (playlistName.isNotBlank() && playlistName.isNotEmpty()) {
                                            songDBViewModel.renamePlaylist(
                                                currentPlaylist!!.id,
                                                playlistName
                                            )
                                            songDBViewModel.hideRenamePlaylistDialog()
                                        }
                                    },
                                    onDismissRequest = {
                                        songDBViewModel.hideRenamePlaylistDialog()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        broadcastAppAlive()
    }

    private fun broadcastAppAlive() {
        val intent = Intent("com.ankurkushwaha.chaos20.CHAOS_ALIVE")
        sendBroadcast(intent)
    }

    override fun onStart() {
        super.onStart()
        broadcastAppAlive()
        // Bind to the service
        startService(Intent(this, MusicService::class.java)) // Ensures service doesn't die
        bindService(
            Intent(this, MusicService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            this.unbindService(serviceConnection)
            isBound = false
        }
    }

    private fun getMail(subject: String) {
        val uriBuilder = StringBuilder("mailto:" + Uri.encode(EMAIL))
        uriBuilder.append("?subject=" + Uri.encode(subject))
        val uriString = uriBuilder.toString()

        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(uriString))
        startActivity(Intent.createChooser(intent, "Send Suggestions"))
        Toast.makeText(this, "Thanks for Contacting Us !!", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val EMAIL = BuildConfig.EMAIL
        private const val PORTFOLIO_LINK = BuildConfig.PORTFOLIO_LINK
        private const val APP_LINK = BuildConfig.APP_LINK
    }

    override fun onDestroy() {
        super.onDestroy()
        musicService = null
    }
}

//                        topBar = {
//                            ChaosTopAppBar(
//                                onMenuClick = {
//                                    scope.launch {
//                                        drawerState.apply {
//                                            if (isClosed) open() else close()
//                                        }
//                                    }
//                                },
//                                onSearchClick = { navController.navigate(Screen.Search) },
//                            )
//                        },

//                                ChaosBottomSheet(
//                                    isVisible = showChaosSheet,
//                                    onDismiss = { musicViewModel.hideChaosBottomSheet() },
//                                    onSleepTimerClick = { sleepTimerViewModel.showSleepDialog() },
//                                    onReportBugClick = { getMail("Chaos : Bug Report") },
//                                    onSuggestionsClick = { getMail("Chaos : Suggestions") },
//                                    onShareClick = {
//                                        val shareIntent = Intent().apply {
//                                            action = Intent.ACTION_SEND
//                                            putExtra(
//                                                Intent.EXTRA_TEXT,
//                                                "Check out this Android App: $APP_LINK"
//                                            )
//                                            type = "text/plain"
//                                        }
//                                        val chooser =
//                                            Intent.createChooser(shareIntent, "Share article via")
//                                        startActivity(chooser)
//                                    },
//                                    onAboutDeveloperClick = {
//                                        val intent =
//                                            Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_LINK))
//                                        startActivity(intent)
//                                    }
//                                )