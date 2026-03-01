package com.cycling.domain.usecase.namegenerator

import com.cycling.domain.data.PlaceNameData
import javax.inject.Inject
import kotlin.random.Random

class GeneratePlaceNameUseCase @Inject constructor() {
    
    operator fun invoke(type: PlaceType, count: Int): List<String> {
        val actualCount = count.coerceAtLeast(1)
        
        val actualType = when (type) {
            PlaceType.RANDOM -> PlaceType.entries.filter { it != PlaceType.RANDOM }.random()
            else -> type
        }
        
        return List(actualCount) {
            generateSinglePlaceName(actualType)
        }
    }
    
    private fun generateSinglePlaceName(type: PlaceType): String {
        return when (type) {
            PlaceType.CITY -> {
                val prefix = PlaceNameData.cityPrefixes.random()
                val suffix = PlaceNameData.citySuffixes.random()
                prefix + suffix
            }
            PlaceType.MOUNTAIN -> {
                val prefix = PlaceNameData.mountainPrefixes.random()
                val suffix = PlaceNameData.mountainSuffixes.random()
                prefix + suffix
            }
            PlaceType.RIVER -> {
                val prefix = PlaceNameData.riverPrefixes.random()
                val suffix = PlaceNameData.riverSuffixes.random()
                prefix + suffix
            }
            PlaceType.FOREST -> {
                val prefix = PlaceNameData.forestPrefixes.random()
                val suffix = PlaceNameData.forestSuffixes.random()
                prefix + suffix
            }
            PlaceType.LAKE -> {
                val prefix = PlaceNameData.lakePrefixes.random()
                val suffix = PlaceNameData.lakeSuffixes.random()
                prefix + suffix
            }
            PlaceType.RANDOM -> {
                val randomType = PlaceType.entries.filter { it != PlaceType.RANDOM }.random()
                generateSinglePlaceName(randomType)
            }
        }
    }
}
