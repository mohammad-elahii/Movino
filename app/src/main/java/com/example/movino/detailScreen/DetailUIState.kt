package com.example.movino.detailScreen

data class Movie(
        val title: String,
        val year: String,
        val contentRating: String, // e.g. "R"
        val runtime: String, // e.g. "142 min"
        val genre: String, // e.g. "Drama"
        val imdbRating: String, // e.g. "9.3"
        val metaRating: String, // e.g. "81"
        val description: String,
        val director: String,
        val writers: String,
        val actors: String,
        val posterUrl:
                String // For now using URL string, but will use a placeholder or generic image
// logic in UI
)

sealed class DetailUIState {
    object Loading : DetailUIState()
    data class Success(val movie: Movie) : DetailUIState()
    data class Error(val message: String) : DetailUIState()
}
