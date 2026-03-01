package com.cycling.domain.usecase.character

import com.cycling.domain.repository.CharacterRepository
import javax.inject.Inject

class DeleteCharacterRelationUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteCharacterRelation(id)
}
