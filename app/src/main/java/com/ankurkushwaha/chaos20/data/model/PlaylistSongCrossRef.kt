package com.ankurkushwaha.chaos20.data.model

import androidx.room.Entity

@Entity(
    tableName = "song_playlist_cross_ref",
    primaryKeys = ["playlistId", "id"]
)
data class PlaylistSongCrossRef(
    val playlistId: Long, // References Playlist id
    val id: Long      // References Song id
)