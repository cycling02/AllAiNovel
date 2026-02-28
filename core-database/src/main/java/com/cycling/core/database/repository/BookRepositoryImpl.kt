package com.cycling.core.database.repository

import com.cycling.core.database.dao.BookDao
import com.cycling.core.database.mapper.toModel
import com.cycling.core.database.mapper.toEntity
import com.cycling.domain.model.Book
import com.cycling.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao
) : BookRepository {
    override fun getAllBooks(): Flow<List<Book>> {
        return bookDao.getAllBooks().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getBookById(id: Long): Flow<Book?> {
        return bookDao.getBookById(id).map { entity ->
            entity?.toModel()
        }
    }

    override suspend fun insertBook(book: Book): Long {
        return bookDao.insert(book.toEntity())
    }

    override suspend fun updateBook(book: Book) {
        bookDao.update(book.toEntity())
    }

    override suspend fun deleteBook(book: Book) {
        bookDao.delete(book.toEntity())
    }

    override suspend fun deleteBookById(id: Long) {
        bookDao.deleteById(id)
    }
}