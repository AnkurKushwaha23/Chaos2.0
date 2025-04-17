package com.ankurkushwaha.chaos20.data.mapper

import com.ankurkushwaha.chaos20.data.model.FavSongEntity
import com.ankurkushwaha.chaos20.data.model.PlaylistEntity
import com.ankurkushwaha.chaos20.data.model.PlaylistSongCrossRef
import com.ankurkushwaha.chaos20.data.model.PlaylistWithSongs
import com.ankurkushwaha.chaos20.data.model.SongEntity
import com.ankurkushwaha.chaos20.domain.model.Playlist
import com.ankurkushwaha.chaos20.domain.model.PlaylistSongCrossRefDomain
import com.ankurkushwaha.chaos20.domain.model.Song

fun SongEntity.toSong(): Song {
    return Song(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        path = path,
        imageUri = albumArtUri,
        isFavorite = isFavorite
    )
}

fun Song.toSongEntity(): SongEntity {
    return SongEntity(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        path = path,
        albumArtUri = imageUri,
        isFavorite = isFavorite
    )
}


fun PlaylistWithSongs.toPlaylist(): Playlist {
    return Playlist(
        id = playlist.playlistId,
        name = playlist.name,
        createdAt = playlist.createdAt,
        songs = songs.map { it.toSong() }
    )
}

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        playlistId = id,
        name = name,
        createdAt = createdAt
    )
}

// Function to create PlaylistSongCrossRef from a Playlist and its Songs
fun Playlist.createCrossRefs(): List<PlaylistSongCrossRef> {
    return songs.map { song ->
        PlaylistSongCrossRef(
            playlistId = id,
            id = song.id
        )
    }
}

/**
 * Extension function to convert from database entity to domain model
 */
fun PlaylistSongCrossRef.toDomain(): PlaylistSongCrossRefDomain {
    return PlaylistSongCrossRefDomain(
        playlistId = playlistId,
        id = id
    )
}

/**
 * Extension function to convert from domain model to database entity
 */
fun PlaylistSongCrossRefDomain.toEntity(): PlaylistSongCrossRef {
    return PlaylistSongCrossRef(
        playlistId = playlistId,
        id = id
    )
}

/**
 * Extension function to convert a Song to a FavSongEntity
 */
fun Song.toFavSongEntity(): FavSongEntity {
    return FavSongEntity(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        path = path,
        imageUri = imageUri
    )
}

/**
 * Extension function to convert a FavSongEntity to a Song
 * Note that FavSongEntity doesn't contain isFavorite information,
 * but since it's coming from favorites table, we set isFavorite to true
 */
fun FavSongEntity.toSong(): Song {
    return Song(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        path = path,
        imageUri = imageUri,
        isFavorite = true // Always true as it comes from favorites table
    )
}

/**
 * Extension function to create a copy of a Song with updated isFavorite status
 */
fun Song.withFavoriteStatus(isFavorite: Boolean): Song {
    return this.copy(isFavorite = isFavorite)
}