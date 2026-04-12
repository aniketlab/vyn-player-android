package com.vyn.player.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    playerViewModel: PlayerViewModel,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destinations.HOME,
    ) {
        composable(Destinations.HOME) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "Home")
                Text(text = "No songs yet")
            }
        }
        composable(Destinations.PLAYER) { PlayerScreen(viewModel = playerViewModel) }
    }
}
