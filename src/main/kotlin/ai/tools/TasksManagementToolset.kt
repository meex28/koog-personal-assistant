package com.example.ai.tools

import PageProperty
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.example.http.client.notion.NotionClient

@LLMDescription("A toolset to manage tasks of the user")
class TasksManagementToolset(
    private val notionClient: NotionClient,
    private val tasksDatabaseId: String
) : ToolSet {
    @Tool
    @LLMDescription("Get all tasks of the user")
    suspend fun fetchTasks(): String {
        val response = notionClient.dataSource.query(dataSourceId = tasksDatabaseId)
        return response.results.joinToString(separator = "\n") {
            val done = it.properties["Done"]!!.toLlmString()
            val task = it.properties["Task"]!!.toLlmString()
            val dueDate = it.properties["Due Date"]!!.toLlmString()
            "- Task: $task (done: $done, due date: $dueDate)"
        }
    }

    // TODO: get all tasks
    // TODO: get tasks with filters applied (?)
    // TODO: add task
    // TODO: set task as done
}

fun PageProperty.toLlmString(): String {
    return when (this) {
        is PageProperty.Checkbox -> this.toLlmString()
        is PageProperty.Date -> this.toLlmString()
        is PageProperty.RichText -> this.richText.joinToString("\n") { it.plainText }
        is PageProperty.Title -> this.title.joinToString("\n") { it.plainText }
        is PageProperty.Url -> this.url
    }
}

fun PageProperty.Checkbox.toLlmString() = if (this.checkbox) {
    "Yes"
} else {
    "No"
}

fun PageProperty.Date.toLlmString() = if (this.date.end == null) {
    this.date.start.toString()
} else {
    "${this.date.start} - ${this.date.end}"
}