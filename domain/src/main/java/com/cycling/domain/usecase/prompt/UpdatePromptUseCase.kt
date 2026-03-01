package com.cycling.domain.usecase.prompt

import com.cycling.domain.model.Prompt
import com.cycling.domain.repository.PromptRepository
import javax.inject.Inject

class UpdatePromptUseCase @Inject constructor(
    private val repository: PromptRepository
) {
    suspend operator fun invoke(prompt: Prompt) = repository.updatePrompt(prompt)
}
