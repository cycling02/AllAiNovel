package com.cycling.domain.usecase.book

import com.cycling.domain.repository.BookRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: Long) = repository.deleteBookById(bookId)
}
