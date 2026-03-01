package com.cycling.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cycling.core.database.entity.NameFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NameFavoriteDao {
    @Query("SELECT * FROM name_favorites ORDER BY createdAt DESC")
    fun getAll(): Flow<List<NameFavoriteEntity>>

    @Query("SELECT * FROM name_favorites WHERE type = :type ORDER BY createdAt DESC")
    fun getByType(type: String): Flow<List<NameFavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(nameFavorite: NameFavoriteEntity): Long

    @Delete
    suspend fun delete(nameFavorite: NameFavoriteEntity)

    @Query("DELETE FROM name_favorites WHERE id = :id")
    suspend fun deleteById(id: Long)
}
