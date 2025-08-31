package com.example.http.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO

fun buildHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(
    engineFactory = CIO,
    block = config,
)
