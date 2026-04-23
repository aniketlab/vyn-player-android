package com.vyn.player

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vyn.player.core.player.PlayerController
import com.vyn.player.data.local.MediaStoreDataSource
import com.vyn.player.data.repository.SongRepository
import com.vyn.player.ui.navigation.Destinations
import com.vyn.player.ui.navigation.PillNavBar
import com.vyn.player.ui.navigation.PillNavItem
import com.vyn.player.ui.navigation.VynNavGraph
import com.vyn.player.ui.onboarding.OnboardingViewModel
import com.vyn.player.ui.player.PlayerHost
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
                    val mediaStoreDataSource = remember(applicationContext) {
                        MediaStoreDataSource(applicationContext)
                    }
                    val songRepository = remember(applicationContext) {
                        SongRepository(mediaStoreDataSource)
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
                                return HomeViewModel(songRepository) as T
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

                    val bottomNavItems = remember {
                        listOf(
                            PillNavItem(
                                route = Destinations.HOME,
                                label = "Home",
                                icon = Icons.Filled.Home,
                            ),
                            PillNavItem(
                                route = Destinations.SEARCH,
                                label = "Search",
                                icon = Icons.Filled.Search,
                            ),
                            PillNavItem(
                                route = Destinations.LIBRARY,
                                label = "Library",
                                icon = Icons.AutoMirrored.Filled.List,
                            ),
                        )
                    }

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    val showBottomNavigation = currentDestination?.route != Destinations.ONBOARDING
                    val currentBottomRoute = bottomNavItems.firstOrNull { item ->
                        currentDestination
                            ?.hierarchy
                            ?.any { destination -> destination.route == item.route } == true
                    }?.route

                    Box(modifier = Modifier.fillMaxSize()) {
                        VynNavGraph(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            onboardingViewModel = onboardingViewModel,
                            homeViewModel = homeViewModel,
                            playerViewModel = playerViewModel,
                            startDestination = startDestination,
                        )

                        if (showBottomNavigation) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .systemBarsPadding(),
                                contentAlignment = Alignment.BottomCenter,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    PillNavBar(
                                        items = bottomNavItems,
                                        currentRoute = currentBottomRoute,
                                        onItemClick = { item ->
                                            Log.d("NAVBAR_FLOW", "Selected tab: ${item.label}")
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        modifier = Modifier.padding(bottom = 20.dp),
                                    )
                                }
                            }
                        }

                        PlayerHost(
                            playerViewModel = playerViewModel,
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
