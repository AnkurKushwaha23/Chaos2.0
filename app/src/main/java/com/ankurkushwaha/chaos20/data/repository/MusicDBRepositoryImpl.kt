package com.ankurkushwaha.chaos20.data.repository

import com.ankurkushwaha.chaos20.data.mapper.toPlaylist
import com.ankurkushwaha.chaos20.data.mapper.toPlaylistEntity
import com.ankurkushwaha.chaos20.data.mapper.toSong
import com.ankurkushwaha.chaos20.data.model.PlaylistSongCrossRef
import com.ankurkushwaha.chaos20.data.roomdb.PlaylistDao
import com.ankurkushwaha.chaos20.data.roomdb.SongDao
import com.ankurkushwaha.chaos20.domain.model.Playlist
import com.ankurkushwaha.chaos20.domain.model.Song
import com.ankurkushwaha.chaos20.domain.repository.MusicDBRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MusicDBRepositoryImpl @Inject constructor(
    private val songDao: SongDao,
    private val playlistDao: PlaylistDao
) : MusicDBRepository {
    override suspend fun addFavoriteSong(songId: Long) {
        songDao.updateFavoriteStatus(songId = songId, isFavorite = true)
    }

    override suspend fun removeFavoriteSong(songId: Long) {
        songDao.updateFavoriteStatus(songId = songId, isFavorite = false)
    }

    override suspend fun isSongFavorite(songId: Long): Boolean {
        return songDao.isSongFavorite(songId = songId)
    }

    override fun isSongFavoriteAsFlow(songId: Long): Flow<Boolean> {
        return songDao.isSongFavoriteAsFlow(songId = songId)
    }

    override fun getAllFavoriteSongs(): Flow<List<Song>> {
        return songDao.getFavoriteSongsAsFlow()
            .map { songs -> songs.map { it.toSong() } }
    }

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
                    songs = emptyList() // Initially empty list of songs
                )
            }
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun insertSongToPlaylist(crossRef: PlaylistSongCrossRef) {
        playlistDao.insertSongToPlaylist(crossRef)
    }

    override fun getSongsForPlaylist(playlistId: Long): Flow<Playlist> {
        return playlistDao.getSongsForPlaylist(playlistId).map {
            it.toPlaylist()
        }
    }

    override suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        playlistDao.removeSongFromPlaylist(playlistId, songId)
    }

//    override suspend fun getAllSongsFromPlaylistDao(): List<Song> {
//        return playlistDao.
//    }
//    override suspend fun getAllSongsFromPlaylistDao(): List<Song> {
//    return playlistDao.getAllSongs().map { it.toSong() }
//}

    override suspend fun getAllPlaylistSongCrossRefs(): List<PlaylistSongCrossRef> {
        return playlistDao.getAllPlaylistSongCrossRefs()
    }

    override suspend fun isSongExists(songId: Long): Boolean {
        return playlistDao.isSongExists(songId)
    }
}