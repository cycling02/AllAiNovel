package com.cycling.core.database.repository

import com.cycling.core.database.dao.InspirationDao
import com.cycling.core.database.mapper.toEntity
import com.cycling.core.database.mapper.toModel
import com.cycling.domain.model.Inspiration
import com.cycling.domain.repository.InspirationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InspirationRepositoryImpl @Inject constructor(
    private val inspirationDao: InspirationDao
) : InspirationRepository {
    override suspend fun addInspiration(inspiration: Inspiration): Long {
        return inspirationDao.insert(inspiration.toEntity())
    }

    override suspend fun updateInspiration(inspiration: Inspiration) {
        inspirationDao.update(inspiration.toEntity())
    }

    override suspend fun deleteInspiration(id: Long) {
        inspirationDao.deleteById(id)
    }

    override fun getInspirationById(id: Long): Flow<Inspiration?> {
        return inspirationDao.getById(id).map { entity ->
            entity?.toModel()
        }
    }

    override fun getAllInspirations(): Flow<List<Inspiration>> {
        return inspirationDao.getAll().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getInspirationsByTag(tag: String): Flow<List<Inspiration>> {
        return inspirationDao.getByTag(tag).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun searchInspirations(query: String): Flow<List<Inspiration>> {
        return inspirationDao.search(query).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getAllTags(): Flow<List<String>> {
        return inspirationDao.getAllTags().map { tagStrings ->
            tagStrings.flatMap { it.split(",") }
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .distinct()
        }
    }
}
