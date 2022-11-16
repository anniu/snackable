package com.example

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(client: HttpClient) {
    routing {
        get("/api/file/metadata/{snackableFileId}") {
            val snackableFileId = call.parameters["snackableFileId"]

            var offset = 0
            var response: Collection<File>

            do {
                response = client.get("http://interview-api.snackable.ai/api/file/all?offset=$offset").body()
                offset += 5
            } while (!response.any { file -> file.fileId == snackableFileId } && response.isNotEmpty())

            val file = response.find { file -> file.fileId == snackableFileId }
                ?: return@get call.respondText("File with given id not found", status = HttpStatusCode.NotFound)

            if (file.processingStatus in listOf(ProcessingStatus.PROCESSING, ProcessingStatus.FAILED)) {
                return@get call.respondText(
                    "File has status ${file.processingStatus}",
                    status = HttpStatusCode.BadRequest
                )
            }

            val fileDetails: Details = client.get("http://interview-api.snackable.ai/api/file/details/$snackableFileId").body()
            val fileSegments: Collection<Segment> = client.get("http://interview-api.snackable.ai/api/file/segments/$snackableFileId").body()

            val fullMetadata = FullMetadata(
                fileDetails,
                fileSegments
            )

            call.respond(fullMetadata)
        }
        get("test") {
            val response = client.get("http://interview-api.snackable.ai/api/file/all?offset=0")

            println(response)

            call.respondText("asd", status = HttpStatusCode.OK)
        }
    }
}