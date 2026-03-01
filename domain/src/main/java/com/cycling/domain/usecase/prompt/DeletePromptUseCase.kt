package com.cycling.domain.usecase.prompt

import com.cycling.domain.repository.PromptRepository
import javax.inject.Inject

class DeletePromptUseCase @Inject constructor(
    private val repository: PromptRepository
) {
    suspend operator fun invoke(id: Long): DeletePromptResult {
        val prompt = repository.getPromptByIdSync(id)
        if (prompt == null) {
            return DeletePromptResult.Error("提示词不存在")
        }
        if (prompt.isSystem) {
            return DeletePromptResult.Error("无法删除系统预设提示词")
        }
        repository.deletePrompt(id)
        return DeletePromptResult.Success
    }
}

sealed class DeletePromptResult {
    object Success : DeletePromptResult()
    data class Error(val message: String) : DeletePromptResult()
}
