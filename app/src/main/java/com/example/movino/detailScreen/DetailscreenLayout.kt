package com.example.movino.detailScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.R
import coil3.compose.AsyncImage
import com.example.movino.ui.components.GlobalTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detailscreen(viewModel: DetailScreenViewModel, onBackClick: () -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
            topBar = {
                GlobalTopAppBar(
                        title = "Movie",
                        onIconClick = onBackClick,
                    icon = {
                        Icon(
                            painter = painterResource(com.example.movino.R.drawable.ic_back_icon),
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
                is DetailUIState.Error -> {
                    Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetailUIState.Success -> {
                    MovieDetailContent(movie = state.movie)
                }
            }
        }
    }
}

@Composable
fun MovieDetailContent(movie: Movie) {
    Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier =
                        Modifier.fillMaxWidth()
                                .aspectRatio(
                                        1.5f
                                )
                                .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
        )

        Text(
                text = movie.title,
                style =
                        MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                        )
        )

        Text(
                text = "${movie.year} . ${movie.contentRating} . ${movie.runtime} . ${movie.genre}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
        )

        Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            RatingBadge(
                    backgroundColor = Color(0xFFF6C700), // IMDb Yellow
                    textColor = Color.Black,
                    icon = { Text(text = "IMDb", fontWeight = FontWeight.Black, fontSize = 10.sp) },
                    score = movie.imdbRating
            )

            RatingBadge(
                    backgroundColor = Color(0xFFECA046), // Metacritic Orangeish
                    textColor = Color.Black,
                    icon = {
                        Box(
                                modifier =
                                        Modifier.size(16.dp)
                                                .background(
                                                        Color.Black,
                                                        shape = RoundedCornerShape(50)
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                            Text(
                                    "M",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    score = movie.metaRating
            )
        }

        Surface(
                color = Color(0xFFFFF9F2),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 20.sp
            )
        }

        CreditRow(label = "Director", value = movie.director)
        CreditRow(label = "Writers", value = movie.writers)
        CreditRow(label = "Actors", value = movie.actors)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RatingBadge(
        backgroundColor: Color,
        textColor: Color,
        icon: @Composable () -> Unit,
        score: String
) {
    Row(
            modifier =
                    Modifier.background(backgroundColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()
        Text(
                text = score,
                color = textColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CreditRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Text(
                text = label,
                style =
                        MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD35D5D)
                        ),
                modifier = Modifier.width(80.dp)
        )
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
