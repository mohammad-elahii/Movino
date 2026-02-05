package com.example.movino.detailScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUIState>(DetailUIState.Loading)
    val uiState: StateFlow<DetailUIState> = _uiState.asStateFlow()

    init {
        loadMovie()
    }

    private fun loadMovie() {

        val movie =
                Movie(
                        title = "The Shawshank Redemption",
                        year = "1994",
                        contentRating = "R",
                        runtime = "142 min",
                        genre = "Drama",
                        imdbRating = "9.3",
                        metaRating = "81",
                        description =
                                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                        director = "Frank Darabont",
                        writers = "Stephan King, Frank Darabont",
                        actors = "Tim Robbins, Morgan Freeman, Bob Gunton",
                        posterUrl =
                                "https://m.media-amazon.com/images/M/MV5BNDE3ODUxMDQ4MV5BMl5BanBnXkFtZTcwMjY3MzXsw._V1_.jpg"
                )
        _uiState.value = DetailUIState.Success(movie)
    }
}
