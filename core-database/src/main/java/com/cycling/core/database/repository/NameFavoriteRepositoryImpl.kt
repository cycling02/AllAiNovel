package com.cycling.core.database.repository

import com.cycling.core.database.dao.NameFavoriteDao
import com.cycling.core.database.mapper.toEntity
import com.cycling.core.database.mapper.toModel
import com.cycling.domain.model.NameFavorite
import com.cycling.domain.model.NameType
import com.cycling.domain.repository.NameFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NameFavoriteRepositoryImpl @Inject constructor(
    private val nameFavoriteDao: NameFavoriteDao
) : NameFavoriteRepository {
    override fun getAll(): Flow<List<NameFavorite>> {
        return nameFavoriteDao.getAll().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getByType(type: NameType): Flow<List<NameFavorite>> {
        return nameFavoriteDao.getByType(type.name).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun add(nameFavorite: NameFavorite): Long {
        return nameFavoriteDao.insert(nameFavorite.toEntity())
    }

    override suspend fun delete(id: Long) {
        nameFavoriteDao.deleteById(id)
    }
}
