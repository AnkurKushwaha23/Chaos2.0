package com.ankurkushwaha.chaos20.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ankurkushwaha.chaos20.data.model.FavSongEntity
import com.ankurkushwaha.chaos20.data.model.PlaylistEntity
import com.ankurkushwaha.chaos20.data.model.PlaylistSongCrossRef
import com.ankurkushwaha.chaos20.data.model.SongEntity

@Database(
    entities = [FavSongEntity::class, SongEntity::class, PlaylistEntity::class, PlaylistSongCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class SongDatabase : RoomDatabase() {
    abstract fun favSongDao(): FavSongDao
    abstract fun playlistDao(): PlaylistDao
}