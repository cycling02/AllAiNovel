package com.cycling.domain.usecase.character

import com.cycling.domain.model.CharacterRelation
import com.cycling.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterRelationsUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(characterId: Long): Flow<List<CharacterRelation>> = 
        repository.getCharacterRelations(characterId)
}
