package com.cycling.core.database.mapper

import com.cycling.core.database.entity.ApiConfigEntity
import com.cycling.domain.model.ApiConfig

fun ApiConfigEntity.toModel(): ApiConfig {
    return ApiConfig(
        id = id,
        name = name,
        provider = provider,
        apiKey = apiKey,
        baseUrl = baseUrl,
        model = model,
        isDefault = isDefault,
        createdAt = createdAt
    )
}

fun ApiConfig.toEntity(): ApiConfigEntity {
    return ApiConfigEntity(
        id = id,
        name = name,
        provider = provider,
        apiKey = apiKey,
        baseUrl = baseUrl,
        model = model,
        isDefault = isDefault,
        createdAt = createdAt,
        updatedAt = System.currentTimeMillis()
    )
}
