package com.cycling.feature.worldbuilding

import com.cycling.domain.model.SettingType
import com.cycling.domain.model.WorldSetting

data class WorldSettingListState(
    val settings: List<WorldSetting> = emptyList(),
    val filteredSettings: List<WorldSetting> = emptyList(),
    val isLoading: Boolean = false,
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val settingToEdit: WorldSetting? = null,
    val settingToDelete: WorldSetting? = null,
    val selectedType: SettingType? = null,
    val searchQuery: String = "",
    val error: String? = null
) {
    val displaySettings: List<WorldSetting>
        get() = when {
            searchQuery.isNotBlank() -> filteredSettings
            selectedType != null -> settings.filter { it.type == selectedType }
            else -> settings
        }
    
    val isEmpty: Boolean
        get() = displaySettings.isEmpty() && !isLoading
}
