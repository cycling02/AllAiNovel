package com.cycling.domain.usecase.outline

import com.cycling.domain.repository.OutlineRepository
import javax.inject.Inject

class DeleteOutlineItemUseCase @Inject constructor(
    private val repository: OutlineRepository
) {
    suspend operator fun invoke(id: Long, deleteChildren: Boolean = false) {
        if (deleteChildren) {
            repository.deleteOutlineItemWithChildren(id)
        } else {
            repository.deleteOutlineItem(id)
        }
    }
}
