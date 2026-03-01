package com.cycling.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cycling.core.database.entity.InspirationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InspirationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inspiration: InspirationEntity): Long

    @Update
    suspend fun update(inspiration: InspirationEntity)

    @Delete
    suspend fun delete(inspiration: InspirationEntity)

    @Query("SELECT * FROM inspirations WHERE id = :id")
    fun getById(id: Long): Flow<InspirationEntity?>

    @Query("SELECT * FROM inspirations ORDER BY createdAt DESC")
    fun getAll(): Flow<List<InspirationEntity>>

    @Query("SELECT * FROM inspirations WHERE tags LIKE '%' || :tag || '%' ORDER BY createdAt DESC")
    fun getByTag(tag: String): Flow<List<InspirationEntity>>

    @Query("SELECT * FROM inspirations WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun search(query: String): Flow<List<InspirationEntity>>

    @Query("SELECT DISTINCT tags FROM inspirations WHERE tags != ''")
    fun getAllTags(): Flow<List<String>>

    @Query("DELETE FROM inspirations WHERE id = :id")
    suspend fun deleteById(id: Long)
}
