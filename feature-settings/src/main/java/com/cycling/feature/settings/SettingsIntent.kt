package com.cycling.feature.settings

import com.cycling.domain.model.ApiProvider

sealed interface SettingsIntent {
    data object LoadConfigs : SettingsIntent
    data object ShowAddDialog : SettingsIntent
    data object HideAddDialog : SettingsIntent
    data class SetProvider(val provider: ApiProvider) : SettingsIntent
    data class SetName(val name: String) : SettingsIntent
    data class SetApiKey(val apiKey: String) : SettingsIntent
    data class SetBaseUrl(val baseUrl: String) : SettingsIntent
    data class SetModel(val model: String) : SettingsIntent
    data object SaveConfig : SettingsIntent
    data class SetDefaultConfig(val id: Long) : SettingsIntent
    data object ClearMessage : SettingsIntent
}
