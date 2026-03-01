package com.cycling.feature.tools.inspiration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.Inspiration
import com.cycling.domain.usecase.inspiration.AddInspirationUseCase
import com.cycling.domain.usecase.inspiration.DeleteInspirationUseCase
import com.cycling.domain.usecase.inspiration.GetAllTagsUseCase
import com.cycling.domain.usecase.inspiration.GetInspirationsByTagUseCase
import com.cycling.domain.usecase.inspiration.GetInspirationsUseCase
import com.cycling.domain.usecase.inspiration.SearchInspirationsUseCase
import com.cycling.domain.usecase.inspiration.UpdateInspirationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class InspirationListViewModel @Inject constructor(
    getInspirationsUseCase: GetInspirationsUseCase,
    private val addInspirationUseCase: AddInspirationUseCase,
    private val updateInspirationUseCase: UpdateInspirationUseCase,
    private val deleteInspirationUseCase: DeleteInspirationUseCase,
    private val searchInspirationsUseCase: SearchInspirationsUseCase,
    private val getInspirationsByTagUseCase: GetInspirationsByTagUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InspirationListState())
    val state: StateFlow<InspirationListState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<InspirationListEffect>()
    val effect = _effect.asSharedFlow()

    private val inspirationsFlow = getInspirationsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val tagsFlow = getAllTagsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            inspirationsFlow.collect { inspirations ->
                val currentQuery = _state.value.searchQuery
                val currentTag = _state.value.selectedTag
                _state.value = _state.value.copy(
                    inspirations = inspirations,
                    filteredInspirations = filterInspirations(inspirations, currentQuery, currentTag),
                    isLoading = false
                )
            }
        }

        viewModelScope.launch {
            tagsFlow.collect { tags ->
                _state.value = _state.value.copy(allTags = tags)
            }
        }
    }

    fun onIntent(intent: InspirationListIntent) {
        when (intent) {
            InspirationListIntent.LoadInspirations -> loadInspirations()
            InspirationListIntent.ShowAddDialog -> showAddDialog()
            InspirationListIntent.HideAddDialog -> hideAddDialog()
            is InspirationListIntent.ShowEditDialog -> showEditDialog(intent.inspiration)
            InspirationListIntent.HideEditDialog -> hideEditDialog()
            is InspirationListIntent.ShowDeleteDialog -> showDeleteDialog(intent.inspiration)
            InspirationListIntent.HideDeleteDialog -> hideDeleteDialog()
            is InspirationListIntent.AddInspiration -> addInspiration(intent.inspiration)
            is InspirationListIntent.UpdateInspiration -> updateInspiration(intent.inspiration)
            InspirationListIntent.ConfirmDelete -> confirmDelete()
            is InspirationListIntent.SearchInspirations -> searchInspirations(intent.query)
            is InspirationListIntent.FilterByTag -> filterByTag(intent.tag)
            InspirationListIntent.LoadAllTags -> loadAllTags()
        }
    }

    private fun loadInspirations() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
        }
    }

    private fun showAddDialog() {
        _state.value = _state.value.copy(showAddDialog = true)
    }

    private fun hideAddDialog() {
        _state.value = _state.value.copy(showAddDialog = false)
    }

    private fun showEditDialog(inspiration: Inspiration) {
        _state.value = _state.value.copy(
            showEditDialog = true,
            inspirationToEdit = inspiration
        )
    }

    private fun hideEditDialog() {
        _state.value = _state.value.copy(
            showEditDialog = false,
            inspirationToEdit = null
        )
    }

    private fun showDeleteDialog(inspiration: Inspiration) {
        _state.value = _state.value.copy(
            showDeleteDialog = true,
            inspirationToDelete = inspiration
        )
    }

    private fun hideDeleteDialog() {
        _state.value = _state.value.copy(
            showDeleteDialog = false,
            inspirationToDelete = null
        )
    }

    private fun addInspiration(inspiration: Inspiration) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                addInspirationUseCase(inspiration)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showAddDialog = false
                )
                _effect.emit(InspirationListEffect.ShowToast("灵感创建成功"))
            } catch (e: IOException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(InspirationListEffect.ShowError("网络错误: ${e.message}"))
            } catch (e: RuntimeException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(InspirationListEffect.ShowError("创建灵感失败: ${e.message}"))
            }
        }
    }

    private fun updateInspiration(inspiration: Inspiration) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val updatedInspiration = inspiration.copy(
                    updatedAt = System.currentTimeMillis()
                )
                updateInspirationUseCase(updatedInspiration)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showEditDialog = false,
                    inspirationToEdit = null
                )
                _effect.emit(InspirationListEffect.ShowToast("灵感更新成功"))
            } catch (e: IOException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(InspirationListEffect.ShowError("网络错误: ${e.message}"))
            } catch (e: RuntimeException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(InspirationListEffect.ShowError("更新灵感失败: ${e.message}"))
            }
        }
    }

    private fun confirmDelete() {
        viewModelScope.launch {
            _state.value.inspirationToDelete?.let { inspiration ->
                _state.value = _state.value.copy(isLoading = true)
                try {
                    deleteInspirationUseCase(inspiration.id)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        showDeleteDialog = false,
                        inspirationToDelete = null
                    )
                    _effect.emit(InspirationListEffect.ShowToast("灵感删除成功"))
                } catch (e: IOException) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                    _effect.emit(InspirationListEffect.ShowError("网络错误: ${e.message}"))
                } catch (e: RuntimeException) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                    _effect.emit(InspirationListEffect.ShowError("删除灵感失败: ${e.message}"))
                }
            }
        }
    }

    private fun searchInspirations(query: String) {
        val currentTag = _state.value.selectedTag
        val filtered = filterInspirations(_state.value.inspirations, query, currentTag)
        _state.value = _state.value.copy(
            searchQuery = query,
            filteredInspirations = filtered
        )
    }

    private fun filterByTag(tag: String?) {
        val currentQuery = _state.value.searchQuery
        val filtered = filterInspirations(_state.value.inspirations, currentQuery, tag)
        _state.value = _state.value.copy(
            selectedTag = tag,
            filteredInspirations = filtered
        )
    }

    private fun loadAllTags() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
        }
    }

    private fun filterInspirations(
        inspirations: List<Inspiration>,
        query: String,
        tag: String?
    ): List<Inspiration> {
        var result = inspirations

        if (tag != null) {
            result = result.filter { it.tags.contains(tag) }
        }

        if (query.isNotBlank()) {
            result = result.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.content.contains(query, ignoreCase = true) ||
                it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
            }
        }

        return result
    }
}
