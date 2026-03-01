package com.cycling.core.database.repository

import com.cycling.core.database.dao.WorldSettingDao
import com.cycling.core.database.mapper.toEntity
import com.cycling.core.database.mapper.toModel
import com.cycling.domain.model.SettingType
import com.cycling.domain.model.WorldSetting
import com.cycling.domain.repository.WorldSettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldSettingRepositoryImpl @Inject constructor(
    private val worldSettingDao: WorldSettingDao
) : WorldSettingRepository {
    override fun getByBookId(bookId: Long): Flow<List<WorldSetting>> {
        return worldSettingDao.getByBookId(bookId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getByBookIdAndType(bookId: Long, type: SettingType): Flow<List<WorldSetting>> {
        return worldSettingDao.getByBookIdAndType(bookId, type.name).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getById(id: Long): Flow<WorldSetting?> {
        return worldSettingDao.getById(id).map { entity ->
            entity?.toModel()
        }
    }

    override suspend fun add(setting: WorldSetting): Long {
        return worldSettingDao.insert(setting.toEntity())
    }

    override suspend fun update(setting: WorldSetting) {
        worldSettingDao.update(setting.toEntity())
    }

    override suspend fun delete(id: Long) {
        worldSettingDao.deleteById(id)
    }

    override fun search(bookId: Long, query: String): Flow<List<WorldSetting>> {
        return worldSettingDao.search(bookId, query).map { entities ->
            entities.map { it.toModel() }
        }
    }
}
