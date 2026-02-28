package com.cycling.domain.usecase.ai

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.repository.AiRepository
import javax.inject.Inject

class ContinueWritingUseCase @Inject constructor(
    private val repository: AiRepository
) {
    suspend operator fun invoke(
        config: ApiConfig,
        context: String,
        maxTokens: Int = 1000
    ): Result<String> = repository.continueWriting(config, context, maxTokens)
}
