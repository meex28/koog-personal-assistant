package com.example.http.server

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.AIAgentTool
import ai.koog.agents.core.agent.asTool
import ai.koog.agents.core.tools.Tool
import ai.koog.agents.core.tools.ToolParameterDescriptor
import ai.koog.agents.core.tools.ToolParameterType
import ai.koog.prompt.dsl.PromptBuilder
import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import ai.koog.prompt.executor.model.PromptExecutor
import com.example.ai.articlesAiAgent
import com.example.ai.personalAiAgent
import com.example.http.client.notion.NotionClient
import com.example.http.client.notion.NotionClientConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module

val notionModule = module {
    single<NotionClientConfig> {
        NotionClientConfig(
            token = System.getenv("NOTION_API_KEY") ?: error("Missing NOTION_API_KEY environment variable")
        )
    }

    single<NotionClient> {
        NotionClient(
            config = get()
        )
    }
}

val aiConfigurationModule = module {
    single<PromptExecutor> {
        val apiKey = System.getenv("OPEN_ROUTER_API_KEY")
        simpleOpenRouterExecutor(apiKey)
    }
}

val aiAgentsModule = module {
    single<AIAgent<*, *>>(named("articlesAiAgent")) {
        articlesAiAgent(
            executor = get(),
            llmModel = OpenRouterModels.GPT5
        )
    }

    single<Tool<AIAgentTool.AgentToolArgs, AIAgentTool.AgentToolResult>>(named("articlesAiAgentTool")) {
        get<AIAgent<String, String>>(named("articlesAiAgent")).asTool(
            agentName = "articlesExpert",
            agentDescription = "An expert in article summarization. Capabilities: retrieve article content by URL and create summaries.",
            inputDescriptor = ToolParameterDescriptor(
                name = "request",
                description = "Article summarization request",
                type = ToolParameterType.String
            ),
        )
    }

    factory<AIAgent<String, String>>(named("personalAiAgent")) { (buildPrompt: PromptBuilder.() -> Unit) ->
        personalAiAgent(
            notionClient = get(),
            executor = get(),
            llmModel = OpenRouterModels.GPT5,
            additionalTools = {
                tool(get<Tool<*, *>>(named("articlesAiAgentTool")))
            },
            buildPrompt = buildPrompt
        )
    }
}
