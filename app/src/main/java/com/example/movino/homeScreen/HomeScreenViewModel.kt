package com.example.movino.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movino.api.GenreItems
import com.example.movino.api.GetMovie
import com.example.movino.api.Movie
import com.example.movino.api.initializeClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val getMovie: GetMovie = GetMovie(initializeClient())) :
        ViewModel() {

    private val _genres = MutableStateFlow<List<GenreItems>>(emptyList())
    val genres: StateFlow<List<GenreItems>> = _genres

    private val _selectedGenreId = MutableStateFlow(-1)
    val selectedGenreId: StateFlow<Int> = _selectedGenreId

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())

    private val _filteredMovies = MutableStateFlow<List<Movie>>(emptyList())
    val filteredMovies: StateFlow<List<Movie>> = _filteredMovies

    private val _currentPage = MutableStateFlow(1)
    private val _hasMorePages = MutableStateFlow(true)
    val hasMorePages: StateFlow<Boolean> = _hasMorePages

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val uiState: StateFlow<HomeUIState> = _uiState

    private val _errorEvents = MutableSharedFlow<HomeEvent>(replay = 0, extraBufferCapacity = 1)
    val errorEvents: SharedFlow<HomeEvent> = _errorEvents

    init {
        fetchGenres()
        fetchMovies()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            _uiState.value = HomeUIState.Loading
            try {
                val response = getMovie.getGenres()
                val allGenre = GenreItems(id = -1, name = "All")
                _genres.value = listOf(allGenre) + response
                _uiState.value = HomeUIState.Success
            } catch (e: Exception) {
                val message = e.message ?: "Unknown error"
                _uiState.value = HomeUIState.Error(message)
                _errorEvents.emit(HomeEvent.ShowError(message))
            }
        }
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                _currentPage.value = 1
                val response = getMovie.getMovies(page = 1)
                _movies.value = response.data
                _hasMorePages.value = response.metadata.current_page < response.metadata.page_count
                filterMovies()
            } catch (e: Exception) {
                val message = e.message ?: "Unknown error"
                _uiState.value = HomeUIState.Error(message)
                _errorEvents.emit(HomeEvent.ShowError(message))
            }
        }
    }

    fun loadMoreMovies() {
        if (!_hasMorePages.value || _isLoadingMore.value) return
        viewModelScope.launch {
            _isLoadingMore.value = true
            try {
                val nextPage = _currentPage.value + 1
                val response = getMovie.getMovies(page = nextPage)
                _movies.value = _movies.value + response.data
                _currentPage.value = nextPage
                _hasMorePages.value = response.metadata.current_page < response.metadata.page_count
                filterMovies()
            } catch (e: Exception) {
                val message = e.message ?: "Unknown error"
                _errorEvents.emit(HomeEvent.ShowError(message))
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    fun retry() {
        viewModelScope.launch {
            _uiState.value = HomeUIState.Loading
            fetchGenres()
            fetchMovies()
        }
    }

    fun selectGenre(genreId: Int) {
        _selectedGenreId.value = genreId
        filterMovies()
    }

    private fun filterMovies() {
        _filteredMovies.value =
                if (_selectedGenreId.value == -1) {
                    _movies.value
                } else {
                    val selectedGenreName =
                            _genres.value.find { it.id == _selectedGenreId.value }?.name
                    _movies.value.filter { movie ->
                        movie.genres != null && selectedGenreName in movie.genres
                    }
                }
    }
}
