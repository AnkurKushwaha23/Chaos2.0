package com.ankurkushwaha.chaos20.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long = 0,
    val name: String,
    val createdAt: Long,
    val songs: List<Song> = emptyList()
) : Parcelable
