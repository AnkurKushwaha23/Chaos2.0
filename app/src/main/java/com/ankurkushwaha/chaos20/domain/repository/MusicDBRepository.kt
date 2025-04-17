package com.ankurkushwaha.chaos20.domain.repository

import com.ankurkushwaha.chaos20.data.model.PlaylistSongCrossRef
import com.ankurkushwaha.chaos20.domain.model.Playlist
import com.ankurkushwaha.chaos20.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicDBRepository {
    suspend fun addFavoriteSong(songId: Long)
    suspend fun removeFavoriteSong(songId: Long)
    suspend fun isSongFavorite(songId: Long): Boolean
    fun isSongFavoriteAsFlow(songId: Long): Flow<Boolean>
    fun getAllFavoriteSongs(): Flow<List<Song>>

    suspend fun insertPlaylist(playlist: Playlist): Long
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun insertSongToPlaylist(crossRef: PlaylistSongCrossRef)
    fun getSongsForPlaylist(playlistId: Long): Flow<Playlist>
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)

//    suspend fun getAllSongsFromPlaylistDao(): List<Song>
    suspend fun getAllPlaylistSongCrossRefs(): List<PlaylistSongCrossRef>
    suspend fun isSongExists(songId: Long): Boolean
}