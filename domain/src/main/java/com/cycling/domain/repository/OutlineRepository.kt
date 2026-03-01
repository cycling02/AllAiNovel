package com.cycling.domain.repository

import com.cycling.domain.model.OutlineItem
import kotlinx.coroutines.flow.Flow

interface OutlineRepository {
    fun getOutlineByBookId(bookId: Long): Flow<List<OutlineItem>>
    fun getOutlineItemById(id: Long): Flow<OutlineItem?>
    suspend fun addOutlineItem(item: OutlineItem): Long
    suspend fun updateOutlineItem(item: OutlineItem)
    suspend fun deleteOutlineItem(id: Long)
    suspend fun deleteOutlineItemWithChildren(id: Long)
    fun getChildrenByParentId(parentId: Long): Flow<List<OutlineItem>>
    suspend fun reorderOutlineItems(items: List<OutlineItem>)
}
