package com.cycling.domain.model

data class WritingSession(
    val id: Long = 0,
    val bookId: Long,
    val chapterId: Long,
    val startWordCount: Int,
    val endWordCount: Int,
    val startTime: Long,
    val endTime: Long,
    val duration: Long
) {
    val wordsWritten: Int
        get() = endWordCount - startWordCount
}
