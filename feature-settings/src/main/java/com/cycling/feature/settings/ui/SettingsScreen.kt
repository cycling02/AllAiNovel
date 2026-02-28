package com.cycling.feature.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cycling.feature.settings.SettingsViewModel
import com.cycling.feature.settings.ui.components.AddConfigDialog
import com.cycling.feature.settings.ui.components.ApiConfigItem

@OptIn(ExperimentalMaterial3Api::class)
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
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.ShowAddDialog) 
            }) {
                Icon(Icons.Default.Add, contentDescription = "添加配置")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.configs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Api,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "还没有API配置",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击右下角按钮添加配置",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.configs, key = { it.id }) { config ->
                    ApiConfigItem(
                        config = config,
                        onSetDefault = { 
                            viewModel.onIntent(com.cycling.feature.settings.SettingsIntent.SetDefaultConfig(config.id)) 
                        }
                    )
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
}
