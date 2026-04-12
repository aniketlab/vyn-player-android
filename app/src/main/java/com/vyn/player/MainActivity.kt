package com.vyn.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.vyn.player.core.player.PlayerController
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.navigation.Destinations
import com.vyn.player.ui.navigation.VynNavGraph
import com.vyn.player.ui.screens.player.PlayerViewModel
import com.vyn.player.ui.theme.VynPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VynPlayerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val playerController = remember(applicationContext) {
                        PlayerController(applicationContext)
                    }

                    DisposableEffect(Unit) {
                        onDispose {
                            playerController.release()
                        }
                    }

                    val playerViewModel = ViewModelProvider(
                        this,
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return PlayerViewModel(
                                    playerController = playerController,
                                ) as T
                            }
                        },
                    )[PlayerViewModel::class.java]

                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            VynNavGraph(
                                modifier = Modifier.fillMaxSize(),
                                navController = navController,
                                playerViewModel = playerViewModel,
                            )
                        }

                        MiniPlayer(
                            viewModel = playerViewModel,
                            onClick = {
                                navController.navigate(Destinations.PLAYER) {
                                    launchSingleTop = true
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
