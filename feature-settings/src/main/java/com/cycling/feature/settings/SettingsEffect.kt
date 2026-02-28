package com.cycling.feature.settings

sealed interface SettingsEffect {
    data class ShowError(val message: String) : SettingsEffect
    data class ShowSuccess(val message: String) : SettingsEffect
}
