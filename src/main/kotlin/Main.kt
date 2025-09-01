package com.example

import CreatePageRequest
import PageParent
import PageProperty
import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import com.example.http.client.notion.NotionClient
import com.example.http.client.notion.responses.RichText
import kotlinx.coroutines.runBlocking

suspend fun main() {
    val notionClient = NotionClient(token = System.getenv("NOTION_API_KEY"))
    val databaseId = "26021a96563280b093eeda80fee9fb00"

    val response = notionClient.page.create(
        CreatePageRequest(
            parent = PageParent.Database(databaseId = databaseId),
            properties = mapOf(
                "Title" to PageProperty.Title(
                    listOf(
                        RichText(
                            type = RichText.Type.TEXT,
                            text = RichText.TextContent(
                                content = "Test title",
                            ),
                            plainText = "Test title"
                        )
                    )
                ),
                "Author" to PageProperty.RichText(
                    listOf(
                        RichText(
                            type = RichText.Type.TEXT,
                            text = RichText.TextContent(
                                content = "Test Author",
                            ),
                            plainText = "Test Author"
                        )
                    )
                ),
                "Link" to PageProperty.Url("https://example.com"),
            ),
        )
    )

    println(response)
    return

    val apiKey = System.getenv("OPEN_ROUTER_API_KEY")
    val agent = articlesAiAgent(
        executor = simpleOpenRouterExecutor(apiKey),
        llmModel = OpenRouterModels.GPT5Mini,
    )

    val result = runBlocking {
        agent.run("Summarize (max 5 sentences, each as point of a list) this article: https://mcfunley.com/choose-boring-technology")
    }

    println("RESULT:")
    println(result)
}
