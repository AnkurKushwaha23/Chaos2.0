package com.ankurkushwaha.chaos20.presentation.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BottomNavViewModel : ViewModel() {
    // Current selected screen
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen

    // Update the current screen
    fun updateCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }
}