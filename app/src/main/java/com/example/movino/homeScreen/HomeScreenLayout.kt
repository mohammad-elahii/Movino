package com.example.movino.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movino.api.GenreItems

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
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        HomeContent(
            modifier = Modifier.padding(innerPadding),
            genres = genresState.value,
            selectedGenreId = selectedGenreId.value,
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
    uiState: HomeUIState,
    onGenreSelect: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
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