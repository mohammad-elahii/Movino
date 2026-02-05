package com.example.movino.searchScreen

import com.example.movino.api.Movie

sealed class SearchUIState {
    object Idle : SearchUIState()
    object Loading : SearchUIState()
    data class Success(val movies: List<Movie>) : SearchUIState()
    object Empty : SearchUIState()
    data class Error(val message: String) : SearchUIState()
}
