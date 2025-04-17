package com.ankurkushwaha.chaos20.presentation.home_screen

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankurkushwaha.chaos20.domain.model.Song
import com.ankurkushwaha.chaos20.services.MusicService
import com.ankurkushwaha.chaos20.services.PLAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor() : ViewModel() {
    //music service
    private val _musicService = MutableStateFlow<MusicService?>(null)
    val musicService = _musicService.asStateFlow()

    //music service binder
    private val _musicBinder = MutableStateFlow<IBinder?>(null)
    val musicBinder = _musicBinder.asStateFlow()

    // Add this for controlling the player sheet visibility
    private val _showPlayerSheet = MutableStateFlow(false)
    val showPlayerSheet = _showPlayerSheet.asStateFlow()

    private val _showChaosSheet = MutableStateFlow(false)
    val showChaosSheet = _showChaosSheet.asStateFlow()

    private val _showSleepDialog = MutableStateFlow(false)
    val showSleepDialog = _showSleepDialog.asStateFlow()

    // Add this for controlling the mini player visibility
    private val _showMiniPlayer = MutableStateFlow(false)
    val showMiniPlayer = _showMiniPlayer.asStateFlow()

    // Dialog State
    private val _detailDialogState = MutableStateFlow<Song?>(null)
    val detailDialogState = _detailDialogState.asStateFlow()

    // Function to show the player sheet
    fun showPlayer() {
        _showPlayerSheet.value = true
    }

    // Function to hide the player sheet
    fun hidePlayer() {
        _showPlayerSheet.value = false
    }

    fun showChaosBottomSheet() {
        _showChaosSheet.value = true
    }

    // Function to hide the player sheet
    fun hideChaosBottomSheet() {
        _showChaosSheet.value = false
    }

    fun showSleepDialog() {
        _showSleepDialog.value = true
    }

    // Function to hide the player sheet
    fun hideSleepDialog() {
        _showSleepDialog.value = false
    }

    // Dialog Controls
    fun showSongDetail(song: Song) {
        _detailDialogState.value = song
    }

    fun hideSongDetail() {
        _detailDialogState.value = null
    }

    // Function to toggle the player sheet visibility
    fun togglePlayer() {
        _showPlayerSheet.value = !_showPlayerSheet.value
    }

    fun setMusicService(service: MusicService?) {
        _musicService.value = service
        if (service != null) {
            observeServiceValues()
        }
    }

    fun setMusicBinder(binder: IBinder?) {
        _musicBinder.value = binder
    }

    // Method to play a song through the service
    fun playSong(song: Song, context: Context) {
        val service = _musicService.value

        if (service != null) {
            // If service is already bound, use it directly
            service.play(song)
        } else {
            // If service isn't bound yet, start it with the song
            val intent = Intent(context, MusicService::class.java).apply {
                putExtra("currentSong", song)
                action = PLAY
            }
            context.startService(intent)
        }
    }

    fun setMusicList(songs: List<Song>) {
        (musicBinder.value as MusicService.MusicBinder).setMusicList(songs)
    }

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _isShuffleOn = MutableStateFlow(false)
    val isShuffleOn = _isShuffleOn.asStateFlow()

    private val _isRepeatSongOn = MutableStateFlow(false)
    val isRepeatSongOn = _isRepeatSongOn.asStateFlow()

    private val _currentDuration = MutableStateFlow(0)
    val currentDuration = _currentDuration.asStateFlow()

    private val _maxDuration = MutableStateFlow(0)
    val maxDuration = _maxDuration.asStateFlow()

    // Add this function to observe all values from the service
    private fun observeServiceValues() {
        viewModelScope.launch {
            _musicService.value?.let { service ->
                launch {
                    service.getCurrentSong().collect { song ->
                        _currentSong.value = song
                    }
                }

                launch {
                    service.isPlaying().collect { playing ->
                        _isPlaying.value = playing
                    }
                }

                launch {
                    service.showMiniPlayer().collect { show ->
                        _showMiniPlayer.emit(show)
                    }
                }

                launch {
                    service.isShuffle().collect { shuffle ->
                        _isShuffleOn.value = shuffle
                    }
                }

                launch {
                    service.isRepeat().collect { repeat ->
                        _isRepeatSongOn.value = repeat
                    }
                }

                launch {
                    service.currentDuration().collect { duration ->
                        _currentDuration.value = duration
                    }
                }

                launch {
                    service.maxDuration().collect { max ->
                        _maxDuration.value = max
                    }
                }
            }
        }
    }

    fun playPause() {
        val service = _musicService.value
        service?.playPause()
    }

    fun playNext() {
        val service = _musicService.value
        service?.nextSong()
    }

    fun playPrevious() {
        val service = _musicService.value
        service?.previousSong()
    }

    fun toggleShuffle() {
        val service = _musicService.value
        service?.toggleShuffle()
    }

    fun toggleRepeat() {
        val service = _musicService.value
        service?.toggleRepeat()
    }

    fun seekTo(position: Int) {
        val service = _musicService.value
        service?.seekTo(position)
    }

    fun queueNextSong(song: Song) {
        val service = _musicService.value
        service?.queueNextSong(song)
    }
}