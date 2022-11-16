package com.snackable.testapp

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val fileId: String,
    val processingStatus: ProcessingStatus
)

enum class ProcessingStatus { FINISHED, PROCESSING, FAILED }

@Serializable
data class FullMetadata(
    val details: Details,
    val segments: Collection<Segment>
)

@Serializable
data class Segment(
    val fileSegmentId: Int,
    val fileId: String,
    val segmentText: String,
    val startTime: Int,
    val endTime: Int
)

@Serializable
data class Details(
    val fileId: String,
    val fileName: String,
    val mp3Path: String,
    val originalFilePath: String,
    val fileLength: Int,
    val seriesTitle: String? = null
)