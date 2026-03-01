package com.cycling.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "writing_sessions")
data class WritingSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Long,
    val chapterId: Long,
    val startWordCount: Int,
    val endWordCount: Int,
    val startTime: Long,
    val endTime: Long,
    val duration: Long
)
