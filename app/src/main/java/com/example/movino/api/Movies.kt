package com.example.movino.api

import kotlinx.serialization.Serializable

@Serializable
data class Movies(
    val data: List<Movie>,
    val metadata: Metadata
)

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val poster: String,
    val genres: List<String>,
    val images: List<String>,
)

@Serializable
data class Metadata(
    val current_page: Int,
    val per_page: Int,
    val page_count: Int,
    val total_count: Int
)

data class MovieByID(
    val id: Int,
    val title: String,
    val year: String,
    val rated: String,
    val runtime: String,
    val director: String,
    val writer: String,
    val actors: String,
    val plot: String,
    val country: String,
    val metascore: String,
    val imdb_rating: String,
    val genres: List<String>,
    val images: List<String>
)