package com.example.movino.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class GetMovie(val client: HttpClient) {
    suspend fun getMovies(): Movies {
        return client.get("movies").body()
    }

    suspend fun getMovieById(id: String): MovieByID {
        return client.get("movies/$id").body()
    }

}