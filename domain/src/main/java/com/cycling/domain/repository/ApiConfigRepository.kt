package com.cycling.domain.repository

import com.cycling.domain.model.ApiConfig
import kotlinx.coroutines.flow.Flow

interface ApiConfigRepository {
    fun getAllConfigs(): Flow<List<ApiConfig>>
    fun getConfigById(id: Long): Flow<ApiConfig?>
    fun getDefaultConfig(): Flow<ApiConfig?>
    suspend fun insertConfig(config: ApiConfig): Long
    suspend fun updateConfig(config: ApiConfig)
    suspend fun deleteConfig(config: ApiConfig)
    suspend fun setDefaultConfig(id: Long)
}
