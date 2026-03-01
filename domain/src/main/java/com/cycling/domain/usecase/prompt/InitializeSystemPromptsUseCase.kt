package com.cycling.domain.usecase.prompt

import com.cycling.domain.repository.PromptRepository
import javax.inject.Inject

class InitializeSystemPromptsUseCase @Inject constructor(
    private val repository: PromptRepository
) {
    suspend operator fun invoke() = repository.initializeSystemPrompts()
}
