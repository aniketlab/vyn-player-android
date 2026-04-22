package com.vyn.player.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vyn.player.ui.onboarding.OnboardingScreen
import com.vyn.player.ui.onboarding.OnboardingViewModel
import com.vyn.player.ui.screens.home.HomeScreen
import com.vyn.player.ui.screens.home.HomeViewModel
import com.vyn.player.ui.screens.library.LibraryScreen
import com.vyn.player.ui.screens.player.PlayerScreen
import com.vyn.player.ui.screens.player.PlayerViewModel
import com.vyn.player.ui.screens.search.SearchScreen

object Destinations {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val SEARCH = "search"
    const val LIBRARY = "library"
    const val PLAYER = "player"
}

@Composable
fun VynNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onboardingViewModel: OnboardingViewModel,
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel,
    startDestination: String,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Destinations.ONBOARDING) {
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onCompleted = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.ONBOARDING) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(Destinations.HOME) {
            HomeScreen(
                viewModel = homeViewModel,
                playerViewModel = playerViewModel,
            )
        }
        composable(Destinations.SEARCH) {
            SearchScreen()
        }
        composable(Destinations.LIBRARY) {
            LibraryScreen()
        }
        composable(Destinations.PLAYER) { PlayerScreen(viewModel = playerViewModel) }
    }
}
