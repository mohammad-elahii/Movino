package com.example.movino.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.movino.R
import com.example.movino.api.GenreItems
import com.example.movino.api.Movie
import com.example.movino.ui.components.GlobalDetailedMovieCard
import com.example.movino.ui.components.GlobalGenresRow
import com.example.movino.ui.components.GlobalLoadingIndicator
import com.example.movino.ui.components.GlobalSnackbarHost
import com.example.movino.ui.components.GlobalTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
        onSearchClick: () -> Unit,
        onMovieClick: (Int) -> Unit,
        viewModel: HomeScreenViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorEvents.collect { event ->
            when (event) {
                is HomeEvent.ShowError -> {
                    val result =
                            snackbarHostState.showSnackbar(
                                    message = event.message,
                                    actionLabel = "Retry",
                                    duration = SnackbarDuration.Short
                            )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.retry()
                    }
                }
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                GlobalTopAppBar(
                        title = stringResource(R.string.home),
                        onIconClick = onSearchClick,
                        scrollBehavior = scrollBehavior
                ) {
                    Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_search_icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            snackbarHost = { GlobalSnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        val genresState = viewModel.genres.collectAsStateWithLifecycle()
        val selectedGenreId = viewModel.selectedGenreId.collectAsStateWithLifecycle()
        val filteredMovies = viewModel.filteredMovies.collectAsStateWithLifecycle()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val hasMorePages = viewModel.hasMorePages.collectAsStateWithLifecycle()
        val isLoadingMore = viewModel.isLoadingMore.collectAsStateWithLifecycle()

        HomeContent(
                modifier = Modifier.padding(innerPadding),
                genres = genresState.value,
                selectedGenreId = selectedGenreId.value,
                filteredMovies = filteredMovies.value,
                uiState = uiState.value,
                movies = filteredMovies.value,
                onGenreSelect = { genreId -> viewModel.selectGenre(genreId) },
                onMovieClick = onMovieClick,
                onLoadMore = { viewModel.loadMoreMovies() },
                hasMorePages = hasMorePages.value,
                isLoadingMore = isLoadingMore.value
        )
    }
}

@Composable
fun HomeContent(
        modifier: Modifier = Modifier,
        genres: List<GenreItems>,
        selectedGenreId: Int,
        filteredMovies: List<Movie>,
        uiState: HomeUIState,
        movies: List<Movie>,
        onGenreSelect: (Int) -> Unit,
        onMovieClick: (Int) -> Unit,
        onLoadMore: () -> Unit = {},
        hasMorePages: Boolean = false,
        isLoadingMore: Boolean = false
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val canLoadMore = hasMorePages && !isLoadingMore
            Triple(lastVisibleIndex, totalItems, canLoadMore)
        }
                .collect { (lastVisibleIndex, totalItems, canLoadMore) ->
                    if (totalItems > 0 && lastVisibleIndex >= totalItems - 5 && canLoadMore) {
                        onLoadMore()
                    }
                }
    }

    Column(modifier = modifier.fillMaxSize().padding(start = 16.dp)) {
        when (uiState) {
            is HomeUIState.Loading -> {
                GlobalLoadingIndicator()
            }
            is HomeUIState.Error -> {
                // emit shode
            }
            is HomeUIState.Success -> {
                LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item(key = "genres") {
                        Column(modifier = Modifier) {
                            Text(
                                    stringResource(R.string.genres),
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(start = 1.dp),
                                    color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(Modifier.height(8.dp))
                            GlobalGenresRow(
                                    genres = genres,
                                    selectedGenreId = selectedGenreId,
                                    onGenreSelect = onGenreSelect
                            )
                        }
                    }
                    item(key = "movies") {
                        Text(
                                stringResource(R.string.movies),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(start = 1.dp),
                                color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    item(key = "movieCard") {
                        LazyRow(
                                modifier = Modifier.height(164.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(movies.take(10), key = { it.id }) { movie ->
                                FeaturedMovieCard(
                                        movie = movie,
                                        onClick = { onMovieClick(movie.id) }
                                )
                            }
                        }
                    }
                    items(items = movies, key = { it.id }) { movie ->
                        GlobalDetailedMovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
                    }
                    if (isLoadingMore) {
                        item(key = "more") {
                            Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                            ) {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedMovieCard(movie: Movie, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
            modifier = modifier.width(265.dp).height(148.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val imageUrl = movie.images?.firstOrNull() ?: movie.poster

            AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
            )

            Box(
                    modifier =
                            Modifier.fillMaxSize()
                                    .background(
                                            color =
                                                    MaterialTheme.colorScheme.primary.copy(
                                                            alpha = 0.5f
                                                    )
                                    )
            )

            Text(
                    text = movie.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFFFFFFF),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            )
        }
    }
}
