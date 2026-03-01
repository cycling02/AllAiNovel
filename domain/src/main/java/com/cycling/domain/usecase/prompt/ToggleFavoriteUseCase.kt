package com.cycling.domain.usecase.prompt

import com.cycling.domain.repository.PromptRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: PromptRepository
) {
    suspend operator fun invoke(id: Long) = repository.toggleFavorite(id)
}
