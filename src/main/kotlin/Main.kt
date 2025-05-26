package com.example

import ai.koog.agents.ext.agent.simpleSingleRunAgent
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import kotlinx.coroutines.runBlocking
import java.util.Properties
import java.io.FileInputStream

fun main() {
    val openAiKey = readOpenAiApiKey() ?: return

    val agent = simpleSingleRunAgent(
        executor = simpleOpenAIExecutor(openAiKey),
        systemPrompt = "You are my assistant to create notes.",
        llmModel = OpenAIModels.Chat.GPT4o
    )

    val result = runBlocking {
        agent.runAndGetResult("Can you give me a proposal of structure for my future notes?")
    }

    println(result)
}

fun readOpenAiApiKey(): String? = Properties().apply {
        FileInputStream("src/main/resources/apikey.properties").use { fileInputStream ->
            load(fileInputStream)
        }
    }.getProperty("openai.api.key")
