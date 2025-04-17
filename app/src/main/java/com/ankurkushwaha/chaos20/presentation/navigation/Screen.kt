package com.ankurkushwaha.chaos20.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Player : Screen()

    @Serializable
    data object Search : Screen()

    @Serializable
    data object Favorite : Screen()

    @Serializable
    data object Playlist : Screen()
}