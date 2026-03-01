package com.cycling.feature.tools.inspiration

import com.cycling.domain.model.Inspiration

sealed interface InspirationListIntent {
    data object LoadInspirations : InspirationListIntent
    data object ShowAddDialog : InspirationListIntent
    data object HideAddDialog : InspirationListIntent
    data class ShowEditDialog(val inspiration: Inspiration) : InspirationListIntent
    data object HideEditDialog : InspirationListIntent
    data class ShowDeleteDialog(val inspiration: Inspiration) : InspirationListIntent
    data object HideDeleteDialog : InspirationListIntent
    data class AddInspiration(val inspiration: Inspiration) : InspirationListIntent
    data class UpdateInspiration(val inspiration: Inspiration) : InspirationListIntent
    data object ConfirmDelete : InspirationListIntent
    data class SearchInspirations(val query: String) : InspirationListIntent
    data class FilterByTag(val tag: String?) : InspirationListIntent
    data object LoadAllTags : InspirationListIntent
}
