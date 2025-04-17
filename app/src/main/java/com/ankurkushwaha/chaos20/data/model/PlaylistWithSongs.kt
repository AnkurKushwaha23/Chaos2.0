package com.ankurkushwaha.chaos20.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,  // Embedded Playlist object
    @Relation(
        parentColumn = "playlistId",           // References the primary key of Playlist
        entityColumn = "id",           // References the primary key of Song
        associateBy = Junction(PlaylistSongCrossRef::class) // Junction to link Playlist and Song
    )
    val songs: List<SongEntity> // List of Songs in the Playlist
)