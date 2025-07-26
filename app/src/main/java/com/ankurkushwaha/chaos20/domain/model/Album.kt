package com.ankurkushwaha.chaos20.domain.model

/**
 * @author Ankur Kushwaha;
 * created at 22 July 2025 16:15
 */


data class Album(
    val id: Long,
    val name: String,
    val artist: String,
    val numberOfSongs: Int,
    val albumArtUri: String
)

