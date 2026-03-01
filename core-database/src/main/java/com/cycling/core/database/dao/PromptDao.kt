package com.cycling.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cycling.core.database.entity.PromptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prompt: PromptEntity): Long

    @Update
    suspend fun update(prompt: PromptEntity)

    @Delete
    suspend fun delete(prompt: PromptEntity)

    @Query("SELECT * FROM prompts WHERE id = :id")
    fun getById(id: Long): Flow<PromptEntity?>

    @Query("SELECT * FROM prompts WHERE id = :id")
    suspend fun getByIdSync(id: Long): PromptEntity?

    @Query("SELECT * FROM prompts ORDER BY createdAt DESC")
    fun getAll(): Flow<List<PromptEntity>>

    @Query("SELECT * FROM prompts WHERE category = :category ORDER BY createdAt DESC")
    fun getByCategory(category: String): Flow<List<PromptEntity>>

    @Query("SELECT * FROM prompts WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavorites(): Flow<List<PromptEntity>>

    @Query("SELECT * FROM prompts WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun search(query: String): Flow<List<PromptEntity>>

    @Query("UPDATE prompts SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long)

    @Query("DELETE FROM prompts WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM prompts WHERE isSystem = 1")
    suspend fun getSystemPromptsCount(): Int
}
