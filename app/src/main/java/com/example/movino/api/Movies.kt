package com.example.movino.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movies(val data: List<Movie>, val metadata: Metadata)

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val poster: String? = null,
    val genres: List<String>? = null,
    val images: List<String>? = null,
    val country: String? = null,
    val year: String? = null,
    @SerialName("imdb_rating")
    val imdbRating: String? = null,
)

@Serializable
data class Metadata(
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("per_page")
    val perPage: Int,
    @SerialName("page_count")
    val pageCount: Int,
    @SerialName("total_count")
    val totalCount: Int
)

@Serializable
data class MovieByID(
    val id: Int,
    val title: String? = null,
    val year: String? = null,
    val rated: String? = null,
    val runtime: String? = null,
    val director: String? = null,
    val writer: String? = null,
    val actors: String? = null,
    val plot: String? = null,
    val country: String? = null,
    val metascore: String? = null,
    @SerialName("imdb_rating")
    val imdbRating: String? = null,
    val genres: List<String>? = null,
    val images: List<String>? = null
)

@Serializable
data class GenreItems(val id: Int, val name: String)
