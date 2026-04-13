package com.vyn.player.data.repository

import android.util.Log
import com.vyn.player.data.local.MediaStoreDataSource
import com.vyn.player.data.model.Song

class SongRepository(
    private val dataSource: MediaStoreDataSource,
) {
    private var cachedSongs: List<Song>? = null

    suspend fun getSongs(): List<Song> {
        if (cachedSongs != null) {
            Log.d("REPO_FLOW", "Returning cached songs: ${cachedSongs!!.size}")
            return cachedSongs!!
        }

        Log.d("REPO_FLOW", "Fetching songs from MediaStore")
        val songs = dataSource.getSongs()
        cachedSongs = songs
        return songs
    }

    fun clearCache() {
        cachedSongs = null
        Log.d("REPO_FLOW", "Cache cleared")
    }
}
