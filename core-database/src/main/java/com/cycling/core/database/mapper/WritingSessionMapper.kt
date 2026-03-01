package com.cycling.core.database.mapper

import com.cycling.core.database.entity.WritingSessionEntity
import com.cycling.domain.model.WritingSession

fun WritingSessionEntity.toModel(): WritingSession {
    return WritingSession(
        id = id,
        bookId = bookId,
        chapterId = chapterId,
        startWordCount = startWordCount,
        endWordCount = endWordCount,
        startTime = startTime,
        endTime = endTime,
        duration = duration
    )
}

fun WritingSession.toEntity(): WritingSessionEntity {
    return WritingSessionEntity(
        id = id,
        bookId = bookId,
        chapterId = chapterId,
        startWordCount = startWordCount,
        endWordCount = endWordCount,
        startTime = startTime,
        endTime = endTime,
        duration = duration
    )
}
