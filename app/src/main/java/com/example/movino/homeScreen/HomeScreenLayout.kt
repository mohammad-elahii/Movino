package com.example.movino.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.graphics.ColorFilter
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
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                            shape = RoundedCornerShape(4.dp),
                            snackbarData = data,
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 34.dp),
                            actionContentColor = MaterialTheme.colorScheme.secondary,
                            dismissActionContentColor = MaterialTheme.colorScheme.secondary,
                            containerColor = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
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
                LoadingIndicator()
            }
            is HomeUIState.Error -> {}
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
                            GenresFilterRow(
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
                        DetailedMovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
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
fun GenresFilterRow(genres: List<GenreItems>, selectedGenreId: Int, onGenreSelect: (Int) -> Unit) {
    LazyRow(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(genres, key = { it.id }) { genre ->
            val isSelected = genre.id == selectedGenreId

            FilterChip(
                    shape = MaterialTheme.shapes.large,
                    colors =
                            FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                    border = null,
                    selected = isSelected,
                    onClick = { onGenreSelect(genre.id) },
                    label = {
                        if (isSelected) {
                            Text(
                                    text = genre.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                    text = genre.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
            )
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

@Composable
fun DetailedMovieCard(movie: Movie, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
            modifier = modifier.width(375.dp).height(160.dp).padding(end = 16.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = onClick,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                    model = movie.poster,
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight().width(107.dp),
                    contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier =
                            Modifier.fillMaxSize().padding(top = 18.dp, bottom = 18.dp, end = 1.dp)
            ) {
                Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                                painter = painterResource(R.drawable.ic_country),
                                contentDescription = null,
                                modifier = Modifier.width(12.dp).height(16.dp),
                                colorFilter =
                                        ColorFilter.tint(
                                                MaterialTheme.colorScheme.onSurface.copy(0.5f)
                                        )
                        )

                        Text(
                                text = movie.country ?: "No Country in API Response",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                                painter = painterResource(R.drawable.ic_date),
                                contentDescription = null,
                                modifier = Modifier.width(12.dp).height(16.dp),
                                colorFilter =
                                        ColorFilter.tint(
                                                MaterialTheme.colorScheme.onSurface.copy(0.5f)
                                        )
                        )

                        Text(
                                text = movie.year ?: "No Year in API Response",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                                painter = painterResource(R.drawable.ic_genres),
                                contentDescription = null,
                                modifier = Modifier.width(12.dp).height(16.dp),
                                colorFilter =
                                        ColorFilter.tint(
                                                MaterialTheme.colorScheme.onSurface.copy(0.5f)
                                        )
                        )

                        Text(
                                text = movie.genres?.take(2)?.joinToString(", ") ?: "Not Available",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                            modifier =
                                    Modifier.background(
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    shape = MaterialTheme.shapes.medium
                                            )
                                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                                text = "IMDb: ${movie.imdb_rating ?: "0.0"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}
