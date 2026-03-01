package com.cycling.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cycling.core.database.entity.OutlineItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OutlineDao {
    @Query("SELECT * FROM outline_items WHERE bookId = :bookId ORDER BY sortOrder ASC")
    fun getOutlineItemsByBookId(bookId: Long): Flow<List<OutlineItemEntity>>

    @Query("SELECT * FROM outline_items WHERE id = :id")
    fun getOutlineItemById(id: Long): Flow<OutlineItemEntity?>

    @Query("SELECT * FROM outline_items WHERE parentId = :parentId ORDER BY sortOrder ASC")
    fun getOutlineItemsByParentId(parentId: Long): Flow<List<OutlineItemEntity>>

    @Query("SELECT * FROM outline_items WHERE parentId IS NULL AND bookId = :bookId ORDER BY sortOrder ASC")
    fun getRootOutlineItems(bookId: Long): Flow<List<OutlineItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(outlineItem: OutlineItemEntity): Long

    @Update
    suspend fun update(outlineItem: OutlineItemEntity)

    @Delete
    suspend fun delete(outlineItem: OutlineItemEntity)

    @Query("DELETE FROM outline_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM outline_items WHERE parentId = :parentId")
    suspend fun deleteByParentId(parentId: Long)

    @Query("DELETE FROM outline_items WHERE id = :id OR parentId = :id")
    suspend fun deleteWithChildren(id: Long)
}
