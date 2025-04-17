package com.ankurkushwaha.chaos20.data.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ankurkushwaha.chaos20.data.model.PlaylistEntity
import com.ankurkushwaha.chaos20.data.model.PlaylistSongCrossRef
import com.ankurkushwaha.chaos20.data.model.PlaylistWithSongs
import com.ankurkushwaha.chaos20.data.model.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM songs")
    suspend fun getAllSongs(): List<SongEntity>

    @Upsert
    suspend fun insertSongs(songs: SongEntity)

    @Upsert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Upsert
    suspend fun insertSongToPlaylist(crossRef: PlaylistSongCrossRef)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists")
    @Transaction // To ensure relationships are fetched atomically
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>

    // Corrected function to fetch songs for a specific playlist
    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :playlistId")
    fun getSongsForPlaylist(playlistId: Long): Flow<PlaylistWithSongs>

    @Query("SELECT * FROM song_playlist_cross_ref")
    suspend fun getAllPlaylistSongCrossRefs(): List<PlaylistSongCrossRef>

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM song_playlist_cross_ref WHERE playlistId = :playlistId AND id = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM songs WHERE id = :songId)")
    suspend fun isSongExists(songId: Long): Boolean

    // Added: Search songs by title or artist
    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    suspend fun searchSongs(query: String): List<SongEntity>

    // Added: Get count of songs in a specific playlist
    @Query("SELECT COUNT(*) FROM song_playlist_cross_ref WHERE playlistId = :playlistId")
    fun getPlaylistSongCount(playlistId: Long): Flow<Int>

    // Added: Get song IDs for a specific playlist
    @Query("SELECT id FROM song_playlist_cross_ref WHERE playlistId = :playlistId")
    suspend fun getSongIdsForPlaylist(playlistId: Long): List<Long>

    // Added: Check if a song exists in a specific playlist
    @Query("SELECT EXISTS(SELECT 1 FROM song_playlist_cross_ref WHERE playlistId = :playlistId AND id = :songId)")
    suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean

    // Added: Update playlist name
    @Query("UPDATE playlists SET name = :newName WHERE playlistId = :playlistId")
    suspend fun updatePlaylistName(playlistId: Long, newName: String)
}