package com.example.movino.detailScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import androidx.lifecycle.viewModelScope
import com.example.movino.api.GetMovie
import com.example.movino.api.initializeClient
import kotlinx.coroutines.launch

class DetailScreenViewModel(
    private val movieId: String,
    private val getMovie: GetMovie = GetMovie(initializeClient())
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUIState>(DetailUIState.Loading)
    val uiState: StateFlow<DetailUIState> = _uiState.asStateFlow()

    init {
        loadMovie(movieId)
    }

    private fun loadMovie(id: String) {
        viewModelScope.launch {
            _uiState.value = DetailUIState.Loading
            try {
                val movie = getMovie.getMovieById(id)
                _uiState.value = DetailUIState.Success(movie)
            } catch (e: Exception) {
                _uiState.value = DetailUIState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
