package com.cycling.core.database.mapper

import com.cycling.core.database.entity.OutlineItemEntity
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.OutlineStatus

fun OutlineItemEntity.toModel(): OutlineItem {
    return OutlineItem(
        id = id,
        bookId = bookId,
        parentId = parentId,
        chapterId = chapterId,
        title = title,
        summary = summary,
        level = level,
        sortOrder = sortOrder,
        status = runCatching { OutlineStatus.valueOf(status) }.getOrDefault(OutlineStatus.PENDING),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun OutlineItem.toEntity(): OutlineItemEntity {
    return OutlineItemEntity(
        id = id,
        bookId = bookId,
        parentId = parentId,
        chapterId = chapterId,
        title = title,
        summary = summary,
        level = level,
        sortOrder = sortOrder,
        status = status.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
