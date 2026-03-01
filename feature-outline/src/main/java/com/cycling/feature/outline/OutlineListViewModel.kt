package com.cycling.feature.outline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.OutlineStatus
import com.cycling.domain.usecase.ai.GenerateOutlineUseCase
import com.cycling.domain.usecase.apiconfig.GetDefaultApiConfigUseCase
import com.cycling.domain.usecase.book.GetBookByIdUseCase
import com.cycling.domain.usecase.outline.AddOutlineItemUseCase
import com.cycling.domain.usecase.outline.DeleteOutlineItemUseCase
import com.cycling.domain.usecase.outline.GenerateAndSaveChapterUseCase
import com.cycling.domain.usecase.outline.GetOutlineByBookIdUseCase
import com.cycling.domain.usecase.outline.ReorderOutlineItemsUseCase
import com.cycling.domain.usecase.outline.UpdateOutlineItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OutlineListViewModel @Inject constructor(
    private val getOutlineByBookIdUseCase: GetOutlineByBookIdUseCase,
    private val addOutlineItemUseCase: AddOutlineItemUseCase,
    private val updateOutlineItemUseCase: UpdateOutlineItemUseCase,
    private val deleteOutlineItemUseCase: DeleteOutlineItemUseCase,
    private val reorderOutlineItemsUseCase: ReorderOutlineItemsUseCase,
    private val generateOutlineUseCase: GenerateOutlineUseCase,
    private val getDefaultApiConfigUseCase: GetDefaultApiConfigUseCase,
    private val generateAndSaveChapterUseCase: GenerateAndSaveChapterUseCase,
    private val getBookByIdUseCase: GetBookByIdUseCase
) : ViewModel() {

    private var currentBookId: Long = 0L

    private val _state = MutableStateFlow(OutlineListState())
    val state: StateFlow<OutlineListState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OutlineListEffect>()
    val effect = _effect.asSharedFlow()

    private var outlineFlow: StateFlow<List<OutlineItem>>? = null

    val uiModels: StateFlow<List<OutlineItemUiModel>> = _state
        .map { state -> buildUiModels(state.outlineItems, state.expandedIds) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun buildUiModels(items: List<OutlineItem>, expandedIds: Set<Long>): List<OutlineItemUiModel> {
        fun buildTree(parentId: Long?, level: Int): List<OutlineItemUiModel> {
            val result = mutableListOf<OutlineItemUiModel>()
            val children = items.filter { it.parentId == parentId }.sortedBy { it.sortOrder }

            for (item in children) {
                val hasChildren = items.any { it.parentId == item.id }
                val isExpanded = expandedIds.contains(item.id)

                result.add(OutlineItemUiModel(
                    item = item,
                    level = level,
                    isExpanded = isExpanded,
                    hasChildren = hasChildren
                ))

                if (hasChildren && isExpanded) {
                    result.addAll(buildTree(item.id, level + 1))
                }
            }

            return result
        }

        return buildTree(null, 0)
    }

    fun setBookId(bookId: Long) {
        android.util.Log.d("OutlineList", "setBookId called with: $bookId, current: $currentBookId")
        if (currentBookId == bookId) return
        currentBookId = bookId
        
        outlineFlow = getOutlineByBookIdUseCase(bookId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )
        
        viewModelScope.launch {
            outlineFlow?.collect { items ->
                _state.value = _state.value.copy(
                    outlineItems = items,
                    isLoading = false
                )
            }
        }
    }

    fun onIntent(intent: OutlineListIntent) {
        when (intent) {
            OutlineListIntent.LoadOutline -> loadOutline()
            is OutlineListIntent.ShowAddDialog -> showAddDialog(intent.parent)
            OutlineListIntent.HideAddDialog -> hideAddDialog()
            is OutlineListIntent.ShowEditDialog -> showEditDialog(intent.item)
            OutlineListIntent.HideEditDialog -> hideEditDialog()
            is OutlineListIntent.ShowDeleteDialog -> showDeleteDialog(intent.item)
            OutlineListIntent.HideDeleteDialog -> hideDeleteDialog()
            is OutlineListIntent.AddOutlineItem -> addOutlineItem(intent.title, intent.summary, intent.parent)
            is OutlineListIntent.UpdateOutlineItem -> updateOutlineItem(intent.item, intent.title, intent.summary, intent.status)
            is OutlineListIntent.DeleteOutlineItem -> deleteOutlineItem(intent.deleteChildren)
            is OutlineListIntent.ToggleExpand -> toggleExpand(intent.itemId)
            is OutlineListIntent.ReorderItems -> reorderItems(intent.items)
            OutlineListIntent.ShowAiGenerateDialog -> showAiGenerateDialog()
            OutlineListIntent.HideAiGenerateDialog -> hideAiGenerateDialog()
            is OutlineListIntent.GenerateOutline -> generateOutline(intent.topic, intent.summary, intent.chapterCount, intent.levelCount)
            OutlineListIntent.ApplyAiOutline -> applyAiOutline()
            OutlineListIntent.HideAiPreviewDialog -> hideAiPreviewDialog()
            is OutlineListIntent.GenerateChapterFromOutline -> generateChapterFromOutline(intent.item)
            is OutlineListIntent.NavigateToChapter -> navigateToChapter(intent.chapterId)
        }
    }

    private fun navigateToChapter(chapterId: Long) {
        viewModelScope.launch {
            _effect.emit(OutlineListEffect.NavigateToChapter(chapterId))
        }
    }

    private fun generateChapterFromOutline(item: OutlineItem) {
        android.util.Log.d("OutlineList", "generateChapterFromOutline called for item: ${item.id}, currentBookId: $currentBookId")
        viewModelScope.launch {
            _state.value = _state.value.copy(generatingChapterId = item.id)

            try {
                val book = getBookByIdUseCase(currentBookId).first()
                android.util.Log.d("OutlineList", "Book fetched: ${book?.title}")
                val bookTitle = book?.title ?: ""
                val bookSummary = book?.description ?: ""

                val result = generateAndSaveChapterUseCase(
                    outlineItem = item,
                    bookTitle = bookTitle,
                    bookSummary = bookSummary
                )
                android.util.Log.d("OutlineList", "Generate result: ${result.isSuccess}")

                result.onSuccess { chapterId ->
                    _state.value = _state.value.copy(generatingChapterId = null)
                    _effect.emit(OutlineListEffect.ShowToast("章节生成成功"))
                    _effect.emit(OutlineListEffect.NavigateToChapter(chapterId))
                }.onFailure { error ->
                    _state.value = _state.value.copy(generatingChapterId = null)
                    _effect.emit(OutlineListEffect.ShowError("生成失败: ${error.message}"))
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(generatingChapterId = null)
                _effect.emit(OutlineListEffect.ShowError("生成失败: ${e.message}"))
            }
        }
    }

    private fun loadOutline() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
        }
    }

    private fun showAddDialog(parent: OutlineItem?) {
        _state.value = _state.value.copy(
            showAddDialog = true,
            parentForNewItem = parent
        )
    }

    private fun hideAddDialog() {
        _state.value = _state.value.copy(
            showAddDialog = false,
            parentForNewItem = null
        )
    }

    private fun showEditDialog(item: OutlineItem) {
        _state.value = _state.value.copy(
            showEditDialog = true,
            itemToEdit = item
        )
    }

    private fun hideEditDialog() {
        _state.value = _state.value.copy(
            showEditDialog = false,
            itemToEdit = null
        )
    }

    private fun showDeleteDialog(item: OutlineItem) {
        _state.value = _state.value.copy(
            showDeleteDialog = true,
            itemToDelete = item
        )
    }

    private fun hideDeleteDialog() {
        _state.value = _state.value.copy(
            showDeleteDialog = false,
            itemToDelete = null
        )
    }

    private fun addOutlineItem(title: String, summary: String, parent: OutlineItem?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val level = if (parent != null) parent.level + 1 else 0
                val item = OutlineItem(
                    bookId = currentBookId,
                    parentId = parent?.id,
                    title = title.trim(),
                    summary = summary.trim(),
                    level = level,
                    sortOrder = getNextSortOrder(parent?.id)
                )
                addOutlineItemUseCase(item)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showAddDialog = false,
                    parentForNewItem = null
                )
                _effect.emit(OutlineListEffect.ShowToast("大纲项创建成功"))
            } catch (e: IOException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(OutlineListEffect.ShowError("网络错误: ${e.message}"))
            } catch (e: RuntimeException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(OutlineListEffect.ShowError("创建大纲项失败: ${e.message}"))
            }
        }
    }

    private fun updateOutlineItem(item: OutlineItem, title: String, summary: String, status: OutlineStatus) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val updatedItem = item.copy(
                    title = title.trim(),
                    summary = summary.trim(),
                    status = status,
                    updatedAt = System.currentTimeMillis()
                )
                updateOutlineItemUseCase(updatedItem)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showEditDialog = false,
                    itemToEdit = null
                )
                _effect.emit(OutlineListEffect.ShowToast("大纲项更新成功"))
            } catch (e: IOException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(OutlineListEffect.ShowError("网络错误: ${e.message}"))
            } catch (e: RuntimeException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(OutlineListEffect.ShowError("更新大纲项失败: ${e.message}"))
            }
        }
    }

    private fun deleteOutlineItem(deleteChildren: Boolean) {
        viewModelScope.launch {
            _state.value.itemToDelete?.let { item ->
                _state.value = _state.value.copy(isLoading = true)
                try {
                    deleteOutlineItemUseCase(item.id, deleteChildren)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        showDeleteDialog = false,
                        itemToDelete = null
                    )
                    _effect.emit(OutlineListEffect.ShowToast("大纲项删除成功"))
                } catch (e: IOException) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                    _effect.emit(OutlineListEffect.ShowError("网络错误: ${e.message}"))
                } catch (e: RuntimeException) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                    _effect.emit(OutlineListEffect.ShowError("删除大纲项失败: ${e.message}"))
                }
            }
        }
    }

    private fun toggleExpand(itemId: Long) {
        val currentExpanded = _state.value.expandedIds
        val newExpanded = if (currentExpanded.contains(itemId)) {
            currentExpanded - itemId
        } else {
            currentExpanded + itemId
        }
        _state.value = _state.value.copy(expandedIds = newExpanded)
    }

    private fun reorderItems(items: List<OutlineItem>) {
        viewModelScope.launch {
            try {
                reorderOutlineItemsUseCase(items)
            } catch (e: Exception) {
                _effect.emit(OutlineListEffect.ShowError("排序失败: ${e.message}"))
            }
        }
    }

    private fun getNextSortOrder(parentId: Long?): Int {
        val siblings = _state.value.outlineItems.filter { it.parentId == parentId }
        return if (siblings.isEmpty()) 0 else siblings.maxOf { it.sortOrder } + 1
    }

    fun canAddChild(parent: OutlineItem?): Boolean {
        if (parent == null) return true
        return parent.level < 2
    }

    fun hasChildren(itemId: Long): Boolean {
        return _state.value.outlineItems.any { it.parentId == itemId }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _effect.emit(OutlineListEffect.NavigateBack)
        }
    }

    private fun showAiGenerateDialog() {
        _state.value = _state.value.copy(showAiGenerateDialog = true)
    }

    private fun hideAiGenerateDialog() {
        _state.value = _state.value.copy(showAiGenerateDialog = false)
    }

    private fun generateOutline(
        topic: String,
        summary: String,
        chapterCount: Int,
        levelCount: Int
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isAiGenerating = true,
                showAiGenerateDialog = false
            )
            
            try {
                val apiConfig = getDefaultApiConfigUseCase().first()
                
                if (apiConfig == null) {
                    _state.value = _state.value.copy(
                        isAiGenerating = false,
                        error = "未配置默认API"
                    )
                    _effect.emit(OutlineListEffect.ShowError("请先配置默认API"))
                    return@launch
                }
                
                val result = generateOutlineUseCase(
                    config = apiConfig,
                    bookId = currentBookId,
                    topic = topic,
                    summary = summary,
                    chapterCount = chapterCount,
                    levelCount = levelCount
                )
                
                result.fold(
                    onSuccess = { outlineItems ->
                        _state.value = _state.value.copy(
                            isAiGenerating = false,
                            aiGeneratedOutline = outlineItems,
                            showAiPreviewDialog = true
                        )
                    },
                    onFailure = { error ->
                        _state.value = _state.value.copy(
                            isAiGenerating = false,
                            error = error.message
                        )
                        _effect.emit(OutlineListEffect.ShowError("AI生成失败: ${error.message}"))
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isAiGenerating = false,
                    error = e.message
                )
                _effect.emit(OutlineListEffect.ShowError("生成大纲失败: ${e.message}"))
            }
        }
    }

    private fun applyAiOutline() {
        viewModelScope.launch {
            val outlineItems = _state.value.aiGeneratedOutline
            if (outlineItems.isNullOrEmpty()) {
                _effect.emit(OutlineListEffect.ShowError("没有可应用的大纲"))
                return@launch
            }
            
            _state.value = _state.value.copy(isLoading = true)
            
            try {
                val idMapping = mutableMapOf<Long, Long>()
                
                outlineItems.forEach { item ->
                    val newParentId = item.parentId?.let { idMapping[it] }
                    
                    val itemToSave = item.copy(
                        bookId = currentBookId,
                        parentId = newParentId
                    )
                    
                    val savedId = addOutlineItemUseCase(itemToSave)
                    idMapping[item.id] = savedId
                }
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    showAiPreviewDialog = false,
                    aiGeneratedOutline = null
                )
                _effect.emit(OutlineListEffect.ShowToast("大纲应用成功"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(OutlineListEffect.ShowError("应用大纲失败: ${e.message}"))
            }
        }
    }

    private fun hideAiPreviewDialog() {
        _state.value = _state.value.copy(showAiPreviewDialog = false)
    }
}
