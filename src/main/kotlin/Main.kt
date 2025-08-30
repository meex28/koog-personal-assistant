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
        agent.run("Summarize (max 5 sentences, each as point of a list) this article: https://mcfunley.com/choose-boring-technology")
    }

    println("RESULT:")
    println(result)
}
