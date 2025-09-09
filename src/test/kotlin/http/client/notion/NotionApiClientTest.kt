package http.client.notion

import CreatePageRequest
import PageParent
import PageProperty
import com.example.http.client.notion.NotionClient
import com.example.http.client.notion.responses.RichText
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class NotionApiClientTest() {
    @Test
    fun shouldQueryDatasource() {
        val notionClient = NotionClient(token = System.getenv("NOTION_API_KEY"))
        val tasksDataSourceId = "26821a96-5632-80d7-be87-000bdd1ac7c0"

        val result = runBlocking {
            notionClient.dataSource.query(tasksDataSourceId)
        }

        println(Json.encodeToString(result))
    }

    @Test
    fun shouldCreatePage() {
        val notionClient = NotionClient(token = System.getenv("NOTION_API_KEY"))
        val tasksDatabaseId = "26821a96563280bab09ee124d6b8d4f4"
        val request = CreatePageRequest(
            parent = PageParent.Database(databaseId = tasksDatabaseId),
            properties = mapOf(
                "Done" to PageProperty.Checkbox(checkbox = false),
                "Task" to PageProperty.Title(
                    title = listOf(
                        RichText(
                            type = RichText.Type.TEXT,
                            text = RichText.TextContent(
                                content = "Task",
                            ),
                            plainText = "Task"
                        )
                    )
                ),
                "Due Date" to PageProperty.Date(
                    date = PageProperty.Date.InnerDate(
                        start = LocalDate.parse("2025-09-10"),
                        end = null
                    )
                )
            ),
        )
        val result = runBlocking {
            notionClient.page.create(request)
        }

        println(Json.encodeToString(result))
    }
}
