package com.ankurkushwaha.chaos20.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.ankurkushwaha.chaos20.data.mapper.toDomain
import com.ankurkushwaha.chaos20.data.mapper.toFavSongEntity
import com.ankurkushwaha.chaos20.data.mapper.toPlaylist
import com.ankurkushwaha.chaos20.data.mapper.toPlaylistEntity
import com.ankurkushwaha.chaos20.data.mapper.toSong
import com.ankurkushwaha.chaos20.data.mapper.toSongEntity
import com.ankurkushwaha.chaos20.data.model.PlaylistSongCrossRef
import com.ankurkushwaha.chaos20.data.roomdb.FavSongDao
import com.ankurkushwaha.chaos20.data.roomdb.PlaylistDao
import com.ankurkushwaha.chaos20.domain.model.Playlist
import com.ankurkushwaha.chaos20.domain.model.PlaylistSongCrossRefDomain
import com.ankurkushwaha.chaos20.domain.model.Song
import com.ankurkushwaha.chaos20.domain.repository.MusicRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val favSongDao: FavSongDao,
    private val playlistDao: PlaylistDao
) : MusicRepository {

    //playlist
    override suspend fun insertPlaylist(playlist: Playlist): Long {
        return playlistDao.insertPlaylist(playlist.toPlaylistEntity())
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { playlistEntities ->
            playlistEntities.map { playlistEntity ->
                Playlist(
                    id = playlistEntity.playlistId,
                    name = playlistEntity.name,
                    createdAt = playlistEntity.createdAt,
                    songs = emptyList()
                )
            }
        }
    }

    override fun getSongsForPlaylist(playlistId: Long): Flow<Playlist> {
        return playlistDao.getSongsForPlaylist(playlistId).map {
            it.toPlaylist()
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun updatePlaylistName(playlistId: Long, newName: String) {
        playlistDao.updatePlaylistName(playlistId,newName)
    }

    override suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        playlistDao.removeSongFromPlaylist(playlistId, songId)
    }

    override suspend fun getAllPlaylistCrossRef(): List<PlaylistSongCrossRefDomain> {
        return playlistDao.getAllPlaylistSongCrossRefs().map { it.toDomain() }
    }

    override suspend fun addSongToPlaylist(
        playlistId: Long,
        song: Song
    ) {
        addSong(song)
        addSongToPlaylist(playlistId, song.id)
    }

    private suspend fun addSongToPlaylist(playlistId: Long, songId: Long) {
        val crossRef = PlaylistSongCrossRef(playlistId = playlistId, id = songId)
        playlistDao.insertSongToPlaylist(crossRef)
    }

    private suspend fun addSong(song: Song) {
        val entity = song.toSongEntity()
        playlistDao.insertSongs(entity)
    }

    //    override suspend fun isSongExists(songId: Long): Boolean {
//        return playlistDao.isSongExists(songId)
//    }

    //favorite
    override suspend fun addFavoriteSong(song: Song) {
        val favSong = song.toFavSongEntity()
        favSongDao.insertFavoriteSong(favSong)
    }

    override suspend fun removeFavoriteSong(song: Song) {
        val favSong = song.toFavSongEntity()
        favSongDao.deleteFavoriteSong(favSong)
    }

    override fun getAllFavoriteSongs(): Flow<List<Song>> {
        return favSongDao.getAllFavoriteSongs()
            .map { favSongs -> favSongs.map { it.toSong() } }
    }

    override fun getAllFavoriteSongsId(): Flow<List<Long>> {
        return favSongDao.getAllFavoriteSongsId()
            .map { favSongs -> favSongs.map { it } }
    }

    override fun getFavoriteSongCount(): Flow<Int> {
        return favSongDao.totalCount()
    }

    override suspend fun isSongFavorite(songId: Long): Boolean {
        return favSongDao.isFavorite(songId = songId)
    }

    override fun isSongFavoriteAsFlow(songId: Long): Flow<Boolean> {
        return favSongDao.isFavoriteAsFlow(songId)
    }

    override suspend fun getArtistName(): List<String> = withContext(Dispatchers.IO) {
        // Fetch songs directly from MediaStore or use cached list
        val songs = fetchSongsFromMediaStore()

        // Extract artists from songs
        val individualArtists = songs
            .mapNotNull { it.artist }
            .filter { it != "<unknown>" }
            .flatMap { it.split(",") } // Split multiple artists
            .map { it.trim() }         // Remove extra spaces
            .filter { it.isNotEmpty() } // Remove empty strings

        // Count and sort by frequency
        val artistCounts = individualArtists
            .groupingBy { it }
            .eachCount()

        // Return top 20 artists by song count
        artistCounts
            .entries
            .sortedByDescending { it.value }
            .map { it.key }
            .take(20)
    }

    override suspend fun fetchSongsFromMediaStore(): List<Song> = withContext(Dispatchers.IO) {
        val songs = mutableListOf<Song>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )

        // all music files larger than 30 seconds and are not recordings
        val selection =
            "${MediaStore.Audio.Media.IS_MUSIC} <> 0 AND ${MediaStore.Audio.Media.DURATION} > 30000"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn).uppercase(Locale.getDefault())

                // Filtering out recordings
                if (title.startsWith("AUD") || title.contains("RECORD") || title.startsWith("PTT")) continue

                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val duration = cursor.getLong(durationColumn)
                val path = cursor.getString(dataColumn)
                val albumId = cursor.getLong(albumIdColumn)

                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                ).toString()

                val song = Song(id, title, artist, album, duration, path, albumArtUri)
                songs.add(song)
            }
        }

        Log.d("MusicRepository", "Fetched ${songs.size} songs")
        songs
    }
}

