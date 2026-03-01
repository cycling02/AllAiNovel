package com.cycling.feature.worldbuilding

import com.cycling.domain.model.SettingType
import com.cycling.domain.model.WorldSetting

sealed interface WorldSettingListIntent {
    data object ShowAddDialog : WorldSettingListIntent
    data object HideAddDialog : WorldSettingListIntent
    data class ShowEditDialog(val setting: WorldSetting) : WorldSettingListIntent
    data object HideEditDialog : WorldSettingListIntent
    data class ShowDeleteDialog(val setting: WorldSetting) : WorldSettingListIntent
    data object HideDeleteDialog : WorldSettingListIntent
    data class AddSetting(val setting: WorldSetting) : WorldSettingListIntent
    data class UpdateSetting(val setting: WorldSetting) : WorldSettingListIntent
    data object ConfirmDelete : WorldSettingListIntent
    data class SearchSettings(val query: String) : WorldSettingListIntent
    data class FilterByType(val type: SettingType?) : WorldSettingListIntent
}
