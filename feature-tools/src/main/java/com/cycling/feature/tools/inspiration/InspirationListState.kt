package com.cycling.feature.tools.inspiration

import com.cycling.domain.model.Inspiration

data class InspirationListState(
    val inspirations: List<Inspiration> = emptyList(),
    val filteredInspirations: List<Inspiration> = emptyList(),
    val isLoading: Boolean = false,
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val inspirationToEdit: Inspiration? = null,
    val inspirationToDelete: Inspiration? = null,
    val searchQuery: String = "",
    val selectedTag: String? = null,
    val allTags: List<String> = emptyList(),
    val error: String? = null
) {
    val displayInspirations: List<Inspiration>
        get() = if (searchQuery.isBlank() && selectedTag == null) {
            inspirations
        } else {
            filteredInspirations
        }

    val isEmpty: Boolean
        get() = displayInspirations.isEmpty() && !isLoading
}
