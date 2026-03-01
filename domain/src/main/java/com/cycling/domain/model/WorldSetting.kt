package com.cycling.domain.model

data class WorldSetting(
    val id: Long = 0,
    val bookId: Long,
    val type: SettingType,
    val name: String,
    val description: String = "",
    val details: String = "",
    val customAttributes: Map<String, String> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
