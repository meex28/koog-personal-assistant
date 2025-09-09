package com.example.ai.tools

import CreatePageRequest
import PageParent
import PageProperty
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import ai.koog.prompt.markdown.markdown
import com.example.http.client.notion.NotionClient
import com.example.http.client.notion.responses.RichText
import kotlinx.datetime.LocalDate

@LLMDescription("A toolset to manage tasks of the user")
class TasksManagementToolset(
    private val notionClient: NotionClient,
    private val tasksDataSourceId: String,
    private val tasksDatabaseId: String,
) : ToolSet {
    @Tool
    @LLMDescription("Get all tasks of the user")
    // TODO: get tasks with filters applied (?)
    suspend fun fetchTasks(): String {
        val response = notionClient.dataSource.query(dataSourceId = tasksDataSourceId)
        return markdown {
            bulleted {
                response.results.forEach {
                    val done = it.properties["Done"]!!.toLlmString()
                    val task = it.properties["Task"]!!.toLlmString()
                    val dueDate = it.properties["Due Date"]!!.toLlmString()

                    item("Task: $task (done: $done, due date: $dueDate)")
                }
            }
        }
    }

    @Tool
    @LLMDescription("Create a new task for the user")
    suspend fun createTask(
        @LLMDescription("Task - what needs to be done (example: Create a Jira ticket to fix the pagination bug)")
        task: String,
        @LLMDescription("Due date of the task in ISO format (example: 2024-09-20)")
        dueDate: String,
    ) {
        val request = CreatePageRequest(
            parent = PageParent.Database(databaseId = tasksDatabaseId),
            properties = mapOf(
                "Done" to PageProperty.Checkbox(checkbox = false),
                "Task" to PageProperty.Title(
                    title = listOf(
                        RichText(
                            type = RichText.Type.TEXT,
                            text = RichText.TextContent(
                                content = task,
                            ),
                            plainText = task
                        )
                    )
                ),
                "Due Date" to PageProperty.Date(
                    date = PageProperty.Date.InnerDate(
                        start = LocalDate.parse(dueDate),
                        end = null
                    )
                )
            ),
        )
        notionClient.page.create(request)
    }

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