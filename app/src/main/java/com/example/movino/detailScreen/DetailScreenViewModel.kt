package com.example.movino.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movino.api.GetMovie
import com.example.movino.api.initializeClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailScreenViewModel(
        private val movieId: String,
        private val getMovie: GetMovie = GetMovie(initializeClient())
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUIState>(DetailUIState.Loading)
    val uiState: StateFlow<DetailUIState> = _uiState.asStateFlow()

    private val _errorEvents = MutableSharedFlow<DetailEvent>()
    val errorEvents = _errorEvents.asSharedFlow()

    init {
        loadMovie(movieId)
    }

    fun retry() {
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
                _errorEvents.emit(DetailEvent.ShowError(e.message ?: "Unknown error"))
            }
        }
    }
}
