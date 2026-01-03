// File: app/src/main/java/com/artepoch/ui/nav/NavGraph.kt
package com.artepoch.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.artepoch.ui.screens.detail.DetailScreen
import com.artepoch.ui.screens.movement.MovementScreen
import com.artepoch.ui.screens.results.ResultsScreen
import com.artepoch.ui.screens.splash.SplashPeriodScreen
import com.artepoch.viewmodel.ArtViewModel

@Composable
fun NavGraph(
    vm: ArtViewModel
) {
    val navController = rememberNavController()
    val state by vm.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashPeriodScreen(
                onPeriodSelected = { period ->
                    vm.selectPeriod(period)
                    navController.navigate(Routes.MOVEMENT)
                }
            )
        }

        composable(Routes.MOVEMENT) {
            MovementScreen(
                selectedPeriod = state.selectedPeriod,
                onMovementSelected = { movement ->
                    vm.selectMovement(movement)
                    vm.loadResults()
                    navController.navigate(Routes.RESULTS)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.RESULTS) {
            ResultsScreen(
                state = state,
                onArtworkClick = { artwork ->
                    vm.openArtwork(artwork)
                    navController.navigate(Routes.DETAIL)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.DETAIL) {
            DetailScreen(
                artwork = state.selectedArtwork,
                onBack = {
                    vm.clearSelectedArtwork()
                    navController.popBackStack()
                }
            )
        }
    }
}