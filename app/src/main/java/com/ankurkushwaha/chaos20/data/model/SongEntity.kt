package com.ankurkushwaha.chaos20.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val title: String,
    val artist: String?,
    val album: String,
    val duration: Long,
    val path: String,
    val albumArtUri: String?,
    var isFavorite: Boolean = false
)
