package com.vyn.player.ui.screens.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyn.player.data.model.Song
import com.vyn.player.ui.screens.player.PlayerUiEvent
import com.vyn.player.ui.screens.player.PlayerViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    playerViewModel: PlayerViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val songs = state.songs.distinctBy { it.uri }
    val context = LocalContext.current
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.loadSongs()
        }
    }
    val hasPermission = ContextCompat.checkSelfPermission(
        context,
        permission,
    ) == PackageManager.PERMISSION_GRANTED

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(permission)
        } else {
            viewModel.loadSongs()
        }
    }

    if (!hasPermission) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = "Permission required to load songs")
                Button(onClick = {
                    launcher.launch(permission)
                }) {
                    Text(text = "Grant Permission")
                }
                Button(onClick = { viewModel.loadSongs() }) {
                    Text(text = "Reload Songs")
                }
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        Button(
            onClick = { viewModel.loadSongs() },
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(text = "Reload Songs")
        }

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
                        playerViewModel.onEvent(
                            PlayerUiEvent.PlaySongs(
                                songs = songs,
                                startIndex = index,
                            ),
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun SongItem(song: Song, onClick: () -> Unit) {
    val cleanTitle = song.title.replace(Regex("^\\d+\\s*"), "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cleanTitle,
                    style = MaterialTheme.typography.bodyLarge,
                )

                if (song.artistName.isNotBlank() && song.artistName != song.title) {
                    Text(
                        text = song.artistName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                    )
                }
            }
        }
    }
}
