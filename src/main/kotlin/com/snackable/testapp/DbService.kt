package com.snackable.testapp

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class DbService {
    companion object {
        private val dbclient = KMongo.createClient().coroutine
        private val database = dbclient.getDatabase("snackable-test")
        private val collection = database.getCollection<File>()

        suspend fun addFinishedFilesToMongo(httpClient: HttpClient) {
            var offset = 0
            var response: Collection<File>

            do {
                response = httpClient.get("http://interview-api.snackable.ai/api/file/all?offset=$offset").body()
                val finishedFiles = response.filter { file -> file.processingStatus == ProcessingStatus.FINISHED }

                if (finishedFiles.isNotEmpty()) {
                    collection.insertMany(finishedFiles)
                }
                offset += 5
            } while (response.isNotEmpty())

        }
    }
}