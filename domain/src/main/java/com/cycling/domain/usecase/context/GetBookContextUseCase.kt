package com.cycling.domain.usecase.context

import com.cycling.domain.model.Book
import com.cycling.domain.model.Character
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.WorldSetting
import com.cycling.domain.repository.BookRepository
import com.cycling.domain.repository.CharacterRepository
import com.cycling.domain.repository.OutlineRepository
import com.cycling.domain.repository.WorldSettingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class BookContext(
    val book: Book?,
    val characters: List<Character>,
    val worldSettings: List<WorldSetting>,
    val outlineItems: List<OutlineItem>
) {
    fun toPrompt(): String {
        val sb = StringBuilder()
        
        book?.let { b ->
            sb.appendLine("【书籍信息】")
            sb.appendLine("书名：${b.title}")
            if (b.description.isNotBlank()) {
                sb.appendLine("简介：${b.description}")
            }
            sb.appendLine()
        }
        
        if (characters.isNotEmpty()) {
            sb.appendLine("【角色设定】")
            characters.take(10).forEach { char ->
                sb.appendLine("角色：${char.name}")
                if (char.alias.isNotBlank()) sb.appendLine("  别名：${char.alias}")
                if (char.gender.isNotBlank()) sb.appendLine("  性别：${char.gender}")
                if (char.age.isNotBlank()) sb.appendLine("  年龄：${char.age}")
                if (char.personality.isNotBlank()) sb.appendLine("  性格：${char.personality}")
                if (char.appearance.isNotBlank()) sb.appendLine("  外貌：${char.appearance}")
                if (char.background.isNotBlank()) sb.appendLine("  背景：${char.background}")
                sb.appendLine()
            }
            if (characters.size > 10) {
                sb.appendLine("... 还有 ${characters.size - 10} 个角色")
                sb.appendLine()
            }
        }
        
        if (worldSettings.isNotEmpty()) {
            sb.appendLine("【世界观设定】")
            worldSettings.take(10).forEach { setting ->
                sb.appendLine("${setting.type.displayName}：${setting.name}")
                if (setting.description.isNotBlank()) {
                    sb.appendLine("  描述：${setting.description}")
                }
                sb.appendLine()
            }
            if (worldSettings.size > 10) {
                sb.appendLine("... 还有 ${worldSettings.size - 10} 个设定")
                sb.appendLine()
            }
        }
        
        if (outlineItems.isNotEmpty()) {
            sb.appendLine("【故事大纲】")
            outlineItems.filter { it.level <= 1 }.take(10).forEach { outline ->
                val prefix = when (outline.level) {
                    0 -> "卷"
                    1 -> "  章"
                    else -> "    节"
                }
                sb.appendLine("$prefix ${outline.title}")
                if (outline.summary.isNotBlank() && outline.level <= 1) {
                    sb.appendLine("    概要：${outline.summary.take(100)}")
                }
            }
            sb.appendLine()
        }
        
        return sb.toString()
    }
    
    fun isEmpty(): Boolean {
        return characters.isEmpty() && worldSettings.isEmpty() && outlineItems.isEmpty()
    }
}

class GetBookContextUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val characterRepository: CharacterRepository,
    private val worldSettingRepository: WorldSettingRepository,
    private val outlineRepository: OutlineRepository
) {
    suspend operator fun invoke(bookId: Long): BookContext {
        val book = bookRepository.getBookById(bookId).first()
        val characters = characterRepository.getCharactersByBookId(bookId).first()
        val worldSettings = worldSettingRepository.getWorldSettingsByBookId(bookId).first()
        val outlineItems = outlineRepository.getOutlineByBookId(bookId).first()
        
        return BookContext(
            book = book,
            characters = characters,
            worldSettings = worldSettings,
            outlineItems = outlineItems
        )
    }
}
