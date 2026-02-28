package com.cycling.feature.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.Book
import com.cycling.domain.usecase.book.AddBookUseCase
import com.cycling.domain.usecase.book.DeleteBookUseCase
import com.cycling.domain.usecase.book.GetBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    getBooksUseCase: GetBooksUseCase,
    private val addBookUseCase: AddBookUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(BookListState())
    val state: StateFlow<BookListState> = _state.asStateFlow()
    
    private val _effect = MutableSharedFlow<BookListEffect>()
    val effect = _effect.asSharedFlow()
    
    private val booksFlow = getBooksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    init {
        viewModelScope.launch {
            booksFlow.collect { books ->
                _state.value = _state.value.copy(
                    books = books,
                    filteredBooks = filterBooks(books, _state.value.searchQuery)
                )
            }
        }
    }
    
    fun onIntent(intent: BookListIntent) {
        when (intent) {
            BookListIntent.LoadBooks -> loadBooks()
            BookListIntent.ShowAddDialog -> showAddDialog()
            BookListIntent.HideAddDialog -> hideAddDialog()
            is BookListIntent.AddBook -> addBook(intent.title)
            is BookListIntent.ShowDeleteDialog -> showDeleteDialog(intent.book)
            BookListIntent.HideDeleteDialog -> hideDeleteDialog()
            BookListIntent.ConfirmDelete -> confirmDelete()
            is BookListIntent.SearchBooks -> searchBooks(intent.query)
        }
    }
    
    private fun loadBooks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
        }
    }
    
    private fun showAddDialog() {
        _state.value = _state.value.copy(showAddDialog = true)
    }
    
    private fun hideAddDialog() {
        _state.value = _state.value.copy(showAddDialog = false)
    }
    
    private fun addBook(title: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val book = Book(title = title.trim())
                addBookUseCase(book)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showAddDialog = false
                )
                _effect.emit(BookListEffect.ShowToast("书籍创建成功"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(BookListEffect.ShowError("创建书籍失败: ${e.message}"))
            }
        }
    }
    
    private fun showDeleteDialog(book: Book) {
        _state.value = _state.value.copy(
            showDeleteDialog = true,
            bookToDelete = book
        )
    }
    
    private fun hideDeleteDialog() {
        _state.value = _state.value.copy(
            showDeleteDialog = false,
            bookToDelete = null
        )
    }
    
    private fun confirmDelete() {
        viewModelScope.launch {
            _state.value.bookToDelete?.let { book ->
                try {
                    deleteBookUseCase(book.id)
                    _state.value = _state.value.copy(
                        showDeleteDialog = false,
                        bookToDelete = null
                    )
                    _effect.emit(BookListEffect.ShowToast("书籍删除成功"))
                } catch (e: Exception) {
                    _state.value = _state.value.copy(
                        error = e.message
                    )
                    _effect.emit(BookListEffect.ShowError("删除书籍失败: ${e.message}"))
                }
            }
        }
    }
    
    private fun searchBooks(query: String) {
        val filteredBooks = filterBooks(_state.value.books, query)
        _state.value = _state.value.copy(
            searchQuery = query,
            filteredBooks = filteredBooks
        )
    }
    
    private fun filterBooks(books: List<Book>, query: String): List<Book> {
        return if (query.isBlank()) {
            books
        } else {
            books.filter { it.title.contains(query, ignoreCase = true) }
        }
    }
    
    fun navigateToChapters(bookId: Long) {
        viewModelScope.launch {
            _effect.emit(BookListEffect.NavigateToChapters(bookId))
        }
    }
    
    fun navigateToSettings() {
        viewModelScope.launch {
            _effect.emit(BookListEffect.NavigateToSettings)
        }
    }
}