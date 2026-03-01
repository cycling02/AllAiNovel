package com.cycling.domain.usecase.inspiration

import com.cycling.domain.model.Inspiration
import com.cycling.domain.repository.InspirationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInspirationsByTagUseCase @Inject constructor(
    private val repository: InspirationRepository
) {
    operator fun invoke(tag: String): Flow<List<Inspiration>> = repository.getInspirationsByTag(tag)
}
