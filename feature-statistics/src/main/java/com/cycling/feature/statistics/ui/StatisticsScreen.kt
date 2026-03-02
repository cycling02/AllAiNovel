package com.cycling.feature.statistics.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cycling.core.ui.components.*
import com.cycling.domain.model.DailyStatistics
import com.cycling.feature.statistics.model.StatisticsIntent
import com.cycling.feature.statistics.viewmodel.StatisticsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // 底部导航栏页面不使用 Scaffold，由 AppNavigation 统一管理
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部栏
        IOSNavBar(title = "写作统计")
        
        // 内容区域
        if (state.isLoading) {
            IOSFullScreenLoading()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
            ) {
                item(key = "today_stats") {
                    TodayStatsCard(
                        wordsWritten = state.todayStats.wordsWritten,
                        duration = state.todayStats.duration,
                        sessionCount = state.todayStats.sessionCount
                    )
                }

                item(key = "total_stats") {
                    TotalStatsCard(
                        totalWords = state.totalStats.totalWords,
                        totalDuration = state.totalStats.totalDuration,
                        totalSessions = state.totalStats.totalSessions,
                        totalChapters = state.totalStats.totalChapters,
                        totalBooks = state.totalStats.totalBooks
                    )
                }

                item(key = "day_filter") {
                    DayFilterChip(
                        selectedDays = state.selectedDays,
                        onDaysSelected = { viewModel.handleIntent(StatisticsIntent.SelectDays(it)) }
                    )
                }

                item(key = "trend_title") {
                    IOSSectionHeader(title = "字数趋势")
                }

                items(state.dailyStatistics, key = { it.date }) { dailyStats ->
                    DailyStatisticsItem(dailyStats = dailyStats)
                }

                if (state.dailyStatistics.isEmpty()) {
                    item(key = "empty_state") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(IOSSpacing.xxxl),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "暂无写作记录",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
                
                item {
                    IOSSpacer(height = IOSSpacing.xxl)
                }
            }
        }
    }
}

@Composable
private fun TodayStatsCard(
    wordsWritten: Int,
    duration: Long,
    sessionCount: Int
) {
    IOSCard(
        modifier = Modifier.padding(horizontal = IOSSpacing.lg)
    ) {
        Column(
            modifier = Modifier.padding(IOSSpacing.lg)
        ) {
            Text(
                text = "今日统计",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            IOSSpacer(height = IOSSpacing.lg)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Edit,
                    label = "字数",
                    value = "${wordsWritten}字"
                )
                StatItem(
                    icon = Icons.Default.Schedule,
                    label = "时长",
                    value = formatDuration(duration)
                )
                StatItem(
                    icon = Icons.Default.AutoStories,
                    label = "次数",
                    value = "${sessionCount}次"
                )
            }
        }
    }
}

@Composable
private fun TotalStatsCard(
    totalWords: Long,
    totalDuration: Long,
    totalSessions: Int,
    totalChapters: Int,
    totalBooks: Int
) {
    IOSCard(
        modifier = Modifier.padding(horizontal = IOSSpacing.lg)
    ) {
        Column(
            modifier = Modifier.padding(IOSSpacing.lg)
        ) {
            Text(
                text = "累计统计",
                style = MaterialTheme.typography.titleMedium
            )
            IOSSpacer(height = IOSSpacing.lg)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Edit,
                    label = "总字数",
                    value = "${totalWords}字"
                )
                StatItem(
                    icon = Icons.Default.Schedule,
                    label = "总时长",
                    value = formatDuration(totalDuration)
                )
            }
            IOSSpacer(height = IOSSpacing.md)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.AutoStories,
                    label = "章节",
                    value = "${totalChapters}章"
                )
                StatItem(
                    icon = Icons.Default.AutoStories,
                    label = "书籍",
                    value = "${totalBooks}本"
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(IOSSize.iconMd)
        )
        IOSSpacer(height = IOSSpacing.xs)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun DayFilterChip(
    selectedDays: Int,
    onDaysSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = IOSSpacing.lg),
        horizontalArrangement = Arrangement.spacedBy(IOSSpacing.sm)
    ) {
        IOSCompactButton(
            text = "近7天",
            onClick = { onDaysSelected(7) },
            style = if (selectedDays == 7) IOSButtonStyle.Primary else IOSButtonStyle.Secondary
        )
        IOSCompactButton(
            text = "近30天",
            onClick = { onDaysSelected(30) },
            style = if (selectedDays == 30) IOSButtonStyle.Primary else IOSButtonStyle.Secondary
        )
    }
}

@Composable
private fun DailyStatisticsItem(dailyStats: DailyStatistics) {
    IOSCard(
        modifier = Modifier.padding(horizontal = IOSSpacing.lg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(IOSSpacing.lg),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = formatDate(dailyStats.date),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${dailyStats.sessionCount}次写作",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${dailyStats.totalWords}字",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = formatDuration(dailyStats.totalDuration),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

private fun formatDuration(milliseconds: Long): String {
    val hours = milliseconds / 3600000
    val minutes = (milliseconds % 3600000) / 60000
    
    return when {
        hours > 0 -> "${hours}小时${minutes}分钟"
        minutes > 0 -> "${minutes}分钟"
        else -> "少于1分钟"
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MM月dd日 EEEE", Locale.CHINA)
    return sdf.format(Date(timestamp))
}
