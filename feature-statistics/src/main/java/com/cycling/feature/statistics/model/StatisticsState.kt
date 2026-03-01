package com.cycling.feature.statistics.model

import com.cycling.domain.model.DailyStatistics
import com.cycling.domain.model.TotalStatistics
import com.cycling.domain.usecase.writingsession.GetTodayStatisticsUseCase

data class StatisticsState(
    val isLoading: Boolean = true,
    val todayStats: GetTodayStatisticsUseCase.TodayStats = GetTodayStatisticsUseCase.TodayStats(0, 0, 0),
    val totalStats: TotalStatistics = TotalStatistics(0, 0, 0, 0, 0),
    val dailyStatistics: List<DailyStatistics> = emptyList(),
    val selectedDays: Int = 7,
    val error: String? = null
)
