package com.cycling.domain.usecase.inspiration

import com.cycling.domain.repository.InspirationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTagsUseCase @Inject constructor(
    private val repository: InspirationRepository
) {
    operator fun invoke(): Flow<List<String>> = repository.getAllTags()
}
