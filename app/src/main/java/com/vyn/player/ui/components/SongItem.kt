package com.vyn.player.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vyn.player.data.model.Song

@Composable
fun SongItem(
    song: Song,
    onClick: (Song) -> Unit = {},
) {
    Text(text = song.title)
}