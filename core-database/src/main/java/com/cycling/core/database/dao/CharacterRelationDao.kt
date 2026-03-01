package com.cycling.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cycling.core.database.entity.CharacterRelationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterRelationDao {
    @Query("SELECT * FROM character_relations WHERE characterId = :characterId OR relatedCharacterId = :characterId")
    fun getRelationsForCharacter(characterId: Long): Flow<List<CharacterRelationEntity>>

    @Query("SELECT * FROM character_relations WHERE id = :id")
    fun getRelationById(id: Long): Flow<CharacterRelationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(relation: CharacterRelationEntity): Long

    @Delete
    suspend fun delete(relation: CharacterRelationEntity)

    @Query("DELETE FROM character_relations WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM character_relations WHERE characterId = :characterId OR relatedCharacterId = :characterId")
    suspend fun deleteAllRelationsForCharacter(characterId: Long)
}
