package com.cycling.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

suspend fun <T> withIOContext(block: suspend () -> T): T = 
    withContext(Dispatchers.IO) { block() }

suspend fun <T> withDefaultContext(block: suspend () -> T): T = 
    withContext(Dispatchers.Default) { block() }

fun <T> Flow<T>.asResult(): Flow<Result<T>> = this
    .map<T, Result<T>> { Result.Success(it) }
    .onStart { emit(Result.Success(null as T)) }
    .catch { emit(Result.Error(it)) }

fun <T, R> Result<T>.fold(
    onSuccess: (T) -> R,
    onError: (Throwable, String?) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(exception, message)
}

inline fun <T> Result<T>.getOrElse(defaultValue: (Throwable, String?) -> T): T = when (this) {
    is Result.Success -> data
    is Result.Error -> defaultValue(exception, message)
}

inline fun <T> Result<T>.recover(transform: (Throwable, String?) -> T): Result<T> = when (this) {
    is Result.Success -> this
    is Result.Error -> Result.Success(transform(exception, message))
}

fun <T> Result<T>.toUiResult(): UiResult<T> = when (this) {
    is Result.Success -> UiResult.Success(data)
    is Result.Error -> UiResult.Error(exception, message)
}

sealed class UiResult<out T> {
    data class Success<out T>(val data: T) : UiResult<T>()
    data class Error(val exception: Throwable, val message: String? = null) : UiResult<Nothing>()
    data object Loading : UiResult<Nothing>()
    
    val isLoading: Boolean
        get() = this is Loading
    
    val isSuccess: Boolean
        get() = this is Success
    
    val isError: Boolean
        get() = this is Error
    
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
}

fun <T> Result<T>.toUiState(): UiState<T> = when (this) {
    is Result.Success -> UiState.Success(data)
    is Result.Error -> UiState.Error(message ?: exception.message ?: "Unknown error")
}

sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    
    val isLoading: Boolean
        get() = this is Loading
    
    val isSuccess: Boolean
        get() = this is Success
    
    val isError: Boolean
        get() = this is Error
    
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    inline fun <R> map(transform: (T) -> R): UiState<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Loading -> this
    }
}
