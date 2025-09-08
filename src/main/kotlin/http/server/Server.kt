package http.server

import ai.koog.ktor.Koog
import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import ai.koog.prompt.executor.llms.all.simpleOpenRouterExecutor
import com.example.ai.articlesAiAgent
import com.example.http.server.requests.ChatRequest
import com.example.http.server.requests.ChatResponse
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging)
    install(Koog)

    routing {
        route("/ai") {
            post("/chat") {
                val request = call.receive<ChatRequest>()
                val apiKey = System.getenv("OPEN_ROUTER_API_KEY")
                val agent = articlesAiAgent(
                    executor = simpleOpenRouterExecutor(apiKey),
                    llmModel = OpenRouterModels.GPT5Mini,
                )

                val output = agent.run(request.message)

                call.respond(ChatResponse(message = output))
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
