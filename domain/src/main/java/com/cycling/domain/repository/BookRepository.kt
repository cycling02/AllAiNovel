package com.cycling.domain.repository

import com.cycling.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAllBooks(): Flow<List<Book>>
    fun getBookById(id: Long): Flow<Book?>
    suspend fun insertBook(book: Book): Long
    suspend fun updateBook(book: Book)
    suspend fun deleteBook(book: Book)
    suspend fun deleteBookById(id: Long)
}
