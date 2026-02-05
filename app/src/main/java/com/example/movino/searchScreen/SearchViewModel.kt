package com.example.movino.searchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movino.api.GetMovie
import com.example.movino.api.initializeClient
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getMovie: GetMovie = GetMovie(initializeClient())
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _uiState = MutableStateFlow<SearchUIState>(SearchUIState.Idle)
    val uiState: StateFlow<SearchUIState> = _uiState

    private var currentPage = 1
    private var hasMorePages = true
    private var isFetchingNextPage = false

    init {
        viewModelScope.launch {
            @OptIn(FlowPreview::class)
            _searchQuery
                .debounce(500)
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _uiState.value = SearchUIState.Idle
                        return@collectLatest
                    }
                    
                    // Reset pagination for new search
                    currentPage = 1
                    hasMorePages = true
                    
                    searchMovies(query, isNewSearch = true)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private suspend fun searchMovies(query: String, isNewSearch: Boolean) {
        if (isNewSearch) {
            _uiState.value = SearchUIState.Loading
        } else {
             if (isFetchingNextPage) return
             isFetchingNextPage = true
        }

        try {
            val response = getMovie.getMovies(page = currentPage, q = query)
            
            val newMovies = response.data
            hasMorePages = response.metadata.current_page < response.metadata.page_count
            
            if (isNewSearch) {
                if (newMovies.isEmpty()) {
                    _uiState.value = SearchUIState.Empty
                } else {
                    _uiState.value = SearchUIState.Success(newMovies)
                }
            } else {
                val currentSuccessState = _uiState.value as? SearchUIState.Success
                if (currentSuccessState != null) {
                     _uiState.value = SearchUIState.Success(currentSuccessState.movies + newMovies)
                }
            }
        } catch (e: Exception) {
            if (isNewSearch) {
               _uiState.value = SearchUIState.Error(e.message ?: "Unknown error")
            }
             // Handle pagination error silently or with a specific state if needed
        } finally {
            isFetchingNextPage = false
        }
    }

    fun loadNextPage() {
        if (hasMorePages && !isFetchingNextPage && _searchQuery.value.isNotBlank()) {
            currentPage++
            viewModelScope.launch {
                searchMovies(_searchQuery.value, isNewSearch = false)
            }
        }
    }
}
