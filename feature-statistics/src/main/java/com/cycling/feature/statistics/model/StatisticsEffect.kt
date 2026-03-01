package com.cycling.feature.statistics.model

sealed interface StatisticsEffect {
    data class ShowError(val message: String) : StatisticsEffect
}
