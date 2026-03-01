package com.cycling.domain.usecase.inspiration

import com.cycling.domain.model.Inspiration
import com.cycling.domain.repository.InspirationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInspirationsUseCase @Inject constructor(
    private val repository: InspirationRepository
) {
    operator fun invoke(): Flow<List<Inspiration>> = repository.getAllInspirations()
}
