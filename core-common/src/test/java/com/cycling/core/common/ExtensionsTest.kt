package com.cycling.core.common

import org.junit.Test
import org.junit.Assert.*

class ExtensionsTest {
    @Test
    fun `fold calls onSuccess for Success`() {
        val result = Result.Success(5)
        val folded = result.fold(
            onSuccess = { it * 2 },
            onError = { _, _ -> 0 }
        )
        assertEquals(10, folded)
    }
    
    @Test
    fun `fold calls onError for Error`() {
        val result: Result<Int> = Result.Error(Exception("test"))
        val folded = result.fold(
            onSuccess = { it * 2 },
            onError = { _, _ -> 0 }
        )
        assertEquals(0, folded)
    }
    
    @Test
    fun `getOrElse returns data for Success`() {
        val result = Result.Success(5)
        val value = result.getOrElse { _, _ -> 0 }
        assertEquals(5, value)
    }
    
    @Test
    fun `getOrElse returns default for Error`() {
        val result: Result<Int> = Result.Error(Exception("test"))
        val value = result.getOrElse { _, _ -> 0 }
        assertEquals(0, value)
    }
    
    @Test
    fun `toUiResult converts Success`() {
        val result = Result.Success(5)
        val uiResult = result.toUiResult()
        assertTrue(uiResult is UiResult.Success)
        assertEquals(5, (uiResult as UiResult.Success).data)
    }
    
    @Test
    fun `toUiResult converts Error`() {
        val exception = Exception("test")
        val result: Result<Int> = Result.Error(exception, "error message")
        val uiResult = result.toUiResult()
        assertTrue(uiResult is UiResult.Error)
        assertEquals("error message", (uiResult as UiResult.Error).message)
    }
    
    @Test
    fun `UiState map transforms Success`() {
        val state: UiState<Int> = UiState.Success(5)
        val mapped = state.map { it * 2 }
        assertTrue(mapped is UiState.Success)
        assertEquals(10, (mapped as UiState.Success).data)
    }
    
    @Test
    fun `UiState map preserves Error`() {
        val state: UiState<Int> = UiState.Error("error")
        val mapped = state.map { it * 2 }
        assertTrue(mapped is UiState.Error)
    }
    
    @Test
    fun `UiState map preserves Loading`() {
        val state: UiState<Int> = UiState.Loading
        val mapped = state.map { it * 2 }
        assertTrue(mapped is UiState.Loading)
    }
}
