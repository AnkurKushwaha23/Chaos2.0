package com.ankurkushwaha.chaos20.domain.repository

import com.ankurkushwaha.chaos20.domain.model.Playlist
import com.ankurkushwaha.chaos20.domain.model.PlaylistSongCrossRefDomain
import com.ankurkushwaha.chaos20.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun fetchSongsFromMediaStore(): List<Song>
    suspend fun getArtistName(): List<String>

    //favorite
    suspend fun addFavoriteSong(song: Song)
    suspend fun removeFavoriteSong(song: Song)
    suspend fun isSongFavorite(songId: Long): Boolean
    fun isSongFavoriteAsFlow(songId: Long): Flow<Boolean>
    fun getAllFavoriteSongs(): Flow<List<Song>>
    fun getAllFavoriteSongsId(): Flow<List<Long>>
    fun getFavoriteSongCount(): Flow<Int>

    // Insert a new playlist
    suspend fun insertPlaylist(playlist: Playlist): Long

    // Remove a song from a playlist
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)

    // Get all playlists
    fun getAllPlaylists(): Flow<List<Playlist>>

    // Get songs for a specific playlist using Flow
    fun getSongsForPlaylist(playlistId: Long): Flow<Playlist>

    suspend fun getAllPlaylistCrossRef(): List<PlaylistSongCrossRefDomain>

    suspend fun addSongToPlaylist(playlistId: Long, song: Song)

    // Delete a playlist
    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylistName(playlistId: Long, newName: String)
}

//    suspend fun insertPlaylist(playlist: Playlist): Long
//    fun getAllPlaylists(): Flow<List<Playlist>>
//    suspend fun deletePlaylist(playlist: Playlist)
//    suspend fun insertSongToPlaylist(crossRef: PlaylistSongCrossRefDomain)
//    fun getSongsForPlaylist(playlistId: Long): Flow<Playlist>
//    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)
//
//    //    suspend fun getAllSongsFromPlaylistDao(): List<Song>
//    suspend fun getAllPlaylistSongCrossRefs(): List<PlaylistSongCrossRefDomain>
//    suspend fun isSongExists(songId: Long): Boolean
//
    //add song in songs table
//    suspend fun addSong(song: Song)
//
//    // Add a song to a playlist
//    suspend fun addSongToPlaylist(playlistId: Long, songId: Long)
//    // Added: Search for songs by title or artist
//    suspend fun searchSongs(query: String): List<Song>
//
//    // Added: Get playlist song count
//    fun getPlaylistSongCount(playlistId: Long): Flow<Int>
//
//    // Added: Check if a song exists in a playlist
//    suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean
//
//    // Added: Update playlist name
//    suspend fun updatePlaylistName(playlistId: Long, newName: String)
//
//    // Added: Get all songs by artist
//    suspend fun getSongsByArtist(artist: String): List<Song>
//
//    // Added: Get distinct album names
//    suspend fun getAlbumNames(): List<String>
//
//    // Added: Get songs by album
//    suspend fun getSongsByAlbum(album: String): List<Song>
//    suspend fun refreshSongs()
//    fun registerContentObserver()
//    fun unregisterContentObserver()
//    suspend fun refreshSongsIncrementally()
//    fun getSongsPaginated(): Flow<PagingData<Song>>
//    fun getFavoriteSongsPaginated(): Flow<PagingData<Song>>