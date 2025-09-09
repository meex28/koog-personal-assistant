package com.example.ai

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.AIAgentTool
import ai.koog.agents.core.tools.Tool
import ai.koog.prompt.dsl.PromptBuilder
import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import ai.koog.prompt.executor.model.PromptExecutor
import org.koin.core.qualifier.named
import org.koin.dsl.module

val aiConfigurationModule = module {
    single<PromptExecutor> {
        val apiKey = System.getenv("OPEN_ROUTER_API_KEY")
        simpleOpenRouterExecutor(apiKey = apiKey)
    }
}

val aiAgentsModule = module {
    single<Tool<AIAgentTool.AgentToolArgs, AIAgentTool.AgentToolResult>>(named("ArticlesSummarizerAiAgentTool")) {
        ArticlesSummarizerAiAgentTool(
            executor = get(),
            llmModel = OpenRouterModels.GPT5
        )
    }

    factory<AIAgent<String, String>>(named("PersonalAiAgent")) { (buildPrompt: PromptBuilder.() -> Unit) ->
        PersonalAiAgent(
            notionClient = get(),
            executor = get(),
            llmModel = OpenRouterModels.GPT5Mini,
            additionalTools = {
                tool(get<Tool<*, *>>(named("ArticlesSummarizerAiAgentTool")))
            },
            buildPrompt = buildPrompt
        )
    }
}
