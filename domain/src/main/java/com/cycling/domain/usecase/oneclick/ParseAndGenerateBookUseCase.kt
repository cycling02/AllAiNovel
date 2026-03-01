package com.cycling.domain.usecase.oneclick

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.Book
import com.cycling.domain.model.Chapter
import com.cycling.domain.model.Character
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.SettingType
import com.cycling.domain.model.WorldSetting
import com.cycling.domain.repository.AiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ParseAndGenerateBookUseCase @Inject constructor(
    private val aiRepository: AiRepository
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    operator fun invoke(
        config: ApiConfig,
        description: String,
        options: GenerationOptions
    ): Flow<GenerationProgress> = flow {
        emit(GenerationProgress.Parsing)

        val parseResult = aiRepository.parseUserDescription(config, description)
        if (parseResult.isFailure) {
            emit(GenerationProgress.Error(parseResult.exceptionOrNull()?.message ?: "解析失败"))
            return@flow
        }

        val parsedData = parseResult.getOrNull() ?: ""
        val parsedBookData = parseParsedData(parsedData)

        var book: Book? = null
        val characters = mutableListOf<Character>()
        val worldSettings = mutableListOf<WorldSetting>()
        val outlineItems = mutableListOf<OutlineItem>()
        var chapterContent = ""

        if (options.generateBook) {
            emit(GenerationProgress.GeneratingBook)
            val bookResult = aiRepository.generateBookInfo(config, parsedData)
            if (bookResult.isSuccess) {
                book = parseBookInfo(bookResult.getOrNull() ?: "")
            }
        }

        if (options.generateCharacters && parsedBookData.characters.isNotEmpty()) {
            emit(GenerationProgress.GeneratingCharacters(parsedBookData.characters.size))
            val characterHints = parsedBookData.characters.map { it.toHint() }
            val charactersResult = aiRepository.generateCharactersBatch(config, characterHints)
            if (charactersResult.isSuccess) {
                characters.addAll(parseCharacters(charactersResult.getOrNull() ?: "", book?.id ?: 0L))
            }
        }

        if (options.generateWorldSettings && parsedBookData.worldSettings.isNotEmpty()) {
            emit(GenerationProgress.GeneratingWorldSettings(parsedBookData.worldSettings.size))
            val worldHints = parsedBookData.worldSettings.map { it.toHint() }
            val worldResult = aiRepository.generateWorldSettingsBatch(config, worldHints)
            if (worldResult.isSuccess) {
                worldSettings.addAll(parseWorldSettings(worldResult.getOrNull() ?: "", book?.id ?: 0L))
            }
        }

        if (options.generateOutline) {
            emit(GenerationProgress.GeneratingOutline)
            val bookInfo = book?.let { "${it.title}\n${it.description}" } ?: ""
            val outlineHint = parsedBookData.outlineStructure.firstOrNull()?.title ?: ""
            val outlineResult = aiRepository.generateOutlineStructure(config, bookInfo, outlineHint)
            if (outlineResult.isSuccess) {
                outlineItems.addAll(parseOutline(outlineResult.getOrNull() ?: "", book?.id ?: 0L))
            }
        }

        if (options.generateFirstChapter) {
            emit(GenerationProgress.GeneratingChapter)
            val bookInfo = book?.let { "${it.title}\n${it.description}" } ?: ""
            val charactersInfo = characters.joinToString("\n") { "${it.name}: ${it.background}" }
            val worldInfo = worldSettings.joinToString("\n") { "${it.name}: ${it.description}" }
            val outlineInfo = outlineItems.joinToString("\n") { it.title }
            val chapterHint = parsedBookData.firstChapterHint ?: ""

            val chapterResult = aiRepository.generateChapterContent(
                config, bookInfo, charactersInfo, worldInfo, outlineInfo, chapterHint
            )
            if (chapterResult.isSuccess) {
                chapterContent = chapterResult.getOrNull() ?: ""
            }
        }

        emit(
            GenerationProgress.Completed(
                book = book,
                characters = characters,
                worldSettings = worldSettings,
                outline = outlineItems,
                chapterContent = chapterContent
            )
        )
    }

    private fun parseParsedData(jsonString: String): ParsedBookData {
        return try {
            json.decodeFromString(ParsedBookData.serializer(), extractJsonContent(jsonString))
        } catch (e: Exception) {
            ParsedBookData()
        }
    }

    private fun parseBookInfo(jsonString: String): Book? {
        return try {
            val dto = json.decodeFromString(BookInfoDto.serializer(), extractJsonContent(jsonString))
            Book(
                id = 0L,
                title = dto.title,
                description = dto.description,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun parseCharacters(jsonString: String, bookId: Long): List<Character> {
        return try {
            val dto = json.decodeFromString(CharactersDto.serializer(), extractJsonContent(jsonString))
            dto.characters.mapIndexed { index, charDto ->
                Character(
                    id = index.toLong(),
                    bookId = bookId,
                    name = charDto.name,
                    alias = charDto.aliases.joinToString(", "),
                    gender = charDto.gender,
                    age = charDto.age?.toString() ?: "",
                    personality = charDto.personality,
                    appearance = charDto.appearance,
                    background = charDto.background,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseWorldSettings(jsonString: String, bookId: Long): List<WorldSetting> {
        return try {
            val dto = json.decodeFromString(WorldSettingsDto.serializer(), extractJsonContent(jsonString))
            dto.settings.mapIndexed { index, settingDto ->
                WorldSetting(
                    id = index.toLong(),
                    bookId = bookId,
                    type = mapCategoryToSettingType(settingDto.category),
                    name = settingDto.name,
                    description = settingDto.description,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun mapCategoryToSettingType(category: String): SettingType {
        return when (category) {
            "地点", "LOCATION" -> SettingType.LOCATION
            "势力", "FACTION" -> SettingType.FACTION
            "力量体系", "修炼体系", "POWER_SYSTEM" -> SettingType.POWER_SYSTEM
            "物品", "ITEM" -> SettingType.ITEM
            else -> SettingType.LOCATION
        }
    }

    private fun parseOutline(jsonString: String, bookId: Long): List<OutlineItem> {
        return try {
            val dto = json.decodeFromString(OutlineDto.serializer(), extractJsonContent(jsonString))
            val items = mutableListOf<OutlineItem>()
            var sortOrder = 0

            dto.items.forEach { itemDto ->
                sortOrder = processOutlineItem(itemDto, bookId, null, 0, sortOrder, items)
            }
            items
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun processOutlineItem(
        dto: OutlineItemDto,
        bookId: Long,
        parentId: Long?,
        level: Int,
        sortOrder: Int,
        items: MutableList<OutlineItem>
    ): Int {
        val item = OutlineItem(
            id = items.size.toLong(),
            bookId = bookId,
            parentId = parentId,
            title = dto.title,
            summary = dto.summary,
            level = level,
            sortOrder = sortOrder,
            status = com.cycling.domain.model.OutlineStatus.PENDING,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        items.add(item)
        var currentSortOrder = sortOrder + 1

        dto.children.forEach { childDto ->
            currentSortOrder = processOutlineItem(childDto, bookId, item.id, level + 1, currentSortOrder, items)
        }
        return currentSortOrder
    }

    private fun extractJsonContent(response: String): String {
        val trimmed = response.trim()
        if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
            return trimmed
        }
        val jsonStartIndex = trimmed.indexOf("{")
        val jsonEndIndex = trimmed.lastIndexOf("}")
        if (jsonStartIndex != -1 && jsonEndIndex != -1 && jsonEndIndex > jsonStartIndex) {
            return trimmed.substring(jsonStartIndex, jsonEndIndex + 1)
        }
        return trimmed
    }
}

data class GenerationOptions(
    val generateBook: Boolean = true,
    val generateCharacters: Boolean = true,
    val generateWorldSettings: Boolean = true,
    val generateOutline: Boolean = true,
    val generateFirstChapter: Boolean = true
)

sealed class GenerationProgress {
    object Parsing : GenerationProgress()
    object GeneratingBook : GenerationProgress()
    data class GeneratingCharacters(val count: Int) : GenerationProgress()
    data class GeneratingWorldSettings(val count: Int) : GenerationProgress()
    object GeneratingOutline : GenerationProgress()
    object GeneratingChapter : GenerationProgress()
    data class Completed(
        val book: Book?,
        val characters: List<Character>,
        val worldSettings: List<WorldSetting>,
        val outline: List<OutlineItem>,
        val chapterContent: String
    ) : GenerationProgress()
    data class Error(val message: String) : GenerationProgress()
}

@Serializable
private data class ParsedBookData(
    val bookTitle: String? = null,
    val bookGenre: String? = null,
    val bookDescription: String? = null,
    val characters: List<ParsedCharacterDto> = emptyList(),
    val worldSettings: List<ParsedWorldSettingDto> = emptyList(),
    val outlineStructure: List<ParsedOutlineItemDto> = emptyList(),
    val firstChapterHint: String? = null
)

@Serializable
private data class ParsedCharacterDto(
    val name: String,
    val identity: String? = null,
    val attributes: Map<String, String> = emptyMap(),
    val relationships: List<Pair<String, String>> = emptyList()
) {
    fun toHint(): String {
        val attrs = attributes.entries.joinToString(", ") { "${it.key}: ${it.value}" }
        val rels = relationships.joinToString(", ") { "${it.first}=${it.second}" }
        return "$name (${identity ?: "角色"}) - $attrs - 关系: $rels"
    }
}

@Serializable
private data class ParsedWorldSettingDto(
    val category: String,
    val name: String,
    val description: String? = null
) {
    fun toHint(): String = "[$category] $name: ${description ?: ""}"
}

@Serializable
private data class ParsedOutlineItemDto(
    val level: Int = 1,
    val title: String,
    val summary: String? = null,
    val children: List<ParsedOutlineItemDto> = emptyList()
)

@Serializable
private data class BookInfoDto(
    val title: String,
    val description: String = "",
    val genre: String = ""
)

@Serializable
private data class CharactersDto(
    val characters: List<CharacterDto> = emptyList()
)

@Serializable
private data class CharacterDto(
    val name: String,
    val aliases: List<String> = emptyList(),
    val gender: String = "",
    val age: Int? = null,
    val personality: String = "",
    val appearance: String = "",
    val background: String = ""
)

@Serializable
private data class WorldSettingsDto(
    val settings: List<WorldSettingDto> = emptyList()
)

@Serializable
private data class WorldSettingDto(
    val category: String,
    val name: String,
    val description: String = ""
)

@Serializable
private data class OutlineDto(
    val items: List<OutlineItemDto> = emptyList()
)

@Serializable
private data class OutlineItemDto(
    val title: String,
    val summary: String = "",
    val children: List<OutlineItemDto> = emptyList()
)
