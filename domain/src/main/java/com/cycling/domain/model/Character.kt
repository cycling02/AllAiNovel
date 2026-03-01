package com.cycling.domain.model

data class Character(
    val id: Long = 0,
    val bookId: Long,
    val name: String,
    val alias: String = "",
    val gender: String = "",
    val age: String = "",
    val personality: String = "",
    val appearance: String = "",
    val background: String = "",
    val notes: String = "",
    val avatarPath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
