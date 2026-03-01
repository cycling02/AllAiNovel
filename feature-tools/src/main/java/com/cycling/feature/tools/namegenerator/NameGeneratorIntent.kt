package com.cycling.feature.tools.namegenerator

import com.cycling.domain.model.NameType
import com.cycling.domain.usecase.namegenerator.FactionType
import com.cycling.domain.usecase.namegenerator.Gender
import com.cycling.domain.usecase.namegenerator.PlaceType

sealed interface NameGeneratorIntent {
    data object GenerateNames : NameGeneratorIntent
    data class SelectNameType(val type: NameType) : NameGeneratorIntent
    data class SelectGender(val gender: Gender) : NameGeneratorIntent
    data class SelectCharCount(val count: Int) : NameGeneratorIntent
    data class SelectPlaceType(val type: PlaceType) : NameGeneratorIntent
    data class SelectFactionType(val type: FactionType) : NameGeneratorIntent
    data class SetGenerateCount(val count: Int) : NameGeneratorIntent
    data class CopyName(val name: String) : NameGeneratorIntent
    data class AddToFavorites(val name: String, val type: NameType) : NameGeneratorIntent
    data class RemoveFromFavorite(val id: Long) : NameGeneratorIntent
    data object ToggleFavorites : NameGeneratorIntent
    data object LoadFavorites : NameGeneratorIntent
    data object ClearError : NameGeneratorIntent
}
