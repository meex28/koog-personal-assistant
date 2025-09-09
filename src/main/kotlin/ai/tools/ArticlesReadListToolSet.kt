package com.example.ai.tools

import CreatePageRequest
import PageParent
import PageProperty
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.example.http.client.notion.NotionClient
import com.example.http.client.notion.responses.RichText

@LLMDescription("A toolset for operations in a user read list.")
class ArticlesReadListToolSet(
    private val notionClient: NotionClient,
    private val databaseId: String
) : ToolSet {
    @Tool
    @LLMDescription("Save an article in the read list.")
    suspend fun saveArticle(
        @LLMDescription("The title of the article")
        title: String,
        @LLMDescription("The URL of the article")
        url: String,
    ): String {
        val response = notionClient.page.create(
            request = buildCreateArticlePageRequest(
                databaseId = databaseId,
                title = title,
                url = url,
            )
        )

        return "Article saved in Notion database with id ${response.id}"
    }
}

private fun buildCreateArticlePageRequest(
    databaseId: String,
    title: String,
    url: String,
): CreatePageRequest {
    return CreatePageRequest(
        parent = PageParent.Database(databaseId = databaseId),
        properties = mapOf(
            "Title" to PageProperty.Title(
                listOf(
                    RichText(
                        type = RichText.Type.TEXT,
                        text = RichText.TextContent(
                            content = title,
                        ),
                        plainText = title
                    )
                )
            ),
            "Link" to PageProperty.Url(url = url),
        ),
    )
}
