package com.cycling.domain.model

data class UserPreferences(
    val darkTheme: Boolean = false,
    val dynamicColors: Boolean = true,
    val defaultApiConfigId: Long? = null
)
