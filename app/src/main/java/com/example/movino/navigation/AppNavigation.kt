package com.example.movino.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movino.homeScreen.HomeScreen
import com.example.movino.searchScreen.SearchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.toRoute
import com.example.movino.detailScreen.DetailScreenViewModel
import com.example.movino.detailScreen.Detailscreen
import com.example.movino.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Splash
    ) {
        composable<AppRoute.Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(AppRoute.Home) {
                        popUpTo(AppRoute.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<AppRoute.Home> {
            HomeScreen(
                onSearchClick = {
                    navController.navigate(AppRoute.Search)
                },
                onMovieClick = { movieId ->
                    navController.navigate(AppRoute.Detail(movieId))
                }
            )
        }

        composable<AppRoute.Search> {
            SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onMovieClick = { movieId ->
                    navController.navigate(AppRoute.Detail(movieId))
                }
            )
        }

        composable<AppRoute.Detail> { backStackEntry ->
            val detail: AppRoute.Detail = backStackEntry.toRoute()
            val viewModel: DetailScreenViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return DetailScreenViewModel(movieId = detail.movieId.toString()) as T
                    }
                }
            )
            Detailscreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
