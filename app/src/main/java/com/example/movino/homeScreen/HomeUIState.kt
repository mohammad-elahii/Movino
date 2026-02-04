package com.example.movino.homeScreen

sealed class HomeUIState {
    object Loading : HomeUIState()
    object Success : HomeUIState()
    data class Error(val message: String) : HomeUIState()
}

sealed class HomeEvent {
    data class ShowError(val message: String) : HomeEvent()
}
