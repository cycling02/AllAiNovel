package com.cycling.domain.model

data class ApiConfig(
    val id: Long = 0,
    val name: String,
    val provider: String,
    val apiKey: String,
    val baseUrl: String,
    val model: String,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
