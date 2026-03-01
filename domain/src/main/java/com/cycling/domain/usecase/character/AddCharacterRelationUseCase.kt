package com.cycling.domain.usecase.character

import com.cycling.domain.model.CharacterRelation
import com.cycling.domain.repository.CharacterRepository
import javax.inject.Inject

class AddCharacterRelationUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(relation: CharacterRelation): Long = repository.addCharacterRelation(relation)
}
