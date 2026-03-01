package com.cycling.feature.tools.prompt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.Prompt
import com.cycling.domain.model.PromptCategory
import com.cycling.domain.usecase.prompt.CreatePromptUseCase
import com.cycling.domain.usecase.prompt.DeletePromptUseCase
import com.cycling.domain.usecase.prompt.DeletePromptResult
import com.cycling.domain.usecase.prompt.GetFavoritePromptsUseCase
import com.cycling.domain.usecase.prompt.GetPromptsByCategoryUseCase
import com.cycling.domain.usecase.prompt.GetPromptsUseCase
import com.cycling.domain.usecase.prompt.SearchPromptsUseCase
import com.cycling.domain.usecase.prompt.ToggleFavoriteUseCase
import com.cycling.domain.usecase.prompt.UpdatePromptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromptViewModel @Inject constructor(
    getPromptsUseCase: GetPromptsUseCase,
    private val getPromptsByCategoryUseCase: GetPromptsByCategoryUseCase,
    private val getFavoritePromptsUseCase: GetFavoritePromptsUseCase,
    private val searchPromptsUseCase: SearchPromptsUseCase,
    private val createPromptUseCase: CreatePromptUseCase,
    private val updatePromptUseCase: UpdatePromptUseCase,
    private val deletePromptUseCase: DeletePromptUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PromptState())
    val state: StateFlow<PromptState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PromptEffect>()
    val effect = _effect.asSharedFlow()

    private val promptsFlow = getPromptsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            promptsFlow.collect { prompts ->
                val currentState = _state.value
                _state.value = currentState.copy(
                    prompts = prompts,
                    filteredPrompts = filterPrompts(
                        prompts = prompts,
                        query = currentState.searchQuery,
                        category = currentState.selectedCategory,
                        favoritesOnly = currentState.showFavoritesOnly
                    ),
                    isLoading = false
                )
            }
        }
    }

    fun onIntent(intent: PromptIntent) {
        when (intent) {
            is PromptIntent.LoadPrompts -> loadPrompts()
            is PromptIntent.SelectCategory -> selectCategory(intent.category)
            is PromptIntent.ToggleFavoritesFilter -> toggleFavoritesFilter()
            is PromptIntent.SearchPrompts -> searchPrompts(intent.query)
            is PromptIntent.ShowAddDialog -> showAddDialog()
            is PromptIntent.HideAddDialog -> hideAddDialog()
            is PromptIntent.ShowEditDialog -> showEditDialog(intent.prompt)
            is PromptIntent.HideEditDialog -> hideEditDialog()
            is PromptIntent.ShowDeleteConfirm -> showDeleteConfirm(intent.prompt)
            is PromptIntent.HideDeleteConfirm -> hideDeleteConfirm()
            is PromptIntent.CreatePrompt -> createPrompt(intent.prompt)
            is PromptIntent.UpdatePrompt -> updatePrompt(intent.prompt)
            is PromptIntent.DeletePrompt -> deletePrompt(intent.prompt)
            is PromptIntent.ToggleFavorite -> toggleFavorite(intent.prompt)
            is PromptIntent.ClearMessage -> clearMessage()
            is PromptIntent.ClearError -> clearError()
        }
    }

    private fun loadPrompts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
        }
    }

    private fun selectCategory(category: PromptCategory?) {
        val currentState = _state.value
        val filtered = filterPrompts(
            prompts = currentState.prompts,
            query = currentState.searchQuery,
            category = category,
            favoritesOnly = currentState.showFavoritesOnly
        )
        _state.value = currentState.copy(
            selectedCategory = category,
            filteredPrompts = filtered
        )
    }

    private fun toggleFavoritesFilter() {
        val currentState = _state.value
        val newFavoritesOnly = !currentState.showFavoritesOnly
        val filtered = filterPrompts(
            prompts = currentState.prompts,
            query = currentState.searchQuery,
            category = currentState.selectedCategory,
            favoritesOnly = newFavoritesOnly
        )
        _state.value = currentState.copy(
            showFavoritesOnly = newFavoritesOnly,
            filteredPrompts = filtered
        )
    }

    private fun searchPrompts(query: String) {
        val currentState = _state.value
        val filtered = filterPrompts(
            prompts = currentState.prompts,
            query = query,
            category = currentState.selectedCategory,
            favoritesOnly = currentState.showFavoritesOnly
        )
        _state.value = currentState.copy(
            searchQuery = query,
            filteredPrompts = filtered
        )
    }

    private fun showAddDialog() {
        _state.value = _state.value.copy(showAddDialog = true)
    }

    private fun hideAddDialog() {
        _state.value = _state.value.copy(showAddDialog = false)
    }

    private fun showEditDialog(prompt: Prompt) {
        _state.value = _state.value.copy(
            showEditDialog = true,
            editingPrompt = prompt
        )
    }

    private fun hideEditDialog() {
        _state.value = _state.value.copy(
            showEditDialog = false,
            editingPrompt = null
        )
    }

    private fun showDeleteConfirm(prompt: Prompt) {
        _state.value = _state.value.copy(
            showDeleteConfirm = true,
            promptToDelete = prompt
        )
    }

    private fun hideDeleteConfirm() {
        _state.value = _state.value.copy(
            showDeleteConfirm = false,
            promptToDelete = null
        )
    }

    private fun createPrompt(prompt: Prompt) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                createPromptUseCase(prompt)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showAddDialog = false
                )
                _effect.emit(PromptEffect.ShowToast("提示词创建成功"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(PromptEffect.ShowError("创建失败: ${e.message}"))
            }
        }
    }

    private fun updatePrompt(prompt: Prompt) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val updatedPrompt = prompt.copy(
                    updatedAt = System.currentTimeMillis()
                )
                updatePromptUseCase(updatedPrompt)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showEditDialog = false,
                    editingPrompt = null
                )
                _effect.emit(PromptEffect.ShowToast("提示词更新成功"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(PromptEffect.ShowError("更新失败: ${e.message}"))
            }
        }
    }

    private fun deletePrompt(prompt: Prompt) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val result = deletePromptUseCase(prompt.id)
                when (result) {
                    is DeletePromptResult.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            showDeleteConfirm = false,
                            promptToDelete = null
                        )
                        _effect.emit(PromptEffect.ShowToast("提示词删除成功"))
                    }
                    is DeletePromptResult.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            showDeleteConfirm = false,
                            promptToDelete = null
                        )
                        _effect.emit(PromptEffect.ShowError(result.message))
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(PromptEffect.ShowError("删除失败: ${e.message}"))
            }
        }
    }

    private fun toggleFavorite(prompt: Prompt) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(prompt.id)
            } catch (e: Exception) {
                _effect.emit(PromptEffect.ShowError("操作失败: ${e.message}"))
            }
        }
    }

    private fun clearMessage() {
        _state.value = _state.value.copy(message = null)
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun filterPrompts(
        prompts: List<Prompt>,
        query: String,
        category: PromptCategory?,
        favoritesOnly: Boolean
    ): List<Prompt> {
        var result = prompts

        if (category != null) {
            result = result.filter { it.category == category }
        }

        if (favoritesOnly) {
            result = result.filter { it.isFavorite }
        }

        if (query.isNotBlank()) {
            result = result.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
            }
        }

        return result
    }
}
