package com.example

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.markdown.markdown
import ai.koog.prompt.text.text
import tools.WebToolset

fun articlesAiAgent(executor: PromptExecutor, llmModel: LLModel): AIAgent<String, String> {
    val webTools = WebToolset()

    val systemPropt = markdown {
        h1("Context")

        +text {
            +"You are a helpful assistant helping user to summarize articles."
            +"User provides an article (URL) and your task is to summarize it."
        }

        h1("Rules")
        bulleted {
            item("Create summary in accordance to user's instructions.")
        }

        h1("Output format")
        bulleted {
            item("ALWAYS return output in markdown format.")
            item("Include title and URL of article in first line of summary.")
            item("Include URL and title in format: <TITLE> (<URL TO CLICK>).")
        }
    }

    val agent = AIAgent(
        executor = executor,
        llmModel = llmModel,
        toolRegistry = ToolRegistry {
            tools(webTools)
        },
        systemPrompt = systemPropt
    )

    return agent
}