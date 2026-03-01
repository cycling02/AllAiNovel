package com.cycling.domain.repository

import com.cycling.domain.model.SettingType
import com.cycling.domain.model.WorldSetting
import kotlinx.coroutines.flow.Flow

interface WorldSettingRepository {
    fun getByBookId(bookId: Long): Flow<List<WorldSetting>>
    fun getByBookIdAndType(bookId: Long, type: SettingType): Flow<List<WorldSetting>>
    fun getById(id: Long): Flow<WorldSetting?>
    suspend fun add(setting: WorldSetting): Long
    suspend fun update(setting: WorldSetting)
    suspend fun delete(id: Long)
    fun search(bookId: Long, query: String): Flow<List<WorldSetting>>
}
