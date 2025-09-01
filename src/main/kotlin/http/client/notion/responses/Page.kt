package com.example.http.client.notion.responses

import PageProperty
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class Page(
    val id: Uuid,
    val properties: Map<String, PageProperty>
)
