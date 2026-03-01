package com.cycling.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cycling.core.database.entity.WorldSettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldSettingDao {
    @Query("SELECT * FROM world_settings WHERE bookId = :bookId ORDER BY createdAt DESC")
    fun getByBookId(bookId: Long): Flow<List<WorldSettingEntity>>

    @Query("SELECT * FROM world_settings WHERE bookId = :bookId AND type = :type ORDER BY createdAt DESC")
    fun getByBookIdAndType(bookId: Long, type: String): Flow<List<WorldSettingEntity>>

    @Query("SELECT * FROM world_settings WHERE id = :id")
    fun getById(id: Long): Flow<WorldSettingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: WorldSettingEntity): Long

    @Update
    suspend fun update(setting: WorldSettingEntity)

    @Delete
    suspend fun delete(setting: WorldSettingEntity)

    @Query("DELETE FROM world_settings WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM world_settings WHERE bookId = :bookId AND (name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%') ORDER BY createdAt DESC")
    fun search(bookId: Long, query: String): Flow<List<WorldSettingEntity>>
}
