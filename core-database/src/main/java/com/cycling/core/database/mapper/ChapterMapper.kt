package com.cycling.core.database.mapper

import com.cycling.core.database.entity.ChapterEntity
import com.cycling.domain.model.Chapter

fun ChapterEntity.toModel(): Chapter {
    return Chapter(
        id = id,
        bookId = bookId,
        outlineItemId = outlineItemId,
        title = title,
        content = content,
        chapterNumber = chapterNumber,
        wordCount = wordCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Chapter.toEntity(): ChapterEntity {
    return ChapterEntity(
        id = id,
        bookId = bookId,
        outlineItemId = outlineItemId,
        title = title,
        content = content,
        chapterNumber = chapterNumber,
        wordCount = wordCount,
        status = "DRAFT",
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
