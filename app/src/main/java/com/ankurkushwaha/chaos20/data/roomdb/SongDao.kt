package com.ankurkushwaha.chaos20.data.roomdb

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.ankurkushwaha.chaos20.data.model.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    // Basic song operations
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): PagingSource<Int, SongEntity>

    @Query("SELECT * FROM songs ORDER BY title ASC LIMIT :limit OFFSET :offset")
    suspend fun getSongsPaged(offset: Int, limit: Int): List<SongEntity>

    @Query("SELECT COUNT(*) FROM songs")
    suspend fun getSongCount(): Int

    @Query("SELECT id FROM songs")
    suspend fun getAllSongIds(): List<Long>

    @Query("SELECT artist FROM songs")
    suspend fun getAllSongArtist(): List<String>

    @Query("DELETE FROM songs WHERE id IN (:songIds)")
    suspend fun deleteSongsByIds(songIds: List<Long>)

    @Query("SELECT * FROM songs WHERE id = :songId")
    suspend fun getSongById(songId: Long): SongEntity?

    @Upsert
    suspend fun insertSong(song: SongEntity)

    @Upsert
    suspend fun insertAll(songs: List<SongEntity>)

    @Update
    suspend fun updateSong(song: SongEntity)

    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()

    @Query("DELETE FROM songs WHERE id = :songId")
    suspend fun deleteSong(songId: Long)

    // Favorite song operations
    @Query("UPDATE songs SET isFavorite = :isFavorite WHERE id = :songId")
    suspend fun updateFavoriteStatus(songId: Long, isFavorite: Boolean)

    @Query("SELECT * FROM songs WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteSongs(): PagingSource<Int, SongEntity>

    @Query("SELECT * FROM songs WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteSongsAsFlow(): Flow<List<SongEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM songs WHERE id = :songId AND isFavorite = 1)")
    suspend fun isSongFavorite(songId: Long): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM songs WHERE id = :songId AND isFavorite = 1)")
    fun isSongFavoriteAsFlow(songId: Long): Flow<Boolean>

    @Query("SELECT COUNT(*) FROM songs WHERE isFavorite = 1")
    suspend fun getFavoriteSongsCount(): Int
}

//// Search functionality
//    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%'")
//    suspend fun searchSongs(query: String): List<SongEntity>
//    @Query("SELECT * FROM songs ORDER BY title ASC")
//    fun getAllSongsAsFlow(): Flow<List<SongEntity>>