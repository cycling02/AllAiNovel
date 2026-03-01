package com.cycling.domain.usecase.worldsetting

import com.cycling.domain.repository.WorldSettingRepository
import javax.inject.Inject

class DeleteWorldSettingUseCase @Inject constructor(
    private val repository: WorldSettingRepository
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
