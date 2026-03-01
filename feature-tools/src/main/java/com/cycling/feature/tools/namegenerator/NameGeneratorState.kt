package com.cycling.feature.tools.namegenerator

import com.cycling.domain.model.NameFavorite
import com.cycling.domain.model.NameType
import com.cycling.domain.usecase.namegenerator.FactionType
import com.cycling.domain.usecase.namegenerator.Gender
import com.cycling.domain.usecase.namegenerator.PlaceType

data class NameGeneratorState(
    val generatedNames: List<String> = emptyList(),
    val favorites: List<NameFavorite> = emptyList(),
    val selectedNameType: NameType = NameType.PERSON_NAME,
    val gender: Gender = Gender.RANDOM,
    val charCount: Int = 2,
    val placeType: PlaceType = PlaceType.RANDOM,
    val factionType: FactionType = FactionType.RANDOM,
    val generateCount: Int = 5,
    val isGenerating: Boolean = false,
    val showFavorites: Boolean = false,
    val error: String? = null
)
