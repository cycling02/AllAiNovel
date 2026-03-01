package com.cycling.domain.usecase.namegenerator

import com.cycling.domain.data.FactionNameData
import javax.inject.Inject

class GenerateFactionNameUseCase @Inject constructor() {
    
    operator fun invoke(type: FactionType, count: Int): List<String> {
        val actualCount = count.coerceAtLeast(1)
        
        val actualType = when (type) {
            FactionType.RANDOM -> FactionType.entries.filter { it != FactionType.RANDOM }.random()
            else -> type
        }
        
        return List(actualCount) {
            generateSingleFactionName(actualType)
        }
    }
    
    private fun generateSingleFactionName(type: FactionType): String {
        return when (type) {
            FactionType.SECT -> {
                val prefix = FactionNameData.sectPrefixes.random()
                val suffix = FactionNameData.sectSuffixes.random()
                prefix + suffix
            }
            FactionType.FAMILY -> {
                val prefix = FactionNameData.familyPrefixes.random()
                val suffix = FactionNameData.familySuffixes.random()
                prefix + suffix
            }
            FactionType.EMPIRE -> {
                val prefix = FactionNameData.empirePrefixes.random()
                val suffix = FactionNameData.empireSuffixes.random()
                prefix + suffix
            }
            FactionType.GUILD -> {
                val prefix = FactionNameData.guildPrefixes.random()
                val suffix = FactionNameData.guildSuffixes.random()
                prefix + suffix
            }
            FactionType.GANG -> {
                val prefix = FactionNameData.gangPrefixes.random()
                val suffix = FactionNameData.gangSuffixes.random()
                prefix + suffix
            }
            FactionType.RANDOM -> {
                val randomType = FactionType.entries.filter { it != FactionType.RANDOM }.random()
                generateSingleFactionName(randomType)
            }
        }
    }
}
