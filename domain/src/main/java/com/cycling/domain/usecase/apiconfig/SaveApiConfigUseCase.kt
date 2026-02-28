package com.cycling.domain.usecase.apiconfig

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.repository.ApiConfigRepository
import javax.inject.Inject

class SaveApiConfigUseCase @Inject constructor(
    private val repository: ApiConfigRepository
) {
    suspend operator fun invoke(config: ApiConfig): Long = repository.insertConfig(config)
}
