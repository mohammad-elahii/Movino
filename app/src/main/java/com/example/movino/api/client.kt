package com.example.movino.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun initializeClient() = HttpClient {
    defaultRequest {
        url("https://moviesapi.ir/api/v1/")
    }
    install(Logging) {
        logger = Logger.ANDROID
    }

    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
}