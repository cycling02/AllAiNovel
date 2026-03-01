package com.cycling.core.database.mapper

import com.cycling.core.database.entity.WorldSettingEntity
import com.cycling.domain.model.SettingType
import com.cycling.domain.model.WorldSetting
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun WorldSettingEntity.toModel(): WorldSetting {
    val type = runCatching { SettingType.valueOf(type) }.getOrDefault(SettingType.LOCATION)
    val customAttrs = runCatching {
        json.decodeFromString<Map<String, String>>(customAttributes)
    }.getOrDefault(emptyMap())

    return WorldSetting(
        id = id,
        bookId = bookId,
        type = type,
        name = name,
        description = description,
        details = details,
        customAttributes = customAttrs,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun WorldSetting.toEntity(): WorldSettingEntity {
    return WorldSettingEntity(
        id = id,
        bookId = bookId,
        type = type.name,
        name = name,
        description = description,
        details = details,
        customAttributes = json.encodeToString(customAttributes),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
