package com.cycling.feature.editor.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.cycling.domain.model.Chapter
import com.cycling.domain.model.WritingSession
import com.cycling.domain.repository.AiRepository
import com.cycling.domain.usecase.apiconfig.GetDefaultApiConfigUseCase
import com.cycling.domain.usecase.chapter.GetChapterByIdUseCase
import com.cycling.domain.usecase.chapter.UpdateChapterUseCase
import com.cycling.domain.usecase.context.GetBookContextUseCase
import com.cycling.domain.usecase.writingsession.SaveWritingSessionUseCase
import com.cycling.feature.editor.model.ChapterEditEffect
import com.cycling.feature.editor.model.ChapterEditIntent
import com.cycling.feature.editor.model.ChapterEditState
import com.cycling.feature.editor.navigation.ChapterEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "ChapterEdit"

@HiltViewModel
class ChapterEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getChapterByIdUseCase: GetChapterByIdUseCase,
    getDefaultApiConfigUseCase: GetDefaultApiConfigUseCase,
    private val updateChapterUseCase: UpdateChapterUseCase,
    private val aiRepository: AiRepository,
    private val saveWritingSessionUseCase: SaveWritingSessionUseCase,
    private val getBookContextUseCase: GetBookContextUseCase
) : ViewModel() {
    
    private val chapterId: Long = savedStateHandle.toRoute<ChapterEdit>().chapterId
    private val isEditable: Boolean = savedStateHandle.toRoute<ChapterEdit>().isEditable
    
    private val _state = MutableStateFlow(ChapterEditState())
    val state: StateFlow<ChapterEditState> = _state.asStateFlow()
    
    private val _effect = MutableSharedFlow<ChapterEditEffect>()
    val effect: SharedFlow<ChapterEditEffect> = _effect.asSharedFlow()
    
    private var saveJob: Job? = null
    private var streamJob: Job? = null
    private var originalContent: String = ""
    private var sessionStartTime: Long = 0L
    private var initialWordCount: Int = 0
    private var currentChapter: Chapter? = null
    
    init {
        sessionStartTime = System.currentTimeMillis()
        _state.value = _state.value.copy(isLoading = true, isEditable = isEditable)
        android.util.Log.d(TAG, "初始化: 开始加载章节, chapterId=$chapterId, isEditable=$isEditable")

        viewModelScope.launch {
            try {
                android.util.Log.d(TAG, "加载章节: 正在查询数据库...")
                val chap = getChapterByIdUseCase(chapterId).first()
                android.util.Log.d(TAG, "加载章节: 查询完成, 标题=${chap?.title}, 内容长度=${chap?.content?.length}")

                if (chap != null) {
                    currentChapter = chap
                    originalContent = chap.content
                    initialWordCount = chap.content.length

                    val bookContext = try {
                        getBookContextUseCase(chap.bookId).also {
                            android.util.Log.d(TAG, "加载书籍上下文: 角色=${it.characters.size}, 设定=${it.worldSettings.size}, 大纲=${it.outlineItems.size}")
                        }
                    } catch (e: Exception) {
                        android.util.Log.e(TAG, "加载书籍上下文失败: ${e.message}")
                        null
                    }

                    _state.value = _state.value.copy(
                        isLoading = false,
                        title = chap.title,
                        content = chap.content,
                        initialWordCount = initialWordCount,
                        sessionStartTime = sessionStartTime,
                        bookContext = bookContext
                    )
                    android.util.Log.d(TAG, "加载章节: 成功, 已更新状态")
                } else {
                    android.util.Log.e(TAG, "加载章节: 失败, 章节不存在")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "章节不存在"
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e(TAG, "加载章节: 异常 - ${e.message}", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "加载失败: ${e.message}"
                )
            }
        }

        viewModelScope.launch {
            val config = getDefaultApiConfigUseCase().first()
            _state.value = _state.value.copy(apiConfig = config)
            android.util.Log.d(TAG, "API配置: ${config?.name ?: "未配置"}")
        }
    }
    
    override fun onCleared() {
        android.util.Log.d(TAG, "ViewModel销毁: 保存写作会话")
        streamJob?.cancel()
        saveWritingSession()
        super.onCleared()
    }
    
    fun handleIntent(intent: ChapterEditIntent) {
        when (intent) {
            is ChapterEditIntent.UpdateContent -> updateContent(intent.content)
            is ChapterEditIntent.SaveChapter -> {
                android.util.Log.d(TAG, "用户操作: 手动保存章节")
                saveChapter()
            }
            is ChapterEditIntent.ContinueWriting -> {
                android.util.Log.d(TAG, "用户操作: AI流式续写")
                continueWritingStream()
            }
            is ChapterEditIntent.StopStreaming -> {
                android.util.Log.d(TAG, "用户操作: 停止流式输出")
                stopStreaming()
            }
            is ChapterEditIntent.ClearError -> clearError()
            is ChapterEditIntent.ToggleUseContext -> {
                _state.update { it.copy(useContext = !it.useContext) }
                android.util.Log.d(TAG, "切换上下文感知: ${_state.value.useContext}")
            }
        }
    }
    
    private fun updateContent(newContent: String) {
        val hasChanges = newContent != originalContent
        android.util.Log.d(TAG, "内容更新: 长度=${newContent.length}, 有变更=$hasChanges")
        _state.value = _state.value.copy(
            content = newContent,
            hasUnsavedChanges = hasChanges
        )
        scheduleAutoSave()
    }
    
    private fun scheduleAutoSave() {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            android.util.Log.d(TAG, "自动保存: 2秒后执行")
            delay(2000)
            saveChapter()
        }
    }
    
    private fun saveChapter() {
        viewModelScope.launch {
            val chap = currentChapter
            if (chap == null) {
                android.util.Log.e(TAG, "保存章节: 失败, 当前章节为空")
                return@launch
            }
            
            android.util.Log.d(TAG, "保存章节: 开始保存, 内容长度=${_state.value.content.length}")
            _state.value = _state.value.copy(isSaving = true)
            
            val updatedChapter = chap.copy(
                content = _state.value.content,
                wordCount = _state.value.content.length,
                updatedAt = System.currentTimeMillis()
            )
            
            updateChapterUseCase(updatedChapter)
            currentChapter = updatedChapter
            originalContent = _state.value.content
            _state.value = _state.value.copy(
                isSaving = false,
                hasUnsavedChanges = false
            )
            android.util.Log.d(TAG, "保存章节: 保存成功")
        }
    }
    
    private fun continueWritingStream() {
        val config = _state.value.apiConfig
        if (config == null) {
            android.util.Log.e(TAG, "AI流式续写: 失败, 未配置API")
            _state.value = _state.value.copy(error = "请先在设置中配置API")
            return
        }

        streamJob?.cancel()

        android.util.Log.d(TAG, "AI流式续写: 开始, 配置=${config.name}")
        _state.update {
            it.copy(
                isStreaming = true,
                isAiLoading = true,
                error = null
            )
        }

        val currentContent = _state.value.content
        val context = currentContent.takeLast(2000)
        android.util.Log.d(TAG, "AI流式续写: 上下文长度=${context.length}")

        val newContentBuilder = StringBuilder(currentContent)
        if (currentContent.isNotEmpty()) {
            newContentBuilder.append("\n\n")
        }

        val useContext = _state.value.useContext
        val bookContext = _state.value.bookContext
        val hasContext = useContext && bookContext != null && !bookContext.isEmpty()

        android.util.Log.d(TAG, "AI流式续写: 使用上下文感知=$hasContext")

        streamJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                android.util.Log.d(TAG, "AI流式续写: 开始收集流数据, 线程=${Thread.currentThread().name}")

                val flow = if (hasContext) {
                    aiRepository.continueWritingWithContextStream(
                        config = config,
                        context = context,
                        bookContext = bookContext!!.toPrompt(),
                        maxTokens = 1000
                    )
                } else {
                    aiRepository.continueWritingStream(
                        config = config,
                        context = context,
                        maxTokens = 1000
                    )
                }

                var chunkIndex = 0
                flow.collect { chunk ->
                    chunkIndex++
                    newContentBuilder.append(chunk)
                    val newContent = newContentBuilder.toString()
                    android.util.Log.d(TAG, "AI流式续写: 收到chunk#$chunkIndex, 内容='${chunk.take(20)}...', 总长度=${newContent.length}")
                    
                    withContext(Dispatchers.Main) {
                        _state.update { 
                            it.copy(content = newContent)
                        }
                        android.util.Log.d(TAG, "AI流式续写: 状态已更新, state.content长度=${_state.value.content.length}")
                    }
                }

                val finalContent = newContentBuilder.toString()
                android.util.Log.d(TAG, "AI流式续写: 完成, 总长度=${finalContent.length}")
                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(
                            isStreaming = false,
                            isAiLoading = false,
                            hasUnsavedChanges = finalContent != originalContent
                        )
                    }
                }
                scheduleAutoSave()
            } catch (e: Exception) {
                android.util.Log.e(TAG, "AI流式续写: 异常 - ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(
                            isStreaming = false,
                            isAiLoading = false,
                            error = "续写失败: ${e.message}"
                        )
                    }
                }
            }
        }
    }
    
    private fun stopStreaming() {
        streamJob?.cancel()
        streamJob = null
        
        val currentContent = _state.value.content
        _state.update { 
            it.copy(
                isStreaming = false,
                isAiLoading = false,
                hasUnsavedChanges = currentContent != originalContent
            )
        }
        android.util.Log.d(TAG, "停止流式输出: 已停止, 当前内容长度=${currentContent.length}")
        scheduleAutoSave()
    }
    
    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    private fun saveWritingSession() {
        val chap = currentChapter
        if (chap == null) {
            android.util.Log.d(TAG, "保存写作会话: 跳过, 当前章节为空")
            return
        }
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - sessionStartTime
        val endWordCount = _state.value.content.length
        
        android.util.Log.d(TAG, "保存写作会话: 时长=${duration}ms, 初始字数=$initialWordCount, 结束字数=$endWordCount")
        
        if (duration >= 1000 && endWordCount != initialWordCount) {
            viewModelScope.launch {
                val session = WritingSession(
                    bookId = chap.bookId,
                    chapterId = chapterId,
                    startWordCount = initialWordCount,
                    endWordCount = endWordCount,
                    startTime = sessionStartTime,
                    endTime = endTime,
                    duration = duration
                )
                saveWritingSessionUseCase(session)
                android.util.Log.d(TAG, "保存写作会话: 成功")
            }
        } else {
            android.util.Log.d(TAG, "保存写作会话: 跳过, 时长不足或字数无变化")
        }
    }
}
