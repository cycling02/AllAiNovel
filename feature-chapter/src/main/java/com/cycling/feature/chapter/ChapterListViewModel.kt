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
import com.cycling.domain.usecase.chapter.UpdateChapterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
    private val deleteChapterUseCase: DeleteChapterUseCase,
    private val updateChapterUseCase: UpdateChapterUseCase
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

    private var recentlyDeletedChapter: Chapter? = null

    fun processIntent(intent: ChapterListIntent) {
        when (intent) {
            is ChapterListIntent.ShowAddDialog -> showAddDialog()
            is ChapterListIntent.HideAddDialog -> hideAddDialog()
            is ChapterListIntent.AddChapter -> addChapter(intent.title)
            is ChapterListIntent.DeleteChapter -> deleteChapter(intent.chapter)
            is ChapterListIntent.UndoDelete -> undoDelete(intent.chapter)
            is ChapterListIntent.LoadChapters -> { }
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
                val currentChapters = chapters.value
                val nextNumber = (currentChapters.maxOfOrNull { chapter -> chapter.chapterNumber } ?: 0) + 1
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

    private fun deleteChapter(chapter: Chapter) {
        viewModelScope.launch {
            try {
                recentlyDeletedChapter = chapter
                deleteChapterUseCase(chapter.id)
                _effect.send(ChapterListEffect.ShowUndoSnackbar("「${chapter.title}」已删除", chapter))
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
                _effect.send(ChapterListEffect.ShowError(e.message ?: "删除章节失败"))
            }
        }
    }

    private fun undoDelete(chapter: Chapter) {
        viewModelScope.launch {
            try {
                val restoredChapter = chapter.copy(id = 0)
                addChapterUseCase(restoredChapter)
                recentlyDeletedChapter = null
                _effect.send(ChapterListEffect.ChapterRestored)
            } catch (e: Exception) {
                _effect.send(ChapterListEffect.ShowError(e.message ?: "恢复章节失败"))
            }
        }
    }
}
