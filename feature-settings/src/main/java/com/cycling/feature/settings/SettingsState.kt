package com.cycling.feature.settings

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.ApiProvider

data class SettingsState(
    val isLoading: Boolean = false,
    val configs: List<ApiConfig> = emptyList(),
    val showAddDialog: Boolean = false,
    val editingConfig: ApiConfig? = null,
    val name: String = "",
    val apiKey: String = "",
    val baseUrl: String = "",
    val model: String = "",
    val provider: ApiProvider = ApiProvider.DEEPSEEK
) {
    val canSave: Boolean
        get() = !isLoading && name.isNotBlank() && apiKey.isNotBlank()
}
