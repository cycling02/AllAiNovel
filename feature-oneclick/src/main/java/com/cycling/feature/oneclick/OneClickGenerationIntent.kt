package com.cycling.feature.oneclick

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.Book
import com.cycling.domain.model.Character
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.WorldSetting

sealed class OneClickGenerationIntent {
    // 输入相关
    data class UpdateDescription(val description: String) : OneClickGenerationIntent()
    data class ToggleGenerateCharacters(val enabled: Boolean) : OneClickGenerationIntent()
    data class ToggleGenerateWorldSettings(val enabled: Boolean) : OneClickGenerationIntent()
    data class ToggleGenerateOutline(val enabled: Boolean) : OneClickGenerationIntent()
    data class ToggleGenerateFirstChapter(val enabled: Boolean) : OneClickGenerationIntent()
    data class SelectModel(val model: ApiConfig) : OneClickGenerationIntent()

    // 生成相关
    object StartGeneration : OneClickGenerationIntent()
    object CancelGeneration : OneClickGenerationIntent()

    // 编辑相关
    data class UpdateBook(val book: Book) : OneClickGenerationIntent()
    data class UpdateCharacter(val character: Character) : OneClickGenerationIntent()
    data class UpdateWorldSetting(val worldSetting: WorldSetting) : OneClickGenerationIntent()
    data class UpdateOutlineItem(val outlineItem: OutlineItem) : OneClickGenerationIntent()
    data class UpdateChapterContent(val content: String) : OneClickGenerationIntent()

    // 显示编辑对话框
    object ShowBookEditDialog : OneClickGenerationIntent()
    object HideBookEditDialog : OneClickGenerationIntent()
    data class ShowCharacterEditDialog(val character: Character) : OneClickGenerationIntent()
    object HideCharacterEditDialog : OneClickGenerationIntent()
    data class ShowWorldSettingEditDialog(val worldSetting: WorldSetting) : OneClickGenerationIntent()
    object HideWorldSettingEditDialog : OneClickGenerationIntent()
    data class ShowOutlineItemEditDialog(val outlineItem: OutlineItem) : OneClickGenerationIntent()
    object HideOutlineItemEditDialog : OneClickGenerationIntent()

    // 重新生成
    data class RegenerateCharacter(val character: Character) : OneClickGenerationIntent()
    data class RegenerateWorldSetting(val worldSetting: WorldSetting) : OneClickGenerationIntent()
    data class RegenerateOutlineItem(val outlineItem: OutlineItem) : OneClickGenerationIntent()
    object RegenerateChapterContent : OneClickGenerationIntent()

    // 应用结果
    object ApplyAllResults : OneClickGenerationIntent()

    // 导航
    object NavigateBack : OneClickGenerationIntent()

    // 错误
    object ClearError : OneClickGenerationIntent()
}
