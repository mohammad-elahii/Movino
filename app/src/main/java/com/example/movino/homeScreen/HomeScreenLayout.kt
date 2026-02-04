package com.example.movino.homeScreen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movino.api.GenreItems
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.R
import coil3.compose.AsyncImage
import com.example.movino.api.Movie
import com.example.movino.homeScreen.HomeScreenViewModel
import com.example.movino.homeScreen.HomeUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "fill later")
                },
                actions = {
                    IconButton(onClick = {
                        // fill later
                    }) {

                    }
                }
            )
        }
    ) { innerPadding ->
        val genresState = viewModel.genres.collectAsStateWithLifecycle()
        val selectedGenreId = viewModel.selectedGenreId.collectAsStateWithLifecycle()
        val filteredMovies = viewModel.filteredMovies.collectAsStateWithLifecycle()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        HomeContent(
            modifier = Modifier.padding(innerPadding),
            genres = genresState.value,
            selectedGenreId = selectedGenreId.value,
            uiState = uiState.value,
            movies = filteredMovies.value,
            onGenreSelect = { genreId -> viewModel.selectGenre(genreId) }
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    genres: List<GenreItems>,
    selectedGenreId: Int,
    uiState: HomeUIState,
    movies: List<Movie>,
    onGenreSelect: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start =  16.dp)
    ) {
        when (uiState) {
            is HomeUIState.Loading -> {
                Text("Loading genres...")
            }

            is HomeUIState.Error -> {
                Text("Error: ${uiState.message}")
            }

            is HomeUIState.Success -> {
                Text(
                    "Genres",
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
                Spacer(Modifier.height(8.dp))
                Text(
                    "Movies",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 1.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movies.take(10), key = { it.id }) { movie ->
                        FeaturedMovieCard(movie = movie)
                    }
                }
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(movies, key = { it.id }) { movie ->
                        DetailedMovieCard(movie = movie)
                    }
                }

            }
        }
    }
}

@Composable
fun GenresFilterRow(
    genres: List<GenreItems>,
    selectedGenreId: Int,
    onGenreSelect: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(genres, key = { it.id }) { genre ->
            val isSelected = genre.id == selectedGenreId

            FilterChip(
                shape = MaterialTheme.shapes.large,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),

                border = null,
                selected = isSelected,
                onClick = {
                    onGenreSelect(genre.id)
                },
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
fun FeaturedMovieCard(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(265.dp)
            .height(148.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = movie.poster,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
            )

            Text(
                text = movie.title,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFFFFFFF),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun DetailedMovieCard(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(375.dp)
            .height(160.dp).padding(end = 16.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = movie.poster,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(107.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 18.dp, bottom = 18.dp, end = 1.dp)
                ,
            ) {

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = movie.country ?: "N/A",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(

                    ) {

                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = movie.year ?: "N/A",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(

                    ) {

                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = movie.genres.take(2).joinToString(", "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "IMDb: ${movie.imdb_rating ?: "N/A"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }
}
