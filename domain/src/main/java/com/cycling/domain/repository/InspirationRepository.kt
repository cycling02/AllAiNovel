package com.cycling.domain.repository

import com.cycling.domain.model.Inspiration
import kotlinx.coroutines.flow.Flow

interface InspirationRepository {
    suspend fun addInspiration(inspiration: Inspiration): Long
    suspend fun updateInspiration(inspiration: Inspiration)
    suspend fun deleteInspiration(id: Long)
    fun getInspirationById(id: Long): Flow<Inspiration?>
    fun getAllInspirations(): Flow<List<Inspiration>>
    fun getInspirationsByTag(tag: String): Flow<List<Inspiration>>
    fun searchInspirations(query: String): Flow<List<Inspiration>>
    fun getAllTags(): Flow<List<String>>
}
