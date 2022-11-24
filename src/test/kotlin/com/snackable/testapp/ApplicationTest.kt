package com.snackable.testapp

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

class ApplicationTest {
    @Test
    fun test() = testApplication {
        val client = createClient {
            install(ClientContentNegotiation) {
                json()
            }
        }

        application {
            module(client)
        }

        externalServices {
            hosts("http://interview-api.snackable.ai") {
                install(ServerContentNegotiation) {
                    json()
                }
                routing {
                    get("api/file/all") {
                        call.respond(listOf(File("test", ProcessingStatus.FINISHED)))
                    }
                    get("api/file/details/test") {
                        call.respond(Details("test", "name", "mp3", "filepath", 123))
                    }
                    get("api/file/segments/test") {
                        call.respond(listOf(Segment(1, "test", "segment", 2, 3)))
                    }
                }
            }
        }

        client.get("/api/file/metadata/test").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}