package com.cycling.feature.editor.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.cycling.domain.model.Chapter
import com.cycling.domain.model.WritingSession
import com.cycling.domain.usecase.ai.ContinueWritingUseCase
import com.cycling.domain.usecase.apiconfig.GetDefaultApiConfigUseCase
import com.cycling.domain.usecase.chapter.GetChapterByIdUseCase
import com.cycling.domain.usecase.chapter.UpdateChapterUseCase
import com.cycling.domain.usecase.writingsession.SaveWritingSessionUseCase
import com.cycling.feature.editor.model.ChapterEditEffect
import com.cycling.feature.editor.model.ChapterEditIntent
import com.cycling.feature.editor.model.ChapterEditState
import com.cycling.feature.editor.navigation.ChapterEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getChapterByIdUseCase: GetChapterByIdUseCase,
    getDefaultApiConfigUseCase: GetDefaultApiConfigUseCase,
    private val updateChapterUseCase: UpdateChapterUseCase,
    private val continueWritingUseCase: ContinueWritingUseCase,
    private val saveWritingSessionUseCase: SaveWritingSessionUseCase
) : ViewModel() {
    
    private val chapterId: Long = savedStateHandle.toRoute<ChapterEdit>().chapterId
    
    private val chapter: StateFlow<Chapter?> = getChapterByIdUseCase(chapterId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    private val _state = MutableStateFlow(ChapterEditState())
    val state: StateFlow<ChapterEditState> = _state.asStateFlow()
    
    private val _effect = MutableSharedFlow<ChapterEditEffect>()
    val effect: SharedFlow<ChapterEditEffect> = _effect.asSharedFlow()
    
    private var saveJob: Job? = null
    private var originalContent: String = ""
    private var sessionStartTime: Long = 0L
    private var initialWordCount: Int = 0
    
    init {
        sessionStartTime = System.currentTimeMillis()
        
        viewModelScope.launch {
            chapter.collect { chap ->
                chap?.let {
                    originalContent = it.content
                    initialWordCount = it.content.length
                    _state.value = _state.value.copy(
                        title = it.title,
                        content = it.content,
                        initialWordCount = initialWordCount,
                        sessionStartTime = sessionStartTime
                    )
                }
            }
        }
        
        viewModelScope.launch {
            _state.value = _state.value.copy(
                apiConfig = getDefaultApiConfigUseCase().first()
            )
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        saveWritingSession()
    }
    
    fun handleIntent(intent: ChapterEditIntent) {
        when (intent) {
            is ChapterEditIntent.UpdateContent -> updateContent(intent.content)
            is ChapterEditIntent.SaveChapter -> saveChapter()
            is ChapterEditIntent.ContinueWriting -> continueWriting()
            is ChapterEditIntent.AcceptAiResult -> acceptAiResult()
            is ChapterEditIntent.DismissAiResult -> dismissAiResult()
            is ChapterEditIntent.ClearError -> clearError()
        }
    }
    
    private fun updateContent(newContent: String) {
        _state.value = _state.value.copy(
            content = newContent,
            hasUnsavedChanges = newContent != originalContent
        )
        scheduleAutoSave()
    }
    
    private fun scheduleAutoSave() {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            delay(2000)
            saveChapter()
        }
    }
    
    private fun saveChapter() {
        viewModelScope.launch {
            val currentChapter = chapter.value ?: return@launch
            _state.value = _state.value.copy(isSaving = true)
            
            val updatedChapter = currentChapter.copy(
                content = _state.value.content,
                wordCount = _state.value.content.length,
                updatedAt = System.currentTimeMillis()
            )
            
            updateChapterUseCase(updatedChapter)
            originalContent = _state.value.content
            _state.value = _state.value.copy(
                isSaving = false,
                hasUnsavedChanges = false
            )
        }
    }
    
    private fun continueWriting() {
        viewModelScope.launch {
            val config = _state.value.apiConfig
            if (config == null) {
                _state.value = _state.value.copy(
                    error = "请先在设置中配置API"
                )
                return@launch
            }
            
            _state.value = _state.value.copy(
                isAiLoading = true,
                error = null
            )
            
            val context = _state.value.content.takeLast(2000)
            val result = continueWritingUseCase(
                config = config,
                context = context,
                maxTokens = 1000
            )
            
            result.fold(
                onSuccess = { aiContent ->
                    _state.value = _state.value.copy(
                        isAiLoading = false,
                        aiResult = aiContent,
                        showAiResult = true
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isAiLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }
    
    private fun acceptAiResult() {
        val currentContent = _state.value.content
        val aiContent = _state.value.aiResult ?: return
        
        _state.value = _state.value.copy(
            content = currentContent + "\n\n" + aiContent,
            showAiResult = false,
            aiResult = null,
            hasUnsavedChanges = true
        )
        scheduleAutoSave()
    }
    
    private fun dismissAiResult() {
        _state.value = _state.value.copy(
            showAiResult = false,
            aiResult = null
        )
    }
    
    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    private fun saveWritingSession() {
        val currentChapter = chapter.value ?: return
        val endTime = System.currentTimeMillis()
        val duration = endTime - sessionStartTime
        val endWordCount = _state.value.content.length
        
        if (duration >= 1000 && endWordCount != initialWordCount) {
            viewModelScope.launch {
                val session = WritingSession(
                    bookId = currentChapter.bookId,
                    chapterId = chapterId,
                    startWordCount = initialWordCount,
                    endWordCount = endWordCount,
                    startTime = sessionStartTime,
                    endTime = endTime,
                    duration = duration
                )
                saveWritingSessionUseCase(session)
            }
        }
    }
}
