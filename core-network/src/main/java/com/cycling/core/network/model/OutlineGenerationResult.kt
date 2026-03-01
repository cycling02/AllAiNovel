package com.cycling.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OutlineGenerationResult(
    val items: List<OutlineItemResult>
)

@Serializable
data class OutlineItemResult(
    val title: String,
    val summary: String,
    val level: Int,
    val children: List<OutlineItemResult> = emptyList()
)
