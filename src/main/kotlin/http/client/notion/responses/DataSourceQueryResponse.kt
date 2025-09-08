package com.example.http.client.notion.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DataSourceQueryResponse(
    val results: List<Page>,
    @SerialName("has_more") val hasMore: Boolean,
)
