package com.vyn.player.data.repository

import com.vyn.player.data.local.MediaStoreDataSource
import com.vyn.player.data.model.Song

class MusicRepository(
    private val mediaStoreDataSource: MediaStoreDataSource,
) {
    suspend fun getSongs(): List<Song> = mediaStoreDataSource.getSongs()
}
