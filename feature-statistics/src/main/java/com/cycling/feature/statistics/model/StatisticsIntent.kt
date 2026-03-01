package com.cycling.feature.statistics.model

sealed interface StatisticsIntent {
    data class SelectDays(val days: Int) : StatisticsIntent
    data object Refresh : StatisticsIntent
    data object ClearError : StatisticsIntent
}
