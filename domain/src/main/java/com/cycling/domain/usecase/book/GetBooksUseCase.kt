package com.cycling.domain.usecase.book

import com.cycling.domain.model.Book
import com.cycling.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(): Flow<List<Book>> = repository.getAllBooks()
}
