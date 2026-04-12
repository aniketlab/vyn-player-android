package com.vyn.player.ui.screens.home

import com.vyn.player.data.model.Song

data class HomeState(
    val songs: List<Song> = emptyList(),
    val hasPermission: Boolean = false,
)
