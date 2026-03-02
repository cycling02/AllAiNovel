package com.cycling.feature.tools.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cycling.core.ui.components.*

data class ToolItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    onNavigateToInspiration: () -> Unit,
    onNavigateToNameGenerator: () -> Unit,
    onNavigateToPromptManagement: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val tools = listOf(
        ToolItem(
            title = "灵感收集",
            description = "快速记录创作灵感，支持标签分类管理",
            icon = Icons.Default.Lightbulb,
            onClick = onNavigateToInspiration
        ),
        ToolItem(
            title = "名字生成器",
            description = "生成中文姓名、地名、势力名等",
            icon = Icons.Default.AutoAwesome,
            onClick = onNavigateToNameGenerator
        ),
        ToolItem(
            title = "提示词管理",
            description = "管理AI写作提示词，支持自定义模板",
            icon = Icons.Default.EditNote,
            onClick = onNavigateToPromptManagement
        )
    )

    // 底部导航栏页面不使用 Scaffold，由 AppNavigation 统一管理
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部栏
        IOSNavBar(title = "写作工具")
        
        // 内容区域
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = IOSSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
        ) {
            item {
                IOSSectionHeader(title = "工具列表")
            }
            
            items(tools, key = { it.title }) { tool ->
                IOSListItemCard(
                    icon = tool.icon,
                    title = tool.title,
                    subtitle = tool.description,
                    onClick = tool.onClick
                )
            }
            
            item {
                IOSSpacer(height = IOSSpacing.xl)
            }
        }
    }
}
