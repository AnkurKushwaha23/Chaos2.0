package com.ankurkushwaha.chaos20.domain.model

data class PlaylistSongCrossRefDomain(
    val playlistId: Long, // References Playlist id
    val id: Long      // References Song id
)
