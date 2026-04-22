package com.vyn.player.ui.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.ui.components.AppButton
import com.vyn.player.ui.components.SongItem
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    playerViewModel: PlayerViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val songs = state.songs.distinctBy { it.uri }

    LaunchedEffect(Unit) {
        viewModel.loadSongsIfNeeded()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 12.dp),
    ) {
        AppButton(
            text = "Reload Songs",
            onClick = { viewModel.reloadSongs() },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (songs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "No songs found")
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(
                items = songs,
                key = { _, song -> song.id },
            ) { index, song ->
                SongItem(
                    song = song,
                    onClick = {
                        Log.d("PLAYER_FLOW", "Clicked: ${song.title} index=$index queueSize=${songs.size}")
                        playerViewModel.playSongs(
                            songs = songs,
                            startIndex = index,
                        )
                    },
                )
            }
        }
    }
}
