package com.example.movino.homeScreen

sealed class HomeUIState {
    object Loading : HomeUIState()
    object Success : HomeUIState()
    data class Error(val message: String) : HomeUIState()
}
