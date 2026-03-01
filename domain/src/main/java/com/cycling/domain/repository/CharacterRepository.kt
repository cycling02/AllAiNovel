package com.cycling.domain.repository

import com.cycling.domain.model.Character
import com.cycling.domain.model.CharacterRelation
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharactersByBookId(bookId: Long): Flow<List<Character>>
    fun getCharacterById(id: Long): Flow<Character?>
    suspend fun addCharacter(character: Character): Long
    suspend fun updateCharacter(character: Character)
    suspend fun deleteCharacter(id: Long)
    fun getCharacterRelations(characterId: Long): Flow<List<CharacterRelation>>
    suspend fun addCharacterRelation(relation: CharacterRelation): Long
    suspend fun deleteCharacterRelation(id: Long)
    suspend fun deleteRelationsByCharacterId(characterId: Long)
}
