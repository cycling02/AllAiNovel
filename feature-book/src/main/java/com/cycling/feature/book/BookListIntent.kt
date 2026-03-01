package com.cycling.feature.book

import com.cycling.domain.model.Book

sealed interface BookListIntent {
    data object LoadBooks : BookListIntent
    data object ShowAddDialog : BookListIntent
    data object HideAddDialog : BookListIntent
    data class AddBook(val title: String) : BookListIntent
    data class DeleteBook(val book: Book) : BookListIntent
    data class UndoDelete(val book: Book) : BookListIntent
    data class SearchBooks(val query: String) : BookListIntent
}
