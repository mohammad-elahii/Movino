package com.example.movino.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movino.homeScreen.HomeScreen
import com.example.movino.searchScreen.SearchScreen

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
                }
            )
        }
        
        composable<AppRoute.Search> {
            SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
