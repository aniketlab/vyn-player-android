package com.vyn.player.data.model

data class Song(
    val id: Long,
    val title: String,
    val artistName: String,
    val albumName: String,
    val uri: String,
    val durationMillis: Long,
)
