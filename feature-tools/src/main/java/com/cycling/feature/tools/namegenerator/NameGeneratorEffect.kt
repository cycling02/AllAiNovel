package com.cycling.feature.tools.namegenerator

sealed interface NameGeneratorEffect {
    data class ShowToast(val message: String) : NameGeneratorEffect
    data class ShowError(val message: String) : NameGeneratorEffect
    data class CopyToClipboard(val text: String) : NameGeneratorEffect
}
