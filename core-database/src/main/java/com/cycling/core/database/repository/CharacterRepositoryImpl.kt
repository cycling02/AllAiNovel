package com.cycling.core.database.repository

import com.cycling.core.database.dao.CharacterDao
import com.cycling.core.database.dao.CharacterRelationDao
import com.cycling.core.database.mapper.toEntity
import com.cycling.core.database.mapper.toModel
import com.cycling.domain.model.Character
import com.cycling.domain.model.CharacterRelation
import com.cycling.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao,
    private val characterRelationDao: CharacterRelationDao
) : CharacterRepository {
    override fun getCharactersByBookId(bookId: Long): Flow<List<Character>> {
        return characterDao.getCharactersByBookId(bookId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getCharacterById(id: Long): Flow<Character?> {
        return characterDao.getCharacterById(id).map { entity ->
            entity?.toModel()
        }
    }

    override suspend fun addCharacter(character: Character): Long {
        return characterDao.insert(character.toEntity())
    }

    override suspend fun updateCharacter(character: Character) {
        characterDao.update(character.toEntity())
    }

    override suspend fun deleteCharacter(id: Long) {
        characterDao.deleteById(id)
    }

    override fun getCharacterRelations(characterId: Long): Flow<List<CharacterRelation>> {
        return characterRelationDao.getRelationsForCharacter(characterId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun addCharacterRelation(relation: CharacterRelation): Long {
        return characterRelationDao.insert(relation.toEntity())
    }

    override suspend fun deleteCharacterRelation(id: Long) {
        characterRelationDao.deleteById(id)
    }

    override suspend fun deleteRelationsByCharacterId(characterId: Long) {
        characterRelationDao.deleteAllRelationsForCharacter(characterId)
    }
}
