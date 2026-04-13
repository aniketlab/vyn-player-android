package com.vyn.player

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.vyn.player.data.local.MediaStoreDataSource
import com.vyn.player.data.repository.MusicRepository
import com.vyn.player.domain.usecase.GetSongsUseCase
import com.vyn.player.ui.components.MiniPlayer
import com.vyn.player.ui.navigation.Destinations
import com.vyn.player.ui.navigation.VynNavGraph
import com.vyn.player.ui.onboarding.OnboardingViewModel
import com.vyn.player.ui.screens.home.HomeViewModel
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

                    val homeViewModel = ViewModelProvider(
                        this,
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                val mediaStoreDataSource = MediaStoreDataSource(applicationContext)
                                val musicRepository = MusicRepository(mediaStoreDataSource)
                                val getSongsUseCase = GetSongsUseCase(musicRepository)
                                return HomeViewModel(getSongsUseCase) as T
                            }
                        },
                    )[HomeViewModel::class.java]

                    val onboardingViewModel = ViewModelProvider(
                        this,
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return OnboardingViewModel(
                                    sharedPreferences = applicationContext.getSharedPreferences(
                                        ONBOARDING_PREFS,
                                        Context.MODE_PRIVATE,
                                    ),
                                ) as T
                            }
                        },
                    )[OnboardingViewModel::class.java]

                    val startDestination = if (onboardingViewModel.state.value.isCompleted) {
                        Destinations.HOME
                    } else {
                        Destinations.ONBOARDING
                    }

                    Log.d("ONBOARDING_FLOW", "Start destination=$startDestination")

                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            VynNavGraph(
                                modifier = Modifier.fillMaxSize(),
                                navController = navController,
                                onboardingViewModel = onboardingViewModel,
                                homeViewModel = homeViewModel,
                                playerViewModel = playerViewModel,
                                startDestination = startDestination,
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

    companion object {
        private const val ONBOARDING_PREFS = "onboarding_prefs"
    }
}
