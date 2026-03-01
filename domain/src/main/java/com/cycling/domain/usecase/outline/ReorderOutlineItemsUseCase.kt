package com.cycling.domain.usecase.outline

import com.cycling.domain.model.OutlineItem
import com.cycling.domain.repository.OutlineRepository
import javax.inject.Inject

class ReorderOutlineItemsUseCase @Inject constructor(
    private val repository: OutlineRepository
) {
    suspend operator fun invoke(items: List<OutlineItem>) = repository.reorderOutlineItems(items)
}
