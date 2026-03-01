package com.cycling.domain.usecase.prompt

import com.cycling.domain.model.Prompt
import com.cycling.domain.repository.PromptRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPromptsUseCase @Inject constructor(
    private val repository: PromptRepository
) {
    operator fun invoke(): Flow<List<Prompt>> = repository.getAllPrompts()
}
