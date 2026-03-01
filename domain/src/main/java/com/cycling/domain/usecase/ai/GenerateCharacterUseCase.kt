package com.cycling.domain.usecase.ai

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.Character
import com.cycling.domain.repository.AiRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GenerateCharacterUseCase @Inject constructor(
    private val repository: AiRepository
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend operator fun invoke(
        config: ApiConfig,
        bookId: Long,
        characterType: String?,
        gender: String?,
        description: String?,
        count: Int = 1
    ): Result<List<Character>> {
        val result = repository.generateCharacter(
            config = config,
            characterType = characterType,
            gender = gender,
            description = description,
            count = count
        )

        return result.mapCatching { content ->
            parseCharacterJson(content, bookId)
        }
    }

    private fun parseCharacterJson(content: String, bookId: Long): List<Character> {
        val jsonContent = extractJsonContent(content)
        
        val dto = json.decodeFromString<CharacterGenerationDto>(jsonContent)
        
        return dto.characters.mapIndexed { index, charDto ->
            Character(
                bookId = bookId,
                name = charDto.name,
                alias = charDto.alias,
                gender = charDto.gender,
                age = charDto.age,
                personality = charDto.personality,
                appearance = charDto.appearance,
                background = charDto.background
            )
        }
    }

    private fun extractJsonContent(content: String): String {
        val startIndex = content.indexOf('{')
        val endIndex = content.lastIndexOf('}')
        
        if (startIndex == -1 || endIndex == -1 || startIndex > endIndex) {
            throw IllegalArgumentException("无法在返回内容中找到有效的JSON")
        }
        
        return content.substring(startIndex, endIndex + 1)
    }

    @kotlinx.serialization.Serializable
    private data class CharacterGenerationDto(
        val characters: List<CharacterDto>
    )

    @kotlinx.serialization.Serializable
    private data class CharacterDto(
        val name: String,
        val alias: String = "",
        val gender: String = "",
        val age: String = "",
        val personality: String = "",
        val appearance: String = "",
        val background: String = ""
    )
}
