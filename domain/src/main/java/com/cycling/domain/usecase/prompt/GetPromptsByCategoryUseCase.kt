package com.cycling.domain.usecase.prompt

import com.cycling.domain.model.Prompt
import com.cycling.domain.model.PromptCategory
import com.cycling.domain.repository.PromptRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPromptsByCategoryUseCase @Inject constructor(
    private val repository: PromptRepository
) {
    operator fun invoke(category: PromptCategory): Flow<List<Prompt>> = 
        repository.getPromptsByCategory(category)
}
