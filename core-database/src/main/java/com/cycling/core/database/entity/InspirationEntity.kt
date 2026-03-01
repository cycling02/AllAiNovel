package com.cycling.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inspirations")
data class InspirationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val tags: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
