package com.cycling.core.database.repository

import com.cycling.core.database.dao.OutlineDao
import com.cycling.core.database.mapper.toModel
import com.cycling.core.database.mapper.toEntity
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.repository.OutlineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutlineRepositoryImpl @Inject constructor(
    private val outlineDao: OutlineDao
) : OutlineRepository {
    override fun getOutlineByBookId(bookId: Long): Flow<List<OutlineItem>> {
        return outlineDao.getOutlineItemsByBookId(bookId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getOutlineItemById(id: Long): Flow<OutlineItem?> {
        return outlineDao.getOutlineItemById(id).map { entity ->
            entity?.toModel()
        }
    }

    override suspend fun addOutlineItem(item: OutlineItem): Long {
        return outlineDao.insert(item.toEntity())
    }

    override suspend fun updateOutlineItem(item: OutlineItem) {
        outlineDao.update(item.toEntity())
    }

    override suspend fun deleteOutlineItem(id: Long) {
        outlineDao.deleteById(id)
    }

    override suspend fun deleteOutlineItemWithChildren(id: Long) {
        outlineDao.deleteWithChildren(id)
    }

    override fun getChildrenByParentId(parentId: Long): Flow<List<OutlineItem>> {
        return outlineDao.getOutlineItemsByParentId(parentId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun reorderOutlineItems(items: List<OutlineItem>) {
        items.forEach { item ->
            outlineDao.update(item.toEntity())
        }
    }
}
