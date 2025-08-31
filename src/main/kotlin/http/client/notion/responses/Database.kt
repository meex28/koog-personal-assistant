package com.example.http.client.notion.responses

import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class Database(
    val id: Uuid,
    val title: List<RichText>,
)
