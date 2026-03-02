package com.cycling.feature.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cycling.core.ui.components.*
import com.cycling.feature.settings.SettingsViewModel
import com.cycling.feature.settings.ui.components.AddConfigDialog
import com.cycling.feature.settings.ui.components.ApiConfigItem

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is com.cycling.feature.settings.SettingsEffect.ShowError -> {
                    snackbarHostState.showSnackbar(it.message)
                    viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.ClearMessage)
                }
                is com.cycling.feature.settings.SettingsEffect.ShowSuccess -> {
                    snackbarHostState.showSnackbar(it.message)
                    viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.ClearMessage)
                }
            }
        }
    }
    
    // 底部导航栏页面不使用 Scaffold，由 AppNavigation 统一管理
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部栏
        IOSLargeNavBar(title = "设置")
        
        // 内容区域
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.configs.isEmpty()) {
                IOSEmptyState(
                    icon = Icons.Outlined.Api,
                    title = "还没有API配置",
                    message = "添加AI模型API配置以使用AI写作功能",
                    action = {
                        IOSButton(
                            text = "添加配置",
                            onClick = { 
                                viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.ShowAddDialog) 
                            },
                            icon = Icons.Default.Add,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                    }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    iosSection(title = "API配置") {
                        state.configs.forEach { config ->
                            ApiConfigItem(
                                config = config,
                                onSetDefault = { 
                                    viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.SetDefaultConfig(config.id)) 
                                }
                            )
                        }
                    }
                    
                    iosSpacer(height = IOSSpacing.xl)
                }
            }
            
            // FAB
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(IOSSpacing.lg)
            ) {
                IOSFAB(onClick = { 
                    viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.ShowAddDialog) 
                })
            }
        }
    }
    
    if (state.showAddDialog) {
        AddConfigDialog(
            state = state,
            onDismiss = { 
                viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.HideAddDialog) 
            },
            onNameChange = { 
                viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.SetName(it)) 
            },
            onApiKeyChange = { 
                viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.SetApiKey(it)) 
            },
            onBaseUrlChange = { 
                viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.SetBaseUrl(it)) 
            },
            onModelChange = { 
                viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.SetModel(it)) 
            },
            onProviderChange = { 
                viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.SetProvider(it)) 
            },
            onSave = { 
                viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.SaveConfig) 
            }
        )
    }
}
