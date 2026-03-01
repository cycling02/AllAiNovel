package com.cycling.feature.book

import com.cycling.domain.model.Book

data class BookListState(
    val books: List<Book> = emptyList(),
    val filteredBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val showAddDialog: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null
) {
    val displayBooks: List<Book>
        get() = if (searchQuery.isBlank()) books else filteredBooks

    val isEmpty: Boolean
        get() = displayBooks.isEmpty() && !isLoading
}
