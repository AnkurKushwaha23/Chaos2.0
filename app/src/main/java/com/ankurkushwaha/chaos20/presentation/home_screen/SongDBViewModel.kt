package com.ankurkushwaha.chaos20.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankurkushwaha.chaos20.domain.model.Playlist
import com.ankurkushwaha.chaos20.domain.model.Song
import com.ankurkushwaha.chaos20.domain.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SongDBViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _favSongs = MutableStateFlow<List<Song>>(emptyList())
    val favSongs = _favSongs.asStateFlow()

    private val _isFav = MutableStateFlow<Boolean>(false)
    val isFav = _isFav.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Add this for controlling the player sheet visibility
    private val _showPlaylistSheet = MutableStateFlow(false)
    val showPlaylistSheet = _showPlaylistSheet.asStateFlow()

    private val _songForPlaylistSheet = MutableStateFlow<Song?>(null)
    val songForPlaylistSheet = _songForPlaylistSheet.asStateFlow()

    //new playlist Dialog State
    private val _newPlaylistDialogState = MutableStateFlow<Boolean>(false)
    val newPlaylistDialogState = _newPlaylistDialogState.asStateFlow()

    //rename playlist Dialog State
    private val _renamePlaylistDialogState = MutableStateFlow<Boolean>(false)
    val renamePlaylistDialogState = _renamePlaylistDialogState.asStateFlow()

    private val _currentPlaylist = MutableStateFlow<Playlist?>(null)
    val currentPlaylist = _currentPlaylist.asStateFlow()

    // Playlist-related states
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists = _playlists.asStateFlow()

    init {
        loadFavoriteSongs()
        loadAllPlaylists()
    }

    // Function to show the player sheet
    fun showPlaylistSheet() {
        _showPlaylistSheet.value = true
    }

    // Function to hide the player sheet
    fun hidePlaylistSheet() {
        _showPlaylistSheet.value = false
        _songForPlaylistSheet.value = null
    }

    fun setSongToPlaylistSheet(song: Song) {
        _songForPlaylistSheet.value = song
    }


    // Dialog Controls
    fun showNewPlaylistDialog() {
        _newPlaylistDialogState.value = true
    }

    fun hideNewPlaylistDialog() {
        _newPlaylistDialogState.value = false
    }

    // Dialog Controls
    fun showRenamePlaylistDialog() {
        _renamePlaylistDialogState.value = true
    }

    fun hideRenamePlaylistDialog() {
        _renamePlaylistDialogState.value = false
        _currentPlaylist.value = null
    }

    fun setCurrentPlaylist(playlist: Playlist) {
        _currentPlaylist.value = playlist
    }

    fun toggleFavoriteSong(song: Song) {
        viewModelScope.launch {
            val isCurrentlyFavorite = musicRepository.isSongFavorite(song.id)
            if (isCurrentlyFavorite) {
                musicRepository.removeFavoriteSong(song)
            } else {
                musicRepository.addFavoriteSong(song)
            }
        }
    }

    fun loadFavoriteSongs() {
        viewModelScope.launch {
            _isLoading.value = true
            musicRepository.getAllFavoriteSongs()
                .catch { e ->
                    _errorMessage.value = "Error loading favorites: ${e.message}"
                }
                .collectLatest { favoriteSongs ->
                    // Filter out songs that don't exist on device
                    val validSongs = favoriteSongs.filter { song ->
                        val fileExists = File(song.path).exists()
                        if (!fileExists) {
                            // Remove non-existent songs from favorites
                            viewModelScope.launch {
                                musicRepository.removeFavoriteSong(song)
                            }
                        }
                        fileExists
                    }
                    _favSongs.value = validSongs
                    _isLoading.value = false
                }
        }
    }

    suspend fun isSongFavorite(songId: Long): Boolean {
        return musicRepository.isSongFavorite(songId)
    }

    fun isSongFavoriteAsFlow(songId: Long) {
        viewModelScope.launch {
            musicRepository.isSongFavoriteAsFlow(songId).collectLatest {
                _isFav.value = it
            }
        }
    }

    // Playlist-related functions
    suspend fun insertPlaylist(playlist: Playlist): Long {
        return musicRepository.insertPlaylist(playlist)
    }

    fun createNewPlaylist(name: String) {
        viewModelScope.launch {
            val newPlaylist = Playlist(
                id = 0, // Auto-generated by Room
                name = name,
                createdAt = System.currentTimeMillis(),
                songs = emptyList()
            )
            insertPlaylist(newPlaylist)
        }
    }

    private fun loadAllPlaylists() {
        viewModelScope.launch {
            musicRepository.getAllPlaylists()
                .catch { e ->
                    _errorMessage.value = "Error loading playlists: ${e.message}"
                }
                .collectLatest { allPlaylists ->
                    _playlists.value = allPlaylists
                }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            musicRepository.deletePlaylist(playlist)
        }
    }

    fun addSongToPlaylist(playlistId: Long, song: Song) {
        viewModelScope.launch {
            musicRepository.addSongToPlaylist(playlistId, song)
        }
    }

    fun loadPlaylistWithSongs(playlistId: Long) {
        viewModelScope.launch {
            musicRepository.getSongsForPlaylist(playlistId)
                .catch { e ->
                    _errorMessage.value = "Error loading playlist songs: ${e.message}"
                }
                .collectLatest { loadedPlaylistWithSongs ->
//                    _currentPlaylist.value = playlist
                    val updatedList = _playlists.value.map { playlist ->
                        if (playlist.id == playlistId) {
                            playlist.copy(songs = loadedPlaylistWithSongs.songs)
                        } else playlist
                    }
                    _playlists.value = updatedList
                }
        }
    }

    fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        viewModelScope.launch {
            musicRepository.removeSongFromPlaylist(playlistId, songId)
        }
    }

    fun renamePlaylist(playlistId: Long, newName: String) {
        viewModelScope.launch {
            musicRepository.updatePlaylistName(playlistId, newName)
        }
    }
}

// fun loadPlaylistWithSongs(playlistId: Long) {
//        viewModelScope.launch {
//            musicRepository.getSongsForPlaylist(playlistId)
//                .catch { e ->
//                    _errorMessage.value = "Error loading playlist songs: ${e.message}"
//                }
//                .collectLatest { loadedPlaylistWithSongs ->
////                    _currentPlaylist.value = playlist
//                    val updatedList = _playlists.value.map { playlist ->
//                        if (playlist.id == playlistId) {
//                            playlist.copy(songs = loadedPlaylistWithSongs.songs)
//                        } else playlist
//                    }
//                    _playlists.value = updatedList
//                }
//        }
//    }


//    val favSongs = musicRepository.getFavoriteSongsPaginated().cachedIn(viewModelScope)
//    private fun updateLocalFavoriteState(songId: Long, isFavorite: Boolean) {
//        _favSongs.value = _favSongs.value.map { song ->
//            if (song.id == songId) {
//                song.copy(isFavorite = isFavorite) // Assuming Song has isFavorite property
//            } else {
//                song
//            }
//        }
//    }