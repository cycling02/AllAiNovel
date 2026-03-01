package com.cycling.domain.usecase.prompt

import com.cycling.domain.model.Prompt
import com.cycling.domain.repository.PromptRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPromptsUseCase @Inject constructor(
    private val repository: PromptRepository
) {
    operator fun invoke(query: String): Flow<List<Prompt>> = repository.searchPrompts(query)
}
