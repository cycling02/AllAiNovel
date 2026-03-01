package com.cycling.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "name_favorites")
data class NameFavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: String,
    val createdAt: Long = System.currentTimeMillis()
)
