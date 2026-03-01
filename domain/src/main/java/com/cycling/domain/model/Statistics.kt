package com.cycling.domain.model

data class DailyStatistics(
    val date: Long,
    val totalWords: Int,
    val totalDuration: Long,
    val sessionCount: Int
)

data class TotalStatistics(
    val totalWords: Long,
    val totalDuration: Long,
    val totalSessions: Int,
    val totalChapters: Int,
    val totalBooks: Int
)
