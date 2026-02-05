package com.example.movino.api

import kotlinx.serialization.Serializable

@Serializable data class Movies(val data: List<Movie>, val metadata: Metadata)

@Serializable
data class Movie(
        val id: Int,
        val title: String,
        val poster: String? = null,
        val genres: List<String>? = null,
        val images: List<String>? = null,
        val country: String? = null,
        val year: String? = null,
        val imdb_rating: String? = null,
)

@Serializable
data class Metadata(
        val current_page: Int,
        val per_page: Int,
        val page_count: Int,
        val total_count: Int
)

@Serializable
data class MovieByID(
        val id: Int,
        val title: String?=null,
        val year: String? = null,
        val rated: String? = null,
        val runtime: String? = null,
        val director: String? = null,
        val writer: String? = null,
        val actors: String? = null,
        val plot: String? = null,
        val country: String? =null,
        val metascore: String? = null,
        val imdb_rating: String? = null,
        val genres: List<String>? = null,
        val images: List<String>? = null
)

@Serializable data class GenreItems(val id: Int, val name: String)
