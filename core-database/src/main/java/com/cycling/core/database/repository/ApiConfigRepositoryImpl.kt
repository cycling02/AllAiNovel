package com.cycling.core.database.repository

import com.cycling.core.database.dao.ApiConfigDao
import com.cycling.core.database.mapper.toModel
import com.cycling.core.database.mapper.toEntity
import com.cycling.domain.model.ApiConfig
import com.cycling.domain.repository.ApiConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiConfigRepositoryImpl @Inject constructor(
    private val apiConfigDao: ApiConfigDao
) : ApiConfigRepository {
    override fun getAllConfigs(): Flow<List<ApiConfig>> {
        return apiConfigDao.getAllConfigs().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getConfigById(id: Long): Flow<ApiConfig?> {
        return apiConfigDao.getConfigById(id).map { entity ->
            entity?.toModel()
        }
    }

    override fun getDefaultConfig(): Flow<ApiConfig?> {
        return apiConfigDao.getDefaultConfig().map { entity ->
            entity?.toModel()
        }
    }

    override suspend fun insertConfig(config: ApiConfig): Long {
        return apiConfigDao.insert(config.toEntity())
    }

    override suspend fun updateConfig(config: ApiConfig) {
        apiConfigDao.update(config.toEntity())
    }

    override suspend fun deleteConfig(config: ApiConfig) {
        apiConfigDao.delete(config.toEntity())
    }

    override suspend fun setDefaultConfig(id: Long) {
        apiConfigDao.clearDefaultConfig()
        apiConfigDao.setDefaultConfig(id)
    }
}