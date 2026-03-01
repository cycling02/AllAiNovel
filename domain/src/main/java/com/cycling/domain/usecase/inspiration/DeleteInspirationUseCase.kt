package com.cycling.domain.usecase.inspiration

import com.cycling.domain.repository.InspirationRepository
import javax.inject.Inject

class DeleteInspirationUseCase @Inject constructor(
    private val repository: InspirationRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteInspiration(id)
}
