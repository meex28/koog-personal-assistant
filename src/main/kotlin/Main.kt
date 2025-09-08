package com.example

import http.server.startHttpServer

fun main() {
    startHttpServer()
//    val apiKey = System.getenv("OPEN_ROUTER_API_KEY")
//    val agent = articlesAiAgent(
//        executor = simpleOpenRouterExecutor(apiKey),
//        llmModel = OpenRouterModels.GPT5Mini,
//    )
//
//    val input = """
//        I read recently this article: https://event-driven.io/en/idempotent_command_handling/
//        Add this article to my Notion read list and write for a short summary (few keypoints).
//    """.trimIndent()
//
//    val result = runBlocking {
//        agent.run(input)
//    }
//
//    println("RESULT:")
//    println(result)
}
