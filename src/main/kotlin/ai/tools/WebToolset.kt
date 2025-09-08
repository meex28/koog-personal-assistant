package tools

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking

@LLMDescription("A toolset for web operations")
class WebToolset : ToolSet {
    @Tool
    @LLMDescription("Reads the content of a website by URL")
    fun readWebsiteContent(
        @LLMDescription("The URL of the website to read")
        websiteUrl: String
    ): String {
        val client = HttpClient(CIO) // TODO: centralize http client
        return runBlocking {
            client.get {
                url("https://urltomarkdown.herokuapp.com")
                parameter("url", websiteUrl)
                parameter("title", true)
                parameter("clean", true)
                parameter("links", true)
            }.bodyAsText()
        }
    }
}