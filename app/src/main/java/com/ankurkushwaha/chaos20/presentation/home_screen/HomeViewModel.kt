package com.ankurkushwaha.chaos20.presentation.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankurkushwaha.chaos20.domain.model.Song
import com.ankurkushwaha.chaos20.domain.repository.MusicRepository
import com.ankurkushwaha.chaos20.domain.repository.PermissionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val permissionRepository: PermissionRepository
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()

    private val _filteredSongs = MutableStateFlow<List<Song>>(emptyList())
    val filterSongs = _filteredSongs.asStateFlow()

    private val _topArtists = MutableStateFlow<List<String>>(emptyList())
    val topArtists: StateFlow<List<String>> = _topArtists

    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted = _permissionsGranted.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        checkPermissions()
        if (_permissionsGranted.value) {
            loadSongs()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    /**
     * Check if all required permissions are granted
     */
    fun checkPermissions() {
        _permissionsGranted.value = permissionRepository.hasRequiredPermissions()
    }

    /**
     * Get the list of permissions that need to be requested
     */
    fun getRequiredPermissions(): Array<String> {
        return permissionRepository.getRequiredPermissions()
    }

    /**
     * Handle permission result after user interaction
     */
    fun onPermissionResult(granted: Boolean) {
        _permissionsGranted.value = granted
        if (granted) {
            loadSongs()
        }
    }

    /**
     * Load all songs directly from MediaStore
     */
    fun loadSongs() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                if (!_permissionsGranted.value) {
                    _errorMessage.value = "Storage permission not granted"
                    return@launch
                }

                val allSongs = musicRepository.fetchSongsFromMediaStore()
                _songs.value = allSongs

            } catch (e: Exception) {
                _errorMessage.value = "Failed to load songs: ${e.localizedMessage}"
                Log.e("HomeViewModel", "Error loading songs", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Fetch the top artists
     */
    fun fetchTopArtists() {
        viewModelScope.launch {
            _topArtists.value = musicRepository.getArtistName()
        }
    }

    /**
     * Search for songs matching the query
     */
    fun searchSongs(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                // If query is empty, show all songs
                _filteredSongs.value = _songs.value
                return@launch
            }

            val filteredSongs = _songs.value.filter { song ->
                song.title.contains(query, ignoreCase = true) ||
                        (song.artist?.contains(query, ignoreCase = true) ?: false) ||
                        song.album.contains(query, ignoreCase = true)
            }
            _filteredSongs.value = filteredSongs
        }
    }

    fun clearSearchResults() {
        _filteredSongs.value = _songs.value
    }
}

//    val paginatedSongs: Flow<PagingData<Song>> = musicRepository.getSongsPaginated()
//        .cachedIn(viewModelScope)

///**
//     * Refresh the database from MediaStore and update the paginated list
//     */
//    fun refreshDatabaseFromMediaStore() {
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                _errorMessage.value = null
//
//                if (!_permissionsGranted.value) {
//                    _errorMessage.value = "Storage permission not granted"
//                    return@launch
//                }
//
//                // Using incremental refresh to be more efficient
//                musicRepository.refreshSongsIncrementally()
//
//                // Also update the non-paginated list for immediate use
//                val allSongs = musicRepository.fetchSongsFromMediaStore()
//                _songs.value = allSongs
//
//            } catch (e: Exception) {
//                _errorMessage.value = "Failed to refresh songs: ${e.localizedMessage}"
//                Log.e("HomeViewModel", "Error refreshing songs", e)
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

/**
 * Refresh the song list and database
 */
//    fun refreshSongs() {
//        refreshDatabaseFromMediaStore()
//    }

///**
//     * Register MediaStore content observer to detect changes
//     */
//    private fun registerMediaStoreObserver() {
//        if (_permissionsGranted.value) {
//            musicRepository.registerContentObserver()
//        }
//    }