package com.cycling.domain.usecase.worldsetting

import com.cycling.domain.model.WorldSetting
import com.cycling.domain.repository.WorldSettingRepository
import javax.inject.Inject

class UpdateWorldSettingUseCase @Inject constructor(
    private val repository: WorldSettingRepository
) {
    suspend operator fun invoke(setting: WorldSetting) = repository.update(setting)
}
