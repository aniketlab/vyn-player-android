package com.vyn.player.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vyn.player.ui.screens.home.HomeScreen
import com.vyn.player.ui.screens.home.HomeViewModel
import com.vyn.player.ui.screens.player.PlayerViewModel
import com.vyn.player.ui.screens.player.PlayerScreen

object Destinations {
    const val HOME = "home"
    const val PLAYER = "player"
}

@Composable
fun VynNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destinations.HOME,
    ) {
        composable(Destinations.HOME) {
            HomeScreen(
                viewModel = homeViewModel,
                playerViewModel = playerViewModel,
            )
        }
        composable(Destinations.PLAYER) { PlayerScreen(viewModel = playerViewModel) }
    }
}