//    private val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
//        override fun onChange(selfChange: Boolean) {
//            CoroutineScope(Dispatchers.IO).launch {
//                refreshSongs()
//            }
//        }
//    }
//
//    override fun registerContentObserver() {
//        context.contentResolver.registerContentObserver(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            true,
//            contentObserver
//        )
//    }
//
//    override fun unregisterContentObserver() {
//        context.contentResolver.unregisterContentObserver(contentObserver)
//    }
//
//    override suspend fun refreshSongsIncrementally() {
//        val deviceSongs = fetchSongsFromMediaStore()
//        val dbSongs = songDao.getAllSongIds()
//
//        val songsToAdd = deviceSongs.filter { it.id !in dbSongs }
//        val songsToRemove = dbSongs.filter { id -> deviceSongs.none { it.id == id } }
//
//        if (songsToAdd.isNotEmpty()) {
//            songDao.insertAll(songsToAdd.map { it.toSongEntity() })
//        }
//        if (songsToRemove.isNotEmpty()) {
//            songDao.deleteSongsByIds(songsToRemove)
//        }
//    }

//    override fun getSongsPaginated(): Flow<PagingData<Song>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 20,
//                prefetchDistance = 5,
//                enablePlaceholders = true
//            )
//        ) {
//            songDao.getAllSongs()
//        }.flow.map { pagingData ->
//            pagingData.map { songEntity ->
//                songEntity.toSong()
//            }
//        }
//    }

//    override fun getFavoriteSongsPaginated(): Flow<PagingData<Song>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 20,
//                prefetchDistance = 5,
//                enablePlaceholders = true
//            )
//        ) {
//            songDao.getFavoriteSongs()
//        }.flow.map { pagingData ->
//            pagingData.map { songEntity ->
//                songEntity.toSong()
//            }
//        }
//    }

//    override fun isSongFavoriteAsFlow(songId: Long): Flow<Boolean> {
//        return songDao.isSongFavoriteAsFlow(songId = songId)
//    }
//    override suspend fun getAllSongsFromPlaylistDao(): List<Song> {
//        return playlistDao.
//    }
//    override suspend fun getAllSongsFromPlaylistDao(): List<Song> {
//    return playlistDao.getAllSongs().map { it.toSong() }
//}

//    override suspend fun refreshSongs() {
//        val songs = fetchSongsFromMediaStore()
//        val songEntities = songs.map { it.toSongEntity() }
//        songDao.insertAll(songEntities)
//    }