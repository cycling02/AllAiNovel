package com.cycling.domain.usecase.apiconfig

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.repository.ApiConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDefaultApiConfigUseCase @Inject constructor(
    private val repository: ApiConfigRepository
) {
    operator fun invoke(): Flow<ApiConfig?> = repository.getDefaultConfig()
}
