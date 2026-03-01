package com.cycling.domain.usecase.worldsetting

import com.cycling.domain.model.WorldSetting
import com.cycling.domain.repository.WorldSettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorldSettingsByBookIdUseCase @Inject constructor(
    private val repository: WorldSettingRepository
) {
    operator fun invoke(bookId: Long): Flow<List<WorldSetting>> = repository.getByBookId(bookId)
}
