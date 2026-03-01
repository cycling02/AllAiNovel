package com.cycling.feature.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.usecase.writingsession.GetDailyStatisticsUseCase
import com.cycling.domain.usecase.writingsession.GetTodayStatisticsUseCase
import com.cycling.domain.usecase.writingsession.GetTotalStatisticsUseCase
import com.cycling.feature.statistics.model.StatisticsEffect
import com.cycling.feature.statistics.model.StatisticsIntent
import com.cycling.feature.statistics.model.StatisticsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getTodayStatisticsUseCase: GetTodayStatisticsUseCase,
    private val getTotalStatisticsUseCase: GetTotalStatisticsUseCase,
    private val getDailyStatisticsUseCase: GetDailyStatisticsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StatisticsEffect>()
    val effect: SharedFlow<StatisticsEffect> = _effect.asSharedFlow()

    init {
        loadStatistics()
    }

    fun handleIntent(intent: StatisticsIntent) {
        when (intent) {
            is StatisticsIntent.SelectDays -> selectDays(intent.days)
            is StatisticsIntent.Refresh -> loadStatistics()
            is StatisticsIntent.ClearError -> clearError()
        }
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                launch {
                    getTodayStatisticsUseCase().collect { todayStats ->
                        _state.update { it.copy(todayStats = todayStats) }
                    }
                }

                launch {
                    getTotalStatisticsUseCase().collect { totalStats ->
                        _state.update { it.copy(totalStats = totalStats, isLoading = false) }
                    }
                }

                loadDailyStatistics(_state.value.selectedDays)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.emit(StatisticsEffect.ShowError(e.message ?: "加载统计数据失败"))
            }
        }
    }

    private fun selectDays(days: Int) {
        _state.update { it.copy(selectedDays = days) }
        loadDailyStatistics(days)
    }

    private fun loadDailyStatistics(days: Int) {
        viewModelScope.launch {
            getDailyStatisticsUseCase(days).collect { dailyStats ->
                _state.update { it.copy(dailyStatistics = dailyStats) }
            }
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
