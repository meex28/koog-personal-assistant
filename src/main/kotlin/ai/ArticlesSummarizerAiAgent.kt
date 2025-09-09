@file:Suppress("FunctionName")

package com.example.ai

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.asTool
import ai.koog.agents.core.tools.ToolParameterDescriptor
import ai.koog.agents.core.tools.ToolParameterType
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.agents.features.eventHandler.feature.EventHandler
import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.markdown.markdown
import ai.koog.prompt.text.text
import tools.WebToolset

fun ArticlesSummarizerAiAgent(
    executor: PromptExecutor,
    llmModel: LLModel
): AIAgent<String, String> {
    val webTools = WebToolset()

    val systemPrompt = markdown {
        h1("Context")

        +text {
            +"You are a specialized assistant focused on summarizing software engineering articles."
            +"The user should always provide an article URL. Use your tools to fetch the article content."
        }

        h1("Rules")
        bulleted {
            item("Always summarize the article according to the user's instructions (length, focus, style).")
            item("If no instructions are given, create a concise summary highlighting: problem, proposed solution, findings, and implications for software engineers.")
            item("If the article cannot be accessed, clearly state this instead of producing a summary.")
            item("Do not store, manage, or alter articles — focus only on summarization.")
        }

        h1("Output format")
        bulleted {
            item("Always return output in markdown format.")
            item("Start with the article title and URL in this format: **<TITLE>** (<URL>).")
            item("Then provide the summary in structured markdown (paragraphs and/or bullet points).")
        }
    }

    return AIAgent(
        executor = executor,
        llmModel = llmModel,
        toolRegistry = ToolRegistry {
            tools(webTools)
        },
        systemPrompt = systemPrompt
    ) {
        install(EventHandler) {
            onToolCall {
                println("Called tool: ${it.tool.name} with args: ${it.toolArgs}")
            }
        }
    }
}

fun ArticlesSummarizerAiAgentTool(
    executor: PromptExecutor,
    llmModel: LLModel
) = ArticlesSummarizerAiAgent(
    executor = executor,
    llmModel = llmModel
).asTool(
    agentName = "articleSummarizer",
    agentDescription = "Specialized agent for summarizing software engineering articles. Retrieves article content by URL and generates structured markdown summaries.",
    inputDescriptor = ToolParameterDescriptor(
        name = "articlesSummaryRequest",
        description = "Article summarization request with required URL and optional instructions for summary style or focus.",
        type = ToolParameterType.String
    ),
)
