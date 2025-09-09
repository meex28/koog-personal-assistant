package com.example.ai

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.agent.singleRunStrategy
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.prompt.dsl.PromptBuilder
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.markdown.markdown
import ai.koog.prompt.text.text
import com.example.ai.tools.TasksManagementToolset
import com.example.http.client.notion.NotionClient

fun personalAiAgent(
    notionClient: NotionClient,
    executor: PromptExecutor,
    llmModel: LLModel,
    additionalTools: ToolRegistry.Builder.() -> Unit,
    buildPrompt: PromptBuilder.() -> Unit
): AIAgent<String, String> {
    val tasksManagementTools = TasksManagementToolset(
        notionClient = notionClient,
        tasksDataSourceId = "26821a96-5632-80d7-be87-000bdd1ac7c0",
        tasksDatabaseId = "26821a96563280bab09ee124d6b8d4f4"
    )

    val systemPropt = markdown {
        h1("Context")

        +text {
            +"You are a helpful assistant helping user to work with his tasks."
        }
    }

    val agent = AIAgent(
        promptExecutor = executor,
        strategy = singleRunStrategy(),
        agentConfig = AIAgentConfig(
            prompt = prompt("chat") {
                system(systemPropt)
                buildPrompt()
            },
            model = llmModel,
            maxAgentIterations = 10
        ),
        toolRegistry = ToolRegistry {
            tools(tasksManagementTools)
            additionalTools()
        },
    )

    return agent
}
