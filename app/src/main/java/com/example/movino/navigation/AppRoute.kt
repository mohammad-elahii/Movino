package com.example.movino.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute {
    @Serializable
    object Home : AppRoute()

    @Serializable
    object Search : AppRoute()

    @Serializable
    object Splash : AppRoute()

    @Serializable
    data class Detail(val movieId: Int) : AppRoute()
}
