package com.example.http.client.notion

import com.example.http.client.buildHttpClient
import com.example.http.client.notion.responses.Database
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

class NotionClient(token: String) {
    private val httpClient = buildHttpClient {
        install(ContentNegotiation) {
            json(json)
        }

        install(DefaultRequest) {
            headers.append("Notion-Version", "2022-06-28")
            bearerAuth(token)
            contentType(ContentType.Application.Json)
        }
    }

    val database = NotionDatabaseClient(httpClient)
}

class NotionDatabaseClient(private val httpClient: HttpClient) {
    suspend fun query(databaseId: String): String {
        val response: HttpResponse = httpClient.post("https://api.notion.com/v1/databases/$databaseId/query")
        return response.bodyAsText()
    }

    suspend fun get(databaseId: String): Database {
        val response: HttpResponse = httpClient.get("https://api.notion.com/v1/databases/$databaseId")
        return response.body<Database>()
    }
}

class NotionPageClient(private val httpClient: HttpClient) {

}