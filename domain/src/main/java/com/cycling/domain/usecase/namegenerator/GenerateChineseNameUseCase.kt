package com.cycling.domain.usecase.namegenerator

import com.cycling.domain.data.FamilyNames
import com.cycling.domain.data.FemaleNameChars
import com.cycling.domain.data.MaleNameChars
import javax.inject.Inject
import kotlin.random.Random

class GenerateChineseNameUseCase @Inject constructor() {
    
    operator fun invoke(gender: Gender, charCount: Int, count: Int): List<String> {
        val actualCharCount = charCount.coerceIn(1, 2)
        val actualCount = count.coerceAtLeast(1)
        
        return List(actualCount) {
            generateSingleName(gender, actualCharCount)
        }
    }
    
    private fun generateSingleName(gender: Gender, charCount: Int): String {
        val familyName = FamilyNames.names.random()
        
        val actualGender = when (gender) {
            Gender.RANDOM -> if (Random.nextBoolean()) Gender.MALE else Gender.FEMALE
            else -> gender
        }
        
        val nameChars = when (actualGender) {
            Gender.MALE -> MaleNameChars.chars
            Gender.FEMALE -> FemaleNameChars.chars
            Gender.RANDOM -> if (Random.nextBoolean()) MaleNameChars.chars else FemaleNameChars.chars
        }
        
        val givenName = when (charCount) {
            1 -> nameChars.random()
            else -> nameChars.random() + nameChars.random()
        }
        
        return familyName + givenName
    }
}
