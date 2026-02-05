package com.example.movino.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movino.api.GenreItems

@Composable
fun GlobalGenresRow(genres: List<GenreItems>, selectedGenreId: Int, onGenreSelect: (Int) -> Unit) {
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