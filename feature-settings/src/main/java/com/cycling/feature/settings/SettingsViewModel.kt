package com.cycling.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.ApiProvider
import com.cycling.domain.usecase.apiconfig.GetAllApiConfigsUseCase
import com.cycling.domain.usecase.apiconfig.SaveApiConfigUseCase
import com.cycling.domain.usecase.apiconfig.SetDefaultApiConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getAllApiConfigsUseCase: GetAllApiConfigsUseCase,
    private val saveApiConfigUseCase: SaveApiConfigUseCase,
    private val setDefaultApiConfigUseCase: SetDefaultApiConfigUseCase
) : ViewModel() {
    
    private val configsFlow = getAllApiConfigsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()
    
    private val _effect = MutableStateFlow<SettingsEffect?>(null)
    val effect: StateFlow<SettingsEffect?> = _effect.asStateFlow()
    
    init {
        loadConfigs()
    }
    
    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.LoadConfigs -> loadConfigs()
            is SettingsIntent.ShowAddDialog -> showAddDialog()
            is SettingsIntent.HideAddDialog -> hideAddDialog()
            is SettingsIntent.SetProvider -> setProvider(intent.provider)
            is SettingsIntent.SetName -> setName(intent.name)
            is SettingsIntent.SetApiKey -> setApiKey(intent.apiKey)
            is SettingsIntent.SetBaseUrl -> setBaseUrl(intent.baseUrl)
            is SettingsIntent.SetModel -> setModel(intent.model)
            is SettingsIntent.SaveConfig -> saveConfig()
            is SettingsIntent.SetDefaultConfig -> setDefaultConfig(intent.id)
            is SettingsIntent.ClearMessage -> clearMessage()
        }
    }
    
    private fun loadConfigs() {
        viewModelScope.launch {
            configsFlow.collect { configs ->
                _state.value = _state.value.copy(configs = configs)
            }
        }
    }
    
    private fun showAddDialog() {
        _state.value = _state.value.copy(
            showAddDialog = true,
            baseUrl = ApiProvider.DEEPSEEK.defaultBaseUrl,
            model = ApiProvider.DEEPSEEK.defaultModel
        )
    }
    
    private fun hideAddDialog() {
        _state.value = SettingsState(configs = _state.value.configs)
    }
    
    private fun setProvider(provider: ApiProvider) {
        _state.value = _state.value.copy(
            provider = provider,
            baseUrl = provider.defaultBaseUrl,
            model = provider.defaultModel
        )
    }
    
    private fun setName(name: String) {
        _state.value = _state.value.copy(name = name)
    }
    
    private fun setApiKey(apiKey: String) {
        _state.value = _state.value.copy(apiKey = apiKey)
    }
    
    private fun setBaseUrl(baseUrl: String) {
        _state.value = _state.value.copy(baseUrl = baseUrl)
    }
    
    private fun setModel(model: String) {
        _state.value = _state.value.copy(model = model)
    }
    
    private fun saveConfig() {
        viewModelScope.launch {
            val state = _state.value
            
            if (state.name.isBlank()) {
                _effect.value = SettingsEffect.ShowError("请输入配置名称")
                return@launch
            }
            
            if (state.apiKey.isBlank()) {
                _effect.value = SettingsEffect.ShowError("请输入API Key")
                return@launch
            }
            
            _state.value = state.copy(isLoading = true)
            
            val config = ApiConfig(
                name = state.name.trim(),
                provider = state.provider.name,
                apiKey = state.apiKey.trim(),
                baseUrl = state.baseUrl.trim(),
                model = state.model.trim(),
                isDefault = state.configs.isEmpty()
            )
            
            val id = saveApiConfigUseCase(config)
            
            if (state.configs.isEmpty()) {
                setDefaultApiConfigUseCase(id)
            }
            
            _state.value = SettingsState(configs = state.configs + config.copy(id = id))
            _effect.value = SettingsEffect.ShowSuccess("配置已保存")
        }
    }
    
    private fun setDefaultConfig(id: Long) {
        viewModelScope.launch {
            setDefaultApiConfigUseCase(id)
            _effect.value = SettingsEffect.ShowSuccess("已设为默认配置")
        }
    }
    
    private fun clearMessage() {
        _effect.value = null
    }
}
