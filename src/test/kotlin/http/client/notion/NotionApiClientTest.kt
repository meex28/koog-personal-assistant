package http.client.notion

import com.example.http.client.notion.NotionClient
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class NotionApiClientTest() {
    @Test
    fun shouldQueryDatasource() {
        val notionClient = NotionClient(token = System.getenv("NOTION_API_KEY"))
        val tasksDatabaseId = "26821a96-5632-80d7-be87-000bdd1ac7c0"

        val result = runBlocking {
            notionClient.dataSource.query(tasksDatabaseId)
        }

        println(Json.encodeToString(result))
    }
}