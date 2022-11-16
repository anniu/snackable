package com.snackable.testapp

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation

fun main() {
    val client = HttpClient(CIO) {
        install(ClientContentNegotiation) {
            json()
        }
    }

    runBlocking {
        DbService.addFinishedFilesToMongo(client)
    }

    embeddedServer(Netty, port = 8080) {
        module(client)
    }.start(wait = true)
}

fun Application.module(client: HttpClient) {
    install(ServerContentNegotiation) {
        json()
    }
    configureRouting(client)
}
