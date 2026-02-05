package com.example.movino.detailScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.movino.R
import com.example.movino.api.MovieByID
import com.example.movino.ui.components.GlobalTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detailscreen(viewModel: DetailScreenViewModel, onBackClick: () -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
            topBar = {
                GlobalTopAppBar(
                        title = stringResource(R.string.movies),
                        onIconClick = onBackClick,
                        icon = {
                            Icon(
                                    painter = painterResource(R.drawable.ic_back_icon),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                )
            }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (val state = uiState) {
                is DetailUIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetailUIState.Error -> {}
                is DetailUIState.Success -> {
                    MovieDetailContent(movie = state.movie)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailContent(movie: MovieByID) {
    Column(
            modifier =
                    Modifier.fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ) {
        AsyncImage(
                error = painterResource(R.drawable.ic_not_poster_available),
                model = movie.images?.firstOrNull(),
                contentDescription = movie.title,
                modifier =
                        Modifier.fillMaxWidth()
                                .width(380.dp)
                                .height(214.dp)
                                .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(16.dp))

        Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(16.dp))
        Text(
                text =
                        "${movie.year} . ${movie.rated} . ${movie.runtime} . ${
                movie.genres.joinToString(
                    ", "
                )
            }",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(16.dp))
        Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                    modifier =
                            Modifier.background(
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = MaterialTheme.shapes.extraSmall
                                    )
                                    .width(68.dp)
                                    .height(36.dp)
                                    .padding(all = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                        painterResource(R.drawable.ic_imdb),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
                )

                Text(
                        text = movie.imdb_rating,
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelLarge
                )
            }
            Row(
                    modifier =
                            Modifier.background(
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = MaterialTheme.shapes.extraSmall
                                    )
                                    .width(68.dp)
                                    .height(36.dp)
                                    .padding(all = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                        painterResource(R.drawable.ic_metacritic),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
                )
                Text(
                        text = movie.metascore,
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelLarge
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                    text = movie.plot,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.height(16.dp))
        CreditRow(label = "Director", value = movie.director)
        Spacer(Modifier.height(16.dp))
        CreditRow(label = "Writers", value = movie.writer)
        Spacer(Modifier.height(16.dp))
        CreditRow(label = "Actors", value = movie.actors)
        Spacer(Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))

        if (movie.images?.isNotEmpty() ?: false) {

            val carouselState = rememberCarouselState { movie.images.size }

            HorizontalMultiBrowseCarousel(
                    state = carouselState,
                    preferredItemWidth = 300.dp,
                    itemSpacing = 4.dp,
                    modifier = Modifier.fillMaxWidth().height(180.dp)
            ) { i ->
                AsyncImage(
                        model = movie.images!![i],
                        contentDescription = null,
                        modifier = Modifier.maskClip(MaterialTheme.shapes.medium).fillMaxSize(),
                        contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun CreditRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(56.dp)
        )
        Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
        )
    }
}
