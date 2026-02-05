package com.example.movino.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class GetMovie(val client: HttpClient) {
    suspend fun getMovies(page: Int = 1): Movies {
        return client.get("movies") {
            parameter("page", page)
        }.body()
    }

    suspend fun getMovieById(id: String): MovieByID {
        return client.get("movies/$id").body()
    }

    suspend fun getGenres(): List<GenreItems> {
        return client.get("genres").body()
    }

}