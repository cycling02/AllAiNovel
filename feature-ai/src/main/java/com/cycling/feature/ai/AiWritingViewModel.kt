package com.cycling.feature.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.Prompt
import com.cycling.domain.model.PromptCategory
import com.cycling.domain.usecase.ai.ContinueWritingUseCase
import com.cycling.domain.usecase.apiconfig.GetDefaultApiConfigUseCase
import com.cycling.domain.usecase.prompt.GetPromptsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiWritingViewModel @Inject constructor(
    private val continueWritingUseCase: ContinueWritingUseCase,
    private val getDefaultApiConfigUseCase: GetDefaultApiConfigUseCase,
    private val getPromptsUseCase: GetPromptsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AiWritingState())
    val state: StateFlow<AiWritingState> = _state.asStateFlow()

    private val _effect = Channel<AiWritingEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadPrompts()
    }

    fun onIntent(intent: AiWritingIntent) {
        when (intent) {
            is AiWritingIntent.LoadContext -> loadContext()
            is AiWritingIntent.UpdateContext -> updateContext(intent.context)
            is AiWritingIntent.SelectMode -> selectMode(intent.mode)
            is AiWritingIntent.SelectPrompt -> selectPrompt(intent.prompt)
            is AiWritingIntent.LoadPrompts -> loadPrompts()
            is AiWritingIntent.GenerateContent -> generateContent(intent.prompt)
            is AiWritingIntent.ApplyResult -> applyResult(intent.result)
            is AiWritingIntent.Regenerate -> regenerate(intent.prompt)
            is AiWritingIntent.ClearResult -> clearResult()
            is AiWritingIntent.DismissError -> dismissError()
        }
    }

    private fun loadContext() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun loadPrompts() {
        viewModelScope.launch {
            getPromptsUseCase().collect { prompts ->
                val currentMode = _state.value.selectedMode
                val category = mapModeToCategory(currentMode)
                val filteredPrompts = prompts.filter { it.category == category }
                _state.value = _state.value.copy(
                    prompts = filteredPrompts,
                    selectedPrompt = null
                )
            }
        }
    }

    private fun mapModeToCategory(mode: AiWritingMode): PromptCategory {
        return when (mode) {
            AiWritingMode.CONTINUE -> PromptCategory.CONTINUE_WRITING
            AiWritingMode.REWRITE -> PromptCategory.REWRITE
            AiWritingMode.EXPAND -> PromptCategory.EXPAND
            AiWritingMode.POLISH -> PromptCategory.POLISH
        }
    }

    private fun updateContext(context: String) {
        _state.value = _state.value.copy(context = context)
    }

    private fun selectMode(mode: AiWritingMode) {
        val category = mapModeToCategory(mode)
        val allPrompts = _state.value.prompts
        val filteredPrompts = allPrompts.filter { it.category == category }
        _state.value = _state.value.copy(
            selectedMode = mode,
            prompts = filteredPrompts,
            selectedPrompt = null
        )
        loadPrompts()
    }

    private fun selectPrompt(prompt: Prompt?) {
        _state.value = _state.value.copy(selectedPrompt = prompt)
    }

    private fun generateContent(prompt: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val defaultConfig = getDefaultApiConfigUseCase().first()
            if (defaultConfig == null) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "请先配置 AI API"
                )
                _effect.send(AiWritingEffect.ShowToast("请先配置 AI API"))
                return@launch
            }

            val finalPrompt = buildPrompt(prompt)
            val result = continueWritingUseCase(
                config = defaultConfig,
                context = _state.value.context,
                maxTokens = 1000
            )

            result.fold(
                onSuccess = { content ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        generatedResult = content,
                        lastPrompt = finalPrompt
                    )
                },
                onFailure = { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = exception.message ?: "生成失败"
                    )
                    _effect.send(AiWritingEffect.ShowToast("生成失败: ${exception.message}"))
                }
            )
        }
    }

    private fun buildPrompt(customPrompt: String?): String {
        val selectedPrompt = _state.value.selectedPrompt
        val context = _state.value.context

        if (selectedPrompt != null) {
            return selectedPrompt.content
                .replace("{{context}}", context)
                .replace("{{content}}", context)
                .replace("{{text}}", context)
        }

        val mode = _state.value.selectedMode

        return when (mode) {
            AiWritingMode.CONTINUE -> {
                customPrompt ?: "请根据以下内容继续续写，保持风格一致：\n\n$context"
            }
            AiWritingMode.REWRITE -> {
                customPrompt ?: "请对以下内容进行改写，保持原意但改变表达方式：\n\n$context"
            }
            AiWritingMode.EXPAND -> {
                customPrompt ?: "请对以下内容进行扩写，增加细节和描写：\n\n$context"
            }
            AiWritingMode.POLISH -> {
                customPrompt ?: "请对以下内容进行润色，优化语言表达和流畅度：\n\n$context"
            }
        }
    }

    private fun applyResult(result: String) {
        viewModelScope.launch {
            _effect.send(AiWritingEffect.ApplyContentToEditor(result))
            _effect.send(AiWritingEffect.NavigateBack)
        }
    }

    private fun regenerate(prompt: String?) {
        generateContent(prompt)
    }

    private fun clearResult() {
        _state.value = _state.value.copy(
            generatedResult = "",
            lastPrompt = ""
        )
    }

    private fun dismissError() {
        _state.value = _state.value.copy(error = null)
    }
}
