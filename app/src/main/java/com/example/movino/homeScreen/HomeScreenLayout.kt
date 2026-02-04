package com.example.movino.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.movino.api.GenreItems
import com.example.movino.api.Movie


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
            filteredMovies = filteredMovies.value,
            uiState = uiState.value,
            onGenreSelect = { genreId -> viewModel.selectGenre(genreId) }
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
    onGenreSelect: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp)
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
                    items(filteredMovies.take(10), key = { it.id }) { movie ->
                        FeaturedMovieCard(movie = movie)
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