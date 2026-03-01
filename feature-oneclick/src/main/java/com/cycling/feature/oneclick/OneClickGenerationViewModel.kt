package com.cycling.feature.oneclick

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.Book
import com.cycling.domain.model.Chapter
import com.cycling.domain.model.Character
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.WorldSetting
import com.cycling.domain.repository.ApiConfigRepository
import com.cycling.domain.repository.BookRepository
import com.cycling.domain.repository.ChapterRepository
import com.cycling.domain.repository.CharacterRepository
import com.cycling.domain.repository.OutlineRepository
import com.cycling.domain.repository.WorldSettingRepository
import com.cycling.domain.usecase.oneclick.GenerationOptions
import com.cycling.domain.usecase.oneclick.GenerationProgress
import com.cycling.domain.usecase.oneclick.ParseAndGenerateBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneClickGenerationViewModel @Inject constructor(
    private val parseAndGenerateBookUseCase: ParseAndGenerateBookUseCase,
    private val apiConfigRepository: ApiConfigRepository,
    private val bookRepository: BookRepository,
    private val characterRepository: CharacterRepository,
    private val worldSettingRepository: WorldSettingRepository,
    private val outlineRepository: OutlineRepository,
    private val chapterRepository: ChapterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OneClickGenerationState())
    val state: StateFlow<OneClickGenerationState> = _state.asStateFlow()

    private val _effect = Channel<OneClickGenerationEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var generationJob: kotlinx.coroutines.Job? = null

    init {
        loadAvailableModels()
    }

    private fun loadAvailableModels() {
        viewModelScope.launch {
            val configs = apiConfigRepository.getAllConfigs().firstOrNull() ?: emptyList()
            val defaultConfig = apiConfigRepository.getDefaultConfig().firstOrNull()
            _state.value = _state.value.copy(
                availableModels = configs,
                selectedModel = defaultConfig ?: configs.firstOrNull()
            )
        }
    }

    fun onIntent(intent: OneClickGenerationIntent) {
        when (intent) {
            is OneClickGenerationIntent.UpdateDescription -> {
                _state.value = _state.value.copy(userDescription = intent.description)
            }
            is OneClickGenerationIntent.ToggleGenerateCharacters -> {
                _state.value = _state.value.copy(
                    generateOptions = _state.value.generateOptions.copy(
                        generateCharacters = intent.enabled
                    )
                )
            }
            is OneClickGenerationIntent.ToggleGenerateWorldSettings -> {
                _state.value = _state.value.copy(
                    generateOptions = _state.value.generateOptions.copy(
                        generateWorldSettings = intent.enabled
                    )
                )
            }
            is OneClickGenerationIntent.ToggleGenerateOutline -> {
                _state.value = _state.value.copy(
                    generateOptions = _state.value.generateOptions.copy(
                        generateOutline = intent.enabled
                    )
                )
            }
            is OneClickGenerationIntent.ToggleGenerateFirstChapter -> {
                _state.value = _state.value.copy(
                    generateOptions = _state.value.generateOptions.copy(
                        generateFirstChapter = intent.enabled
                    )
                )
            }
            is OneClickGenerationIntent.SelectModel -> {
                _state.value = _state.value.copy(selectedModel = intent.model)
            }
            OneClickGenerationIntent.StartGeneration -> startGeneration()
            OneClickGenerationIntent.CancelGeneration -> cancelGeneration()
            is OneClickGenerationIntent.UpdateBook -> {
                _state.value = _state.value.copy(generatedBook = intent.book)
            }
            is OneClickGenerationIntent.UpdateCharacter -> {
                updateCharacter(intent.character)
            }
            is OneClickGenerationIntent.UpdateWorldSetting -> {
                updateWorldSetting(intent.worldSetting)
            }
            is OneClickGenerationIntent.UpdateOutlineItem -> {
                updateOutlineItem(intent.outlineItem)
            }
            is OneClickGenerationIntent.UpdateChapterContent -> {
                _state.value = _state.value.copy(generatedChapterContent = intent.content)
            }
            OneClickGenerationIntent.ShowBookEditDialog -> {
                _state.value = _state.value.copy(editingBook = true)
            }
            OneClickGenerationIntent.HideBookEditDialog -> {
                _state.value = _state.value.copy(editingBook = false)
            }
            is OneClickGenerationIntent.ShowCharacterEditDialog -> {
                _state.value = _state.value.copy(editingCharacter = intent.character)
            }
            OneClickGenerationIntent.HideCharacterEditDialog -> {
                _state.value = _state.value.copy(editingCharacter = null)
            }
            is OneClickGenerationIntent.ShowWorldSettingEditDialog -> {
                _state.value = _state.value.copy(editingWorldSetting = intent.worldSetting)
            }
            OneClickGenerationIntent.HideWorldSettingEditDialog -> {
                _state.value = _state.value.copy(editingWorldSetting = null)
            }
            is OneClickGenerationIntent.ShowOutlineItemEditDialog -> {
                _state.value = _state.value.copy(editingOutlineItem = intent.outlineItem)
            }
            OneClickGenerationIntent.HideOutlineItemEditDialog -> {
                _state.value = _state.value.copy(editingOutlineItem = null)
            }
            is OneClickGenerationIntent.RegenerateCharacter -> {
                // TODO: 实现重新生成角色
            }
            is OneClickGenerationIntent.RegenerateWorldSetting -> {
                // TODO: 实现重新生成世界观
            }
            is OneClickGenerationIntent.RegenerateOutlineItem -> {
                // TODO: 实现重新生成大纲项
            }
            OneClickGenerationIntent.RegenerateChapterContent -> {
                // TODO: 实现重新生成章节内容
            }
            OneClickGenerationIntent.ApplyAllResults -> applyAllResults()
            OneClickGenerationIntent.NavigateBack -> {
                viewModelScope.launch {
                    _effect.send(OneClickGenerationEffect.NavigateBack)
                }
            }
            OneClickGenerationIntent.ClearError -> {
                _state.value = _state.value.copy(error = null)
            }
        }
    }

    private fun startGeneration() {
        val currentState = _state.value
        val description = currentState.userDescription.trim()
        val model = currentState.selectedModel

        if (description.isBlank()) {
            _state.value = currentState.copy(error = "请输入描述内容")
            return
        }

        if (model == null) {
            _state.value = currentState.copy(error = "请先配置AI模型")
            return
        }

        if (!currentState.generateOptions.hasAnyOption) {
            _state.value = currentState.copy(error = "请至少选择一个生成选项")
            return
        }

        generationJob = viewModelScope.launch {
            _state.value = currentState.copy(
                isGenerating = true,
                generationStage = GenerationStage.PARSING,
                progress = 0f,
                currentStageText = GenerationStage.PARSING.toDisplayText(),
                error = null
            )

            val options = com.cycling.domain.usecase.oneclick.GenerationOptions(
                generateBook = true,
                generateCharacters = currentState.generateOptions.generateCharacters,
                generateWorldSettings = currentState.generateOptions.generateWorldSettings,
                generateOutline = currentState.generateOptions.generateOutline,
                generateFirstChapter = currentState.generateOptions.generateFirstChapter
            )

            parseAndGenerateBookUseCase(model, description, options)
                .collect { progress ->
                    when (progress) {
                        is GenerationProgress.Parsing -> {
                            _state.value = _state.value.copy(
                                generationStage = GenerationStage.PARSING,
                                progress = GenerationStage.PARSING.toProgress(),
                                currentStageText = GenerationStage.PARSING.toDisplayText()
                            )
                        }
                        is GenerationProgress.GeneratingBook -> {
                            _state.value = _state.value.copy(
                                generationStage = GenerationStage.GENERATING_BOOK,
                                progress = GenerationStage.GENERATING_BOOK.toProgress(),
                                currentStageText = GenerationStage.GENERATING_BOOK.toDisplayText()
                            )
                        }
                        is GenerationProgress.GeneratingCharacters -> {
                            _state.value = _state.value.copy(
                                generationStage = GenerationStage.GENERATING_CHARACTERS,
                                progress = GenerationStage.GENERATING_CHARACTERS.toProgress(),
                                currentStageText = "正在生成角色 (${progress.count}个)..."
                            )
                        }
                        is GenerationProgress.GeneratingWorldSettings -> {
                            _state.value = _state.value.copy(
                                generationStage = GenerationStage.GENERATING_WORLD_SETTINGS,
                                progress = GenerationStage.GENERATING_WORLD_SETTINGS.toProgress(),
                                currentStageText = "正在生成世界观 (${progress.count}个)..."
                            )
                        }
                        is GenerationProgress.GeneratingOutline -> {
                            _state.value = _state.value.copy(
                                generationStage = GenerationStage.GENERATING_OUTLINE,
                                progress = GenerationStage.GENERATING_OUTLINE.toProgress(),
                                currentStageText = GenerationStage.GENERATING_OUTLINE.toDisplayText()
                            )
                        }
                        is GenerationProgress.GeneratingChapter -> {
                            _state.value = _state.value.copy(
                                generationStage = GenerationStage.GENERATING_CHAPTER,
                                progress = GenerationStage.GENERATING_CHAPTER.toProgress(),
                                currentStageText = GenerationStage.GENERATING_CHAPTER.toDisplayText()
                            )
                        }
                        is GenerationProgress.Completed -> {
                            _state.value = _state.value.copy(
                                isGenerating = false,
                                generationStage = GenerationStage.COMPLETED,
                                progress = 1f,
                                currentStageText = GenerationStage.COMPLETED.toDisplayText(),
                                generatedBook = progress.book,
                                generatedCharacters = progress.characters,
                                generatedWorldSettings = progress.worldSettings,
                                generatedOutline = progress.outline,
                                generatedChapterContent = progress.chapterContent
                            )
                        }
                        is GenerationProgress.Error -> {
                            _state.value = _state.value.copy(
                                isGenerating = false,
                                generationStage = GenerationStage.ERROR,
                                error = progress.message
                            )
                        }
                    }
                }
        }
    }

    private fun cancelGeneration() {
        generationJob?.cancel()
        generationJob = null
        _state.value = _state.value.copy(
            isGenerating = false,
            generationStage = GenerationStage.IDLE,
            progress = 0f,
            currentStageText = ""
        )
    }

    private fun updateCharacter(character: Character) {
        val currentCharacters = _state.value.generatedCharacters.toMutableList()
        val index = currentCharacters.indexOfFirst { it.id == character.id }
        if (index != -1) {
            currentCharacters[index] = character
            _state.value = _state.value.copy(
                generatedCharacters = currentCharacters,
                editingCharacter = null
            )
        }
    }

    private fun updateWorldSetting(worldSetting: WorldSetting) {
        val currentSettings = _state.value.generatedWorldSettings.toMutableList()
        val index = currentSettings.indexOfFirst { it.id == worldSetting.id }
        if (index != -1) {
            currentSettings[index] = worldSetting
            _state.value = _state.value.copy(
                generatedWorldSettings = currentSettings,
                editingWorldSetting = null
            )
        }
    }

    private fun updateOutlineItem(outlineItem: OutlineItem) {
        val currentOutline = _state.value.generatedOutline.toMutableList()
        val index = currentOutline.indexOfFirst { it.id == outlineItem.id }
        if (index != -1) {
            currentOutline[index] = outlineItem
            _state.value = _state.value.copy(
                generatedOutline = currentOutline,
                editingOutlineItem = null
            )
        }
    }

    private fun applyAllResults() {
        viewModelScope.launch {
            try {
                val currentState = _state.value
                val book = currentState.generatedBook

                Log.d("OneClickGeneration", "applyAllResults called, book: $book")

                if (book == null) {
                    Log.e("OneClickGeneration", "Book is null")
                    _effect.send(OneClickGenerationEffect.ShowError("书籍信息为空"))
                    return@launch
                }

                Log.d("OneClickGeneration", "Saving book...")
                // 1. 保存书籍
                val bookId = bookRepository.insertBook(book)
                Log.d("OneClickGeneration", "Book saved with id: $bookId")

                // 2. 保存角色
                Log.d("OneClickGeneration", "Saving ${currentState.generatedCharacters.size} characters...")
                currentState.generatedCharacters.forEach { character ->
                    characterRepository.addCharacter(character.copy(bookId = bookId))
                }

                // 3. 保存世界观设定
                Log.d("OneClickGeneration", "Saving ${currentState.generatedWorldSettings.size} world settings...")
                currentState.generatedWorldSettings.forEach { setting ->
                    worldSettingRepository.add(setting.copy(bookId = bookId))
                }

                // 4. 保存大纲（需要处理父子关系）
                Log.d("OneClickGeneration", "Saving ${currentState.generatedOutline.size} outline items...")
                val outlineIdMap = mutableMapOf<Long, Long>() // 临时id -> 真实id
                currentState.generatedOutline.sortedBy { it.level }.forEach { item ->
                    val realParentId = item.parentId?.let { outlineIdMap[it] }
                    val realId = outlineRepository.addOutlineItem(item.copy(bookId = bookId, parentId = realParentId))
                    outlineIdMap[item.id] = realId
                }

                // 5. 创建第一章
                Log.d("OneClickGeneration", "Creating first chapter...")
                // 找到第一个章级别的项（level >= 1，或者没有子项的项）
                val firstChapterOutline = currentState.generatedOutline
                    .sortedBy { it.sortOrder }
                    .firstOrNull { it.level >= 1 || it.parentId != null }
                    ?: currentState.generatedOutline.firstOrNull()
                val firstChapter = Chapter(
                    id = 0L,
                    bookId = bookId,
                    title = firstChapterOutline?.title ?: "第一章",
                    content = currentState.generatedChapterContent,
                    chapterNumber = 1,
                    wordCount = currentState.generatedChapterContent.length,
                    status = com.cycling.domain.model.ChapterStatus.DRAFT,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                val chapterId = chapterRepository.insertChapter(firstChapter)
                Log.d("OneClickGeneration", "Chapter saved with id: $chapterId")

                // 6. 更新大纲项状态和关联章节
                firstChapterOutline?.let { outline ->
                    val realOutlineId = outlineIdMap[outline.id]
                    if (realOutlineId != null) {
                        val updatedOutline = outline.copy(
                            id = realOutlineId,
                            bookId = bookId,
                            chapterId = chapterId,
                            status = com.cycling.domain.model.OutlineStatus.COMPLETED
                        )
                        outlineRepository.updateOutlineItem(updatedOutline)
                        Log.d("OneClickGeneration", "Updated outline item status to COMPLETED")
                    }
                }

                _effect.send(OneClickGenerationEffect.ShowToast("书籍创建成功！"))
                _effect.send(OneClickGenerationEffect.NavigateToEditor(chapterId))
            } catch (e: Exception) {
                Log.e("OneClickGeneration", "Error saving results", e)
                _effect.send(OneClickGenerationEffect.ShowError("保存失败: ${e.message}"))
            }
        }
    }
}
