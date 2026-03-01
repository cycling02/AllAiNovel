package com.cycling.domain.usecase.ai

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.OutlineStatus
import com.cycling.domain.repository.AiRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GenerateOutlineUseCase @Inject constructor(
    private val repository: AiRepository
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    suspend operator fun invoke(
        config: ApiConfig,
        bookId: Long,
        topic: String,
        summary: String,
        chapterCount: Int = 20,
        levelCount: Int = 2
    ): Result<List<OutlineItem>> {
        val apiResult = repository.generateOutline(
            config = config,
            topic = topic,
            summary = summary,
            chapterCount = chapterCount,
            levelCount = levelCount
        )

        return apiResult.mapCatching { jsonString ->
            parseOutlineJson(jsonString, bookId)
        }
    }

    private fun parseOutlineJson(jsonString: String, bookId: Long): List<OutlineItem> {
        val cleanJson = extractJsonContent(jsonString)
        val result = json.decodeFromString<OutlineGenerationDto>(cleanJson)
        
        val outlineItems = mutableListOf<OutlineItem>()
        var sortOrder = 0
        
        result.items.forEach { itemDto ->
            sortOrder = processOutlineItemDto(
                itemDto = itemDto,
                bookId = bookId,
                parentLevel = -1,
                sortOrder = sortOrder,
                outlineItems = outlineItems,
                lastItemByLevel = mutableMapOf()
            )
        }
        
        return outlineItems
    }

    private fun processOutlineItemDto(
        itemDto: OutlineItemDto,
        bookId: Long,
        parentLevel: Int,
        sortOrder: Int,
        outlineItems: MutableList<OutlineItem>,
        lastItemByLevel: MutableMap<Int, Int>
    ): Int {
        var currentSortOrder = sortOrder
        
        val actualLevel = itemDto.level - 1
        val parentIdIndex = lastItemByLevel[parentLevel]
        
        val item = OutlineItem(
            id = outlineItems.size.toLong(),
            bookId = bookId,
            parentId = if (parentLevel >= 0 && parentIdIndex != null) parentIdIndex.toLong() else null,
            title = itemDto.title,
            summary = itemDto.summary,
            level = actualLevel,
            sortOrder = currentSortOrder,
            status = OutlineStatus.PENDING,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        outlineItems.add(item)
        lastItemByLevel[actualLevel] = outlineItems.size - 1
        currentSortOrder++
        
        itemDto.children.forEach { childDto ->
            currentSortOrder = processOutlineItemDto(
                itemDto = childDto,
                bookId = bookId,
                parentLevel = actualLevel,
                sortOrder = currentSortOrder,
                outlineItems = outlineItems,
                lastItemByLevel = lastItemByLevel
            )
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

@Serializable
private data class OutlineGenerationDto(
    val items: List<OutlineItemDto>
)

@Serializable
private data class OutlineItemDto(
    val title: String,
    val summary: String = "",
    val level: Int = 1,
    val children: List<OutlineItemDto> = emptyList()
)
