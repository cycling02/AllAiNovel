package com.cycling.domain.usecase.character

import com.cycling.domain.model.Character
import com.cycling.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(id: Long): Flow<Character?> = repository.getCharacterById(id)
}
