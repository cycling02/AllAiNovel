package com.cycling.feature.ai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cycling.feature.ai.AiWritingIntent
import com.cycling.feature.ai.AiWritingMode
import com.cycling.feature.ai.AiWritingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiWritingScreen(
    initialContext: String = "",
    onNavigateBack: () -> Unit,
    onApplyContent: (String) -> Unit,
    viewModel: AiWritingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val effect = viewModel.effect
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (initialContext.isNotEmpty()) {
            viewModel.onIntent(AiWritingIntent.UpdateContext(initialContext))
        }
    }

    LaunchedEffect(effect) {
        effect.collect { effect ->
            when (effect) {
                is com.cycling.feature.ai.AiWritingEffect.ShowToast -> {
                }
                is com.cycling.feature.ai.AiWritingEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is com.cycling.feature.ai.AiWritingEffect.ApplyContentToEditor -> {
                    onApplyContent(effect.content)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI 写作助手") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ModeSelector(
                selectedMode = state.selectedMode,
                onModeSelected = { mode ->
                    viewModel.onIntent(AiWritingIntent.SelectMode(mode))
                }
            )

            OutlinedTextField(
                value = state.context,
                onValueChange = { viewModel.onIntent(AiWritingIntent.UpdateContext(it)) },
                label = { Text("上下文内") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 5,
                maxLines = 10,
                enabled = !state.isLoading
            )

            Button(
                onClick = { viewModel.onIntent(AiWritingIntent.GenerateContent()) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.context.isNotBlank() && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(getGenerateButtonText(state.selectedMode))
            }

            if (state.generatedResult.isNotEmpty()) {
                ResultCard(
                    result = state.generatedResult,
                    onApply = { viewModel.onIntent(AiWritingIntent.ApplyResult(it)) },
                    onRegenerate = { viewModel.onIntent(AiWritingIntent.Regenerate()) },
                    onClear = { viewModel.onIntent(AiWritingIntent.ClearResult) },
                    isLoading = state.isLoading
                )
            }

            state.error?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.onIntent(AiWritingIntent.DismissError) }) {
                            Text("关闭")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModeSelector(
    selectedMode: AiWritingMode,
    onModeSelected: (AiWritingMode) -> Unit
) {
    Column {
        Text(
            text = "写作模式",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AiWritingMode.values().forEach { mode ->
                FilterChip(
                    selected = selectedMode == mode,
                    onClick = { onModeSelected(mode) },
                    label = { Text(getModeText(mode)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ResultCard(
    result: String,
    onApply: (String) -> Unit,
    onRegenerate: () -> Unit,
    onClear: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "生成结果",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onRegenerate,
                        enabled = !isLoading
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "重新生成")
                    }
                    TextButton(onClick = onClear) {
                        Text("清除")
                    }
                }
            }

            Divider()

            Text(
                text = result,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .verticalScroll(rememberScrollState())
            )

            Button(
                onClick = { onApply(result) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("应用内容")
            }
        }
    }
}

fun getModeText(mode: AiWritingMode): String {
    return when (mode) {
        AiWritingMode.CONTINUE -> "续写"
        AiWritingMode.REWRITE -> "改写"
        AiWritingMode.EXPAND -> "扩写"
        AiWritingMode.POLISH -> "润色"
    }
}

fun getGenerateButtonText(mode: AiWritingMode): String {
    return when (mode) {
        AiWritingMode.CONTINUE -> "开始续"
        AiWritingMode.REWRITE -> "开始改"
        AiWritingMode.EXPAND -> "开始扩"
        AiWritingMode.POLISH -> "开始润"
    }
}
