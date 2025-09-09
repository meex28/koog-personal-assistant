package http.server

import ai.koog.agents.core.agent.AIAgent
import ai.koog.ktor.Koog
import ai.koog.prompt.dsl.PromptBuilder
import com.example.ai.aiAgentsModule
import com.example.ai.aiConfigurationModule
import com.example.http.server.notionModule
import com.example.http.server.requests.ChatHistory
import com.example.http.server.requests.ChatMessage
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging)
    install(Koog) {
        llm {
            openRouter(apiKey = System.getenv("OPEN_ROUTER_API_KEY"))
        }
    }
    install(Koin) {
        slf4jLogger()
        modules(
            notionModule,
            aiConfigurationModule,
            aiAgentsModule
        )
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)

        allowHeader(HttpHeaders.ContentType)

        anyHost()
    }

    routing {
        route("/ai") {
            post("/chat") {
                val chatHistory = call.receive<ChatHistory>()
                val chatPreviousMessages = chatHistory.messages.subList(0, chatHistory.messages.size - 1)
                val userInput = chatHistory.messages.last()
                require(userInput is ChatMessage.UserChatMessage) {
                    "Last message must be from user!"
                }

                val promptBuilder: PromptBuilder.() -> Unit = {
                    chatPreviousMessages.forEach {
                        when (it) {
                            is ChatMessage.UserChatMessage -> user(it.content)
                            is ChatMessage.AssistantChatMessage -> assistant(it.content)
                            is ChatMessage.SystemChatMessage -> system(it.content)
                        }
                    }
                }
                val agent = get<AIAgent<String, String>>(named("PersonalAiAgent")) {
                    parametersOf(promptBuilder)
                }

                val output = agent.run(userInput.content)

                val response = chatHistory.addMessage(ChatMessage.AssistantChatMessage(output))
                call.respond(response)
            }
        }
    }
}

fun startHttpServer() {
    embeddedServer(
        factory = Netty,
        port = 8080,
        module = Application::module,
    ).start(wait = true)
}
