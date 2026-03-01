package com.cycling.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterGenerationResult(
    val characters: List<CharacterResult>
)

@Serializable
data class CharacterResult(
    val name: String,
    val alias: String = "",
    val gender: String = "",
    val age: String = "",
    val personality: String = "",
    val appearance: String = "",
    val background: String = ""
)
