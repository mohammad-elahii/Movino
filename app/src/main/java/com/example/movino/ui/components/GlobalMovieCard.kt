package com.example.movino.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.movino.R
import com.example.movino.api.Movie

@Composable
fun GlobalDetailedMovieCard(movie: Movie, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .width(375.dp)
            .height(160.dp)
            .padding(end = 16.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(top = 18.dp, bottom = 18.dp, end = 1.dp)
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
                            modifier = Modifier
                                .width(12.dp)
                                .height(16.dp),
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
                            modifier = Modifier
                                .width(12.dp)
                                .height(16.dp),
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
                            modifier = Modifier
                                .width(12.dp)
                                .height(16.dp),
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
                            Modifier
                                .background(
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