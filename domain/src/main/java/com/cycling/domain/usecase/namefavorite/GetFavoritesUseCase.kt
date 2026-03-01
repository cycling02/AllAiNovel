package com.cycling.domain.usecase.namefavorite

import com.cycling.domain.model.NameFavorite
import com.cycling.domain.repository.NameFavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: NameFavoriteRepository
) {
    operator fun invoke(): Flow<List<NameFavorite>> = repository.getAll()
}
