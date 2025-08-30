package com.example

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import kotlinx.coroutines.runBlocking

fun main() {
    val apiKey = System.getenv("OPEN_ROUTER_API_KEY")

    val agent = AIAgent(
        executor = simpleOpenRouterExecutor(apiKey),
        llmModel = OpenRouterModels.GPT5Mini,
    )

    val result = runBlocking {
        agent.run("Hello! Who are you? In max 5 words.")
    }

    println(result)
}
