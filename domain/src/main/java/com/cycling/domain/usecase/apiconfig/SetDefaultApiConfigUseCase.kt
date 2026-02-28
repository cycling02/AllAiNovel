package com.cycling.domain.usecase.apiconfig

import com.cycling.domain.repository.ApiConfigRepository
import javax.inject.Inject

class SetDefaultApiConfigUseCase @Inject constructor(
    private val repository: ApiConfigRepository
) {
    suspend operator fun invoke(id: Long) = repository.setDefaultConfig(id)
}
