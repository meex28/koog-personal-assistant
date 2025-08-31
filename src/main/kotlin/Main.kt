package com.example

import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import com.example.http.client.notion.NotionClient
import kotlinx.coroutines.runBlocking

fun main() {
    val notionClient = NotionClient(token = System.getenv("NOTION_API_KEY"))
    val databaseId = "26021a96563280b093eeda80fee9fb00"
    val database = runBlocking {
        notionClient.database.get(databaseId = databaseId)
    }
    println(database)
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
