package com.cycling.feature.worldbuilding

sealed interface WorldSettingListEffect {
    data class ShowToast(val message: String) : WorldSettingListEffect
    data class ShowError(val message: String) : WorldSettingListEffect
    data object NavigateBack : WorldSettingListEffect
}
