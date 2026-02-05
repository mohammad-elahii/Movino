package com.example.movino.detailScreen

import com.example.movino.api.MovieByID

sealed class DetailUIState {
    object Loading : DetailUIState()
    data class Success(val movie: MovieByID) : DetailUIState()
    data class Error(val message: String) : DetailUIState()
}
