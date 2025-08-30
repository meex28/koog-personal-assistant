package tools

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import it.skrape.core.htmlDocument
import it.skrape.fetcher.BrowserFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.body

@LLMDescription("A toolset for web operations")
class WebToolset : ToolSet {
    @Tool
    @LLMDescription("Reads the content of a website by URL")
    fun readWebsiteContent(
        @LLMDescription("The URL of the website to read")
        websiteUrl: String
    ): String {
        return skrape(BrowserFetcher) {
            request {
                url = websiteUrl
            }

            response {
                htmlDocument {
                    body {
                        this.findAll("").joinToString {
                            it.text
                        }
                    }
                }
            }
        }
    }
}