package com.cycling.domain.usecase.worldsetting

import com.cycling.domain.model.SettingType
import com.cycling.domain.model.WorldSetting
import com.cycling.domain.repository.WorldSettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorldSettingsByTypeUseCase @Inject constructor(
    private val repository: WorldSettingRepository
) {
    operator fun invoke(bookId: Long, type: SettingType): Flow<List<WorldSetting>> = 
        repository.getByBookIdAndType(bookId, type)
}
