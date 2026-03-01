package com.cycling.domain.usecase.outline

import com.cycling.domain.model.OutlineItem
import com.cycling.domain.repository.OutlineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOutlineItemByIdUseCase @Inject constructor(
    private val repository: OutlineRepository
) {
    operator fun invoke(id: Long): Flow<OutlineItem?> = repository.getOutlineItemById(id)
}
