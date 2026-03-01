package com.cycling.feature.oneclick

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.Book
import com.cycling.domain.model.Character
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.WorldSetting

data class OneClickGenerationState(
    // 输入状态
    val userDescription: String = "",
    val generateOptions: GenerationOptions = GenerationOptions(),
    val selectedModel: ApiConfig? = null,
    val availableModels: List<ApiConfig> = emptyList(),

    // 生成状态
    val isGenerating: Boolean = false,
    val generationStage: GenerationStage = GenerationStage.IDLE,
    val progress: Float = 0f,
    val currentStageText: String = "",

    // 生成结果
    val generatedBook: Book? = null,
    val generatedCharacters: List<Character> = emptyList(),
    val generatedWorldSettings: List<WorldSetting> = emptyList(),
    val generatedOutline: List<OutlineItem> = emptyList(),
    val generatedChapterContent: String = "",

    // 编辑状态
    val editingBook: Boolean = false,
    val editingCharacter: Character? = null,
    val editingWorldSetting: WorldSetting? = null,
    val editingOutlineItem: OutlineItem? = null,

    // 错误状态
    val error: String? = null
)

data class GenerationOptions(
    val generateCharacters: Boolean = true,
    val generateWorldSettings: Boolean = true,
    val generateOutline: Boolean = true,
    val generateFirstChapter: Boolean = true
) {
    val hasAnyOption: Boolean
        get() = generateCharacters || generateWorldSettings || generateOutline || generateFirstChapter
}

enum class GenerationStage {
    IDLE,
    PARSING,
    GENERATING_BOOK,
    GENERATING_CHARACTERS,
    GENERATING_WORLD_SETTINGS,
    GENERATING_OUTLINE,
    GENERATING_CHAPTER,
    COMPLETED,
    ERROR;

    fun toDisplayText(): String = when (this) {
        IDLE -> "准备就绪"
        PARSING -> "正在解析描述..."
        GENERATING_BOOK -> "正在生成书籍信息..."
        GENERATING_CHARACTERS -> "正在生成角色..."
        GENERATING_WORLD_SETTINGS -> "正在生成世界观..."
        GENERATING_OUTLINE -> "正在生成大纲..."
        GENERATING_CHAPTER -> "正在生成第一章..."
        COMPLETED -> "生成完成！"
        ERROR -> "生成出错"
    }

    fun toProgress(): Float = when (this) {
        IDLE -> 0f
        PARSING -> 0.1f
        GENERATING_BOOK -> 0.2f
        GENERATING_CHARACTERS -> 0.4f
        GENERATING_WORLD_SETTINGS -> 0.55f
        GENERATING_OUTLINE -> 0.7f
        GENERATING_CHAPTER -> 0.85f
        COMPLETED -> 1f
        ERROR -> 0f
    }
}
