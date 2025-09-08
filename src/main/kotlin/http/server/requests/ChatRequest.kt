package com.example.http.server.requests

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

// TODO: split to be history and input
@Serializable
data class ChatHistory(
    val messages: List<ChatMessage>,
) {
    fun addMessages(messages: List<ChatMessage>) = this.copy(
        messages = this.messages + messages
    )

    fun addMessage(message: ChatMessage) = addMessages(listOf(message))
}

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("from")
sealed interface ChatMessage {
    val content: String

    @SerialName("user")
    @Serializable
    data class UserChatMessage(override val content: String) : ChatMessage

    @SerialName("assistant")
    @Serializable
    data class AssistantChatMessage(override val content: String) : ChatMessage

    @SerialName("system")
    @Serializable
    data class SystemChatMessage(override val content: String) : ChatMessage
}
