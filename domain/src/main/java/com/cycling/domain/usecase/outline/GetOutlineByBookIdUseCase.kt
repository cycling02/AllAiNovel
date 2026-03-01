package com.cycling.domain.usecase.outline

import com.cycling.domain.model.OutlineItem
import com.cycling.domain.repository.OutlineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOutlineByBookIdUseCase @Inject constructor(
    private val repository: OutlineRepository
) {
    operator fun invoke(bookId: Long): Flow<List<OutlineItem>> = repository.getOutlineByBookId(bookId)
}
