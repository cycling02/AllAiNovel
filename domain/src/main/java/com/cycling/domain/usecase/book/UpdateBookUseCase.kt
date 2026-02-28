package com.cycling.domain.usecase.book

import com.cycling.domain.model.Book
import com.cycling.domain.repository.BookRepository
import javax.inject.Inject

class UpdateBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: Book) = repository.updateBook(book)
}
