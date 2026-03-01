package com.cycling.domain.usecase.namefavorite

import com.cycling.domain.repository.NameFavoriteRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val repository: NameFavoriteRepository
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
