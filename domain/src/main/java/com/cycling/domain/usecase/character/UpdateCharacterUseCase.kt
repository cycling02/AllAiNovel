package com.cycling.domain.usecase.character

import com.cycling.domain.model.Character
import com.cycling.domain.repository.CharacterRepository
import javax.inject.Inject

class UpdateCharacterUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(character: Character) = repository.updateCharacter(character)
}
