package com.cycling.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cycling.core.database.entity.ApiConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApiConfigDao {
    @Query("SELECT * FROM api_configs ORDER BY createdAt DESC")
    fun getAllConfigs(): Flow<List<ApiConfigEntity>>

    @Query("SELECT * FROM api_configs WHERE id = :id")
    fun getConfigById(id: Long): Flow<ApiConfigEntity?>

    @Query("SELECT * FROM api_configs WHERE isDefault = 1 LIMIT 1")
    fun getDefaultConfig(): Flow<ApiConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(config: ApiConfigEntity): Long

    @Update
    suspend fun update(config: ApiConfigEntity)

    @Delete
    suspend fun delete(config: ApiConfigEntity)

    @Query("UPDATE api_configs SET isDefault = 0")
    suspend fun clearDefaultConfig()

    @Query("UPDATE api_configs SET isDefault = 1 WHERE id = :id")
    suspend fun setDefaultConfig(id: Long)
}
