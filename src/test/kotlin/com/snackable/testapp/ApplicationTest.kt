package com.snackable.testapp

import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondOutputStream
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

// sorry this is not working yet
class ApplicationTest {
    @Test
    @Ignore
    fun test() = testApplication {
        val client = createClient {
            install(ClientContentNegotiation) {
                json()
            }
        }

        externalServices {
            hosts("http://interview-api.snackable.ai") {
                routing {
                    get("api/file/all") {
                        call.respond(File("test", ProcessingStatus.FINISHED))
                    }
                    get("api/file/details/test") {
                        call.respond(Details("test", "name", "mp3", "filepath", 123))
                    }
                    get("api/file/details/segments/test") {
                        call.respond(arrayOf(Segment(1, "test", "segment", 2, 3)))
                    }
                }
            }
        }

        application {
            module(client)
        }

        client.get("/api/file/metadata/test").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}