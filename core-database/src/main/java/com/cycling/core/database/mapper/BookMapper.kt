package com.cycling.core.database.mapper

import com.cycling.core.database.entity.BookEntity
import com.cycling.domain.model.Book
import com.cycling.domain.model.BookStatus

fun BookEntity.toModel(): Book {
    return Book(
        id = id,
        title = title,
        author = author,
        description = description,
        coverPath = coverPath,
        status = runCatching { BookStatus.valueOf(status) }.getOrDefault(BookStatus.DRAFT),
        wordCount = wordCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Book.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        author = author,
        description = description,
        coverPath = coverPath,
        genre = "",
        status = status.name,
        wordCount = wordCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
