package com.ankurkushwaha.chaos20.data.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ankurkushwaha.chaos20.data.model.FavSongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavSongDao {
    @Upsert
    suspend fun insertFavoriteSong(song: FavSongEntity)

    @Delete
    suspend fun deleteFavoriteSong(song: FavSongEntity)

    @Query("SELECT * FROM favorite_songs")
    fun getAllFavoriteSongs(): Flow<List<FavSongEntity>>

    @Query("SELECT id FROM favorite_songs")
    fun getAllFavoriteSongsId(): Flow<List<Long>>

    @Query("SELECT COUNT(*) FROM favorite_songs")
    fun totalCount(): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_songs WHERE id = :songId LIMIT 1)")
    suspend fun isFavorite(songId: Long): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_songs WHERE id = :songId LIMIT 1)")
    fun isFavoriteAsFlow(songId: Long): Flow<Boolean>
}