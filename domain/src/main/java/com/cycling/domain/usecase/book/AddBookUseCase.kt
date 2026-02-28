package com.cycling.domain.usecase.book

import com.cycling.domain.model.Book
import com.cycling.domain.repository.BookRepository
import javax.inject.Inject

class AddBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: Book): Long = repository.insertBook(book)
}
