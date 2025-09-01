package com.example.http.client.notion.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RichText(
    val type: Type,
    @SerialName("plain_text") val plainText: String,
    val text: TextContent? = null,
    val mention: MentionContent? = null,
    val equation: EquationContent? = null,
    val annotations: Annotations = Annotations(),
    val href: String? = null
) {
    @Serializable
    enum class Type {
        @SerialName("text")
        TEXT,
        @SerialName("mention")
        MENTION,
        @SerialName("equation")
        EQUATION
    }

    @Serializable
    data class TextContent(
        val content: String,
        val link: Link? = null
    )

    @Serializable
    data class Link(
        val url: String
    )

    @Serializable
    data class EquationContent(
        val expression: String
    )

    @Serializable
    data class MentionContent(
        val type: MentionType,
        val database: IdObject? = null,
        val page: IdObject? = null,
        val user: UserReference? = null,
        val date: DateRange? = null,
        @SerialName("template_mention")
        val templateMention: TemplateMention? = null,
        @SerialName("link_preview")
        val linkPreview: LinkPreview? = null
    ) {
        @Serializable
        enum class MentionType {
            @SerialName("database")
            DATABASE,

            @SerialName("page")
            PAGE,

            @SerialName("user")
            USER,

            @SerialName("date")
            DATE,

            @SerialName("template_mention")
            TEMPLATE_MENTION,

            @SerialName("link_preview")
            LINK_PREVIEW
        }

        @Serializable
        data class IdObject(val id: String)

        @Serializable
        data class UserReference(
            val `object`: String,
            val id: String
        )

        @Serializable
        data class DateRange(
            val start: String,
            val end: String? = null
        )

        @Serializable
        data class TemplateMention(
            val type: TemplateMentionType,
            @SerialName("template_mention_date")
            val templateMentionDate: String? = null,
            @SerialName("template_mention_user")
            val templateMentionUser: String? = null
        ) {
            @Serializable
            enum class TemplateMentionType {
                @SerialName("template_mention_date")
                DATE,

                @SerialName("template_mention_user")
                USER
            }
        }

        @Serializable
        data class LinkPreview(val url: String)
    }

    @Serializable
    data class Annotations(
        val bold: Boolean = false,
        val italic: Boolean = false,
        val strikethrough: Boolean = false,
        val underline: Boolean = false,
        val code: Boolean = false,
        val color: String = "default"
    )
}
