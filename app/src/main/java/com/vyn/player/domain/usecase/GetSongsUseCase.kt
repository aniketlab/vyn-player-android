package com.vyn.player.domain.usecase

import com.vyn.player.data.model.Song
import com.vyn.player.data.repository.MusicRepository

class GetSongsUseCase(
    private val repository: MusicRepository,
) {
    suspend operator fun invoke(): List<Song> {
        return repository.getSongs()
    }
}
