package com.example.http.client.notion

import CreatePageRequest
import com.example.http.client.buildHttpClient
import com.example.http.client.notion.responses.DataSourceQueryResponse
import com.example.http.client.notion.responses.Database
import com.example.http.client.notion.responses.Page
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
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
            headers.append("Notion-Version", "2025-09-03")
            bearerAuth(token)
            contentType(ContentType.Application.Json)
        }
    }

    val database = NotionDatabaseClient(httpClient)
    val page = NotionPageClient(httpClient)
    val dataSource = NotionDataSourceClient(httpClient)
}

class NotionDatabaseClient(private val httpClient: HttpClient) {
    suspend fun get(databaseId: String): Database {
        val response: HttpResponse = httpClient.get("https://api.notion.com/v1/databases/$databaseId")
        return response.body<Database>()
    }
}

class NotionDataSourceClient(private val httpClient: HttpClient) {
    suspend fun query(dataSourceId: String): DataSourceQueryResponse {
        val response: HttpResponse = httpClient.post("https://api.notion.com/v1/data_sources/$dataSourceId/query")
        return response.body<DataSourceQueryResponse>()
    }
}

class NotionPageClient(private val httpClient: HttpClient) {
    suspend fun create(request: CreatePageRequest): Page {
        val response = httpClient.post("https://api.notion.com/v1/pages") {
            setBody(request)
            contentType(ContentType.Application.Json)
        }
        return response.body()
    }
}
