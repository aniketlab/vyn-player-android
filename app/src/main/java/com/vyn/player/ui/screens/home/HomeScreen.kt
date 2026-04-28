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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import com.vyn.player.ui.components.AppButton
import com.vyn.player.ui.components.SongItem
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    playerViewModel: PlayerViewModel,
    onScrollDirectionChanged: (Boolean) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val songs = state.songs.distinctBy { it.uri }
    val listState = rememberLazyListState()
    val lastDirection = remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadSongsIfNeeded()
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }
            .pairWithPrevious()
            .map { (prev, curr) ->
                if (prev == null) return@map null

                val indexChanged = curr.first != prev.first
                val delta = curr.second - prev.second
                val direction = when {
                    indexChanged -> curr.first < prev.first
                    delta < -MIN_SCROLL_DISTANCE -> true
                    delta > MIN_SCROLL_DISTANCE -> false
                    else -> null
                }

                Log.d("SCROLL_DEBUG", "delta=$delta direction=$direction index=${curr.first}")

                if (direction != null && direction != lastDirection.value) {
                    lastDirection.value = direction
                    direction
                } else {
                    null
                }
            }
            .filterNotNull()
            .distinctUntilChanged()
            .collect {
                onScrollDirectionChanged(it)
            }
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
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(
                items = songs,
                key = { _, song -> song.id },
            ) { _, song ->
                SongItem(
                    song = song,
                    onClick = {
                        val actualIndex = songs.indexOfFirst { queuedSong -> queuedSong.id == song.id }

                        Log.d(
                            "PLAYER_DEBUG",
                            "Clicked=${song.title}, resolvedIndex=$actualIndex, total=${songs.size}",
                        )

                        if (actualIndex != -1) {
                            playerViewModel.playSongs(
                                songs = songs,
                                startIndex = actualIndex,
                            )
                        }
                    },
                )
            }
        }
    }
}

private const val MIN_SCROLL_DISTANCE = 40

private fun <T> Flow<T>.pairWithPrevious(): Flow<Pair<T?, T>> = flow {
    var previous: T? = null
    collect { value ->
        emit(previous to value)
        previous = value
    }
}
