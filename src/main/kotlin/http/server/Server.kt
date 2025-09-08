package http.server

import ai.koog.ktor.Koog
import ai.koog.ktor.llm
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.openrouter.OpenRouterModels
import com.example.http.server.requests.ChatRequest
import com.example.http.server.requests.ChatResponse
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
                val request = call.receive<ChatRequest>()
                val apiKey = System.getenv("OPEN_ROUTER_API_KEY")

                val output = llm().execute(
                    prompt = prompt("chat") {
                        user(request.message)
                    },
                    model = OpenRouterModels.GPT5Mini
                )
//                val agent = articlesAiAgent(
//                    executor = simpleOpenRouterExecutor(apiKey),
//                    llmModel = OpenRouterModels.GPT5Mini,
//                )
//
//                val output = agent.run(request.message)

                call.respond(ChatResponse(message = output.last().content))
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
