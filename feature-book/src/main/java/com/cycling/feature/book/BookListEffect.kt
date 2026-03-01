package com.cycling.feature.book

sealed interface BookListEffect {
    data class ShowToast(val message: String) : BookListEffect
    data class NavigateToChapters(val bookId: Long) : BookListEffect
    data object NavigateToSettings : BookListEffect
    data class ShowError(val error: String) : BookListEffect
}
