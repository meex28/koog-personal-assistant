package com.example

import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import kotlinx.coroutines.runBlocking

fun main() {
    val apiKey = System.getenv("OPEN_ROUTER_API_KEY")
    val agent = articlesAiAgent(
        executor = simpleOpenRouterExecutor(apiKey),
        llmModel = OpenRouterModels.GPT5Mini,
    )

    val result = runBlocking {
        agent.run("Add this article to my read list in Notion: https://maikotrindade.com/ai/kotlin/android/development/agents/2025/08/19/building-agentic-ai-mobile-tester-koog-kotlin. Do not write summary but just save it to the database.")
    }

    println("RESULT:")
    println(result)
}
