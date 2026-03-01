package com.cycling.domain.usecase.inspiration

import com.cycling.domain.model.Inspiration
import com.cycling.domain.repository.InspirationRepository
import javax.inject.Inject

class AddInspirationUseCase @Inject constructor(
    private val repository: InspirationRepository
) {
    suspend operator fun invoke(inspiration: Inspiration): Long = repository.addInspiration(inspiration)
}
