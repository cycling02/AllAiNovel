package com.cycling.domain.usecase.namefavorite

import com.cycling.domain.model.NameFavorite
import com.cycling.domain.repository.NameFavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: NameFavoriteRepository
) {
    suspend operator fun invoke(nameFavorite: NameFavorite): Long = repository.add(nameFavorite)
}
