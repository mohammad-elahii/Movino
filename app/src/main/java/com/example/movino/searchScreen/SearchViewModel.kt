package com.example.movino.searchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movino.api.GetMovie
import com.example.movino.api.initializeClient
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getMovie: GetMovie = GetMovie(initializeClient())
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _uiState = MutableStateFlow<SearchUIState>(SearchUIState.Idle)
    val uiState: StateFlow<SearchUIState> = _uiState

    @OptIn(FlowPreview::class)
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        
        if (query.isBlank()) {
            _uiState.value = SearchUIState.Idle
            return
        }
        
        // Perform search
        searchMovies(query)
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            _uiState.value = SearchUIState.Loading
            try {
                // Get all movies and filter by title
                val response = getMovie.getMovies()
                val filteredMovies = response.data.filter { movie ->
                    movie.title.contains(query, ignoreCase = true)
                }
                
                _uiState.value = if (filteredMovies.isEmpty()) {
                    SearchUIState.Empty
                } else {
                    SearchUIState.Success(filteredMovies)
                }
            } catch (e: Exception) {
                _uiState.value = SearchUIState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
