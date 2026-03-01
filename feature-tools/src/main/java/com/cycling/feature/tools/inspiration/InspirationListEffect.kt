package com.cycling.feature.tools.inspiration

sealed interface InspirationListEffect {
    data class ShowToast(val message: String) : InspirationListEffect
    data class ShowError(val message: String) : InspirationListEffect
}
