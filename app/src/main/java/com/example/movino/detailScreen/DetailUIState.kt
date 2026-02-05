package com.example.movino.detailScreen

data class Movie(
        val title: String,
        val year: String,
        val contentRating: String,
        val runtime: String,
        val genre: String,
        val imdbRating: String,
        val metaRating: String,
        val description: String,
        val director: String,
        val writers: String,
        val actors: String,
        val posterUrl: String)

sealed class DetailUIState {
    object Loading : DetailUIState()
    data class Success(val movie: Movie) : DetailUIState()
    data class Error(val message: String) : DetailUIState()
}
