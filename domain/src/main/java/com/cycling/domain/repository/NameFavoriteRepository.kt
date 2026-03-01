package com.cycling.domain.repository

import com.cycling.domain.model.NameFavorite
import com.cycling.domain.model.NameType
import kotlinx.coroutines.flow.Flow

interface NameFavoriteRepository {
    fun getAll(): Flow<List<NameFavorite>>
    fun getByType(type: NameType): Flow<List<NameFavorite>>
    suspend fun add(nameFavorite: NameFavorite): Long
    suspend fun delete(id: Long)
}
