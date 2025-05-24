package com.ankurkushwaha.chaos20.presentation.search_screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.ankurkushwaha.chaos20.presentation.components.SongCard
import com.ankurkushwaha.chaos20.presentation.home_screen.HomeViewModel
import com.ankurkushwaha.chaos20.presentation.home_screen.MusicViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    homeViewModel: HomeViewModel,
    musicViewModel: MusicViewModel,
) {
    val songs by homeViewModel.filterSongs.collectAsState()
    val artists by homeViewModel.topArtists.collectAsState()
    // State for search query
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSuggestionChipsVisible by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Check if all required permissions are granted
        val allGranted = permissions.all { it.value }
        homeViewModel.onPermissionResult(allGranted)
    }

    LaunchedEffect(Unit) {
        homeViewModel.fetchTopArtists()
    }

    // Focus on the search bar when screen opens
    LaunchedEffect(key1 = Unit) {
        delay(500)
        focusRequester.requestFocus()
    }

    // List of placeholder suggestions
    val placeholderSuggestions = remember {
        listOf("Search songs...", "Search artists...", "Search albums...")
    }

    // State for the current placeholder index
    var currentPlaceholderIndex by remember { mutableStateOf(0) }

    // Rotate the placeholder text every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentPlaceholderIndex = (currentPlaceholderIndex + 1) % placeholderSuggestions.size
        }
    }

    // Get current placeholder text
    val currentPlaceholder = placeholderSuggestions[currentPlaceholderIndex]


    // Debounce the search query to avoid too many API calls
    val debouncedSearchQuery by remember(searchQuery) {
        derivedStateOf { searchQuery }
    }

    // Call searchSongs when the debounced query changes
    LaunchedEffect(debouncedSearchQuery) {
        if (debouncedSearchQuery.isNotEmpty()) {
            homeViewModel.searchSongs(debouncedSearchQuery)
        } else {
            // Optionally clear search results or show all songs
            homeViewModel.clearSearchResults()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            ) {
                SearchBar(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { isSuggestionChipsVisible = it.isFocused },
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {

                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    placeholder = {
                        if (searchQuery.isEmpty()) {
                            AnimatedPlaceholder(currentPlaceholder)
                        }
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (searchQuery.isNotEmpty()) searchQuery = ""
                                else {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                        }
                    },
                    active = false,
                    onActiveChange = {},
                    content = {}
                )
            }
            AnimatedVisibility(visible = isSuggestionChipsVisible) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(artists) { artist ->
                        SuggestionChip(
                            onClick = {
                                searchQuery = artist
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            label = { Text(text = artist) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(songs) { song ->
                    SongCard(
                        song = song,
                        onClick = {
                            musicViewModel.playSong(song, context)
                            musicViewModel.setMusicList(songs)
                            musicViewModel.showPlayer()
                        },
                        onMenuItemClick = { action ->
                            when (action) {
                                "PLAY_NEXT" -> {
                                    musicViewModel.queueNextSong(song)
                                }

                                "ADD_TO_PLAYLIST" -> {

                                }

                                "DETAILS" -> {
                                    musicViewModel.showSongDetail(song)
                                }

                                "DELETE" -> {
                                    if (homeViewModel.hasExternalStoragePermission()) {
                                        homeViewModel.deleteSong(song)
                                    } else {
                                        permissionLauncher.launch(homeViewModel.getRequiredPermissions())
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun AnimatedPlaceholder(text: String) {
    AnimatedVisibility(
        visible = true, // Control visibility externally if needed
        enter = slideInVertically(
            initialOffsetY = { -it }, // Slides in from top (negative Y)
            animationSpec = tween(durationMillis = 200)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }, // Slides out to bottom (positive Y)
            animationSpec = tween(durationMillis = 200)
        )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        // Search bar as first item
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp, vertical = 12.dp),
//            shape = RoundedCornerShape(28.dp),
//            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
//        ) {
//            Row(
//                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Search",
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                BasicTextField(
//                    value = searchQuery,
//                    onValueChange = { searchQuery = it },
//                    textStyle = MaterialTheme.typography.bodyLarge.copy(
//                        color = MaterialTheme.colorScheme.onSurface
//                    ),
//                    decorationBox = { innerTextField ->
//                        Box {
//                            if (searchQuery.isEmpty()) {
//                                AnimatedPlaceholder(currentPlaceholder)
//                            }
//                            innerTextField()
//                        }
//                    },
//                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                )
//            }
//        }
//
//        // Results List

//    }