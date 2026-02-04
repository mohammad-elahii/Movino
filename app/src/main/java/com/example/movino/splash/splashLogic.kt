package com.example.movino.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds splash
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    SplashUI()
}
