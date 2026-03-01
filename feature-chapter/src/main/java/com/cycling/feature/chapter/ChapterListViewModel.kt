package com.cycling.feature.chapter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.cycling.feature.chapter.navigation.ChapterList
import com.cycling.domain.model.Chapter
import com.cycling.domain.usecase.chapter.AddChapterUseCase
import com.cycling.domain.usecase.chapter.DeleteChapterUseCase
import com.cycling.domain.usecase.chapter.GetChaptersByBookIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getChaptersByBookIdUseCase: GetChaptersByBookIdUseCase,
    private val addChapterUseCase: AddChapterUseCase,
    private val deleteChapterUseCase: DeleteChapterUseCase
) : ViewModel() {

    private val bookId: Long = savedStateHandle.toRoute<ChapterList>().bookId

    val chapters: StateFlow<List<Chapter>> = getChaptersByBookIdUseCase(bookId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow(ChapterListState(bookId = bookId))
    val state: StateFlow<ChapterListState> = _state.asStateFlow()

    private val _effect = Channel<ChapterListEffect>()
    val effect = _effect.receiveAsFlow()

    fun processIntent(intent: ChapterListIntent) {
        when (intent) {
            is ChapterListIntent.ShowAddDialog -> showAddDialog()
            is ChapterListIntent.HideAddDialog -> hideAddDialog()
            is ChapterListIntent.AddChapter -> addChapter(intent.title)
            is ChapterListIntent.ShowDeleteDialog -> showDeleteDialog(intent.chapter)
            is ChapterListIntent.HideDeleteDialog -> hideDeleteDialog()
            is ChapterListIntent.ConfirmDelete -> deleteChapter()
            is ChapterListIntent.LoadChapters -> { /* Data loaded via StateFlow */ }
        }
    }

    private fun showAddDialog() {
        _state.value = _state.value.copy(showAddDialog = true)
    }

    private fun hideAddDialog() {
        _state.value = _state.value.copy(showAddDialog = false)
    }

    private fun addChapter(title: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val nextNumber = (chapters.value.maxOfOrNull { chapter -> chapter.chapterNumber } ?: 0) + 1
                val chapter = Chapter(
                    bookId = bookId,
                    title = title.trim().ifEmpty { "第${nextNumber}章" },
                    chapterNumber = nextNumber
                )
                addChapterUseCase(chapter)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showAddDialog = false
                )
                _effect.send(ChapterListEffect.ChapterAdded)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.send(ChapterListEffect.ShowError(e.message ?: "添加章节失败"))
            }
        }
    }

    private fun showDeleteDialog(chapter: Chapter) {
        _state.value = _state.value.copy(
            showDeleteDialog = true,
            chapterToDelete = chapter
        )
    }

    private fun hideDeleteDialog() {
        _state.value = _state.value.copy(
            showDeleteDialog = false,
            chapterToDelete = null
        )
    }

    private fun deleteChapter() {
        viewModelScope.launch {
            try {
                _state.value.chapterToDelete?.let { chapter ->
                    deleteChapterUseCase(chapter.id)
                }
                hideDeleteDialog()
                _effect.send(ChapterListEffect.ChapterDeleted)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
                _effect.send(ChapterListEffect.ShowError(e.message ?: "删除章节失败"))
            }
        }
    }
}
