package com.cycling.core.common

import org.junit.Test
import org.junit.Assert.*

class ResultTest {
    @Test
    fun `Success isSuccess returns true`() {
        val result = Result.Success("test")
        assertTrue(result.isSuccess)
        assertFalse(result.isError)
    }
    
    @Test
    fun `Error isError returns true`() {
        val result = Result.Error(Exception("test"))
        assertTrue(result.isError)
        assertFalse(result.isSuccess)
    }
    
    @Test
    fun `getOrNull returns data for Success`() {
        val result = Result.Success("test")
        assertEquals("test", result.getOrNull())
    }
    
    @Test
    fun `getOrNull returns null for Error`() {
        val result = Result.Error(Exception("test"))
        assertNull(result.getOrNull())
    }
    
    @Test
    fun `map transforms Success data`() {
        val result = Result.Success(5)
        val mapped = result.map { it * 2 }
        assertEquals(10, mapped.getOrNull())
    }
    
    @Test
    fun `map preserves Error`() {
        val exception = Exception("test")
        val result: Result<Int> = Result.Error(exception)
        val mapped = result.map { it * 2 }
        assertTrue(mapped.isError)
    }
    
    @Test
    fun `onSuccess is called for Success`() {
        var called = false
        Result.Success("test").onSuccess { called = true }
        assertTrue(called)
    }
    
    @Test
    fun `onError is called for Error`() {
        var called = false
        Result.Error(Exception("test")).onError { _, _ -> called = true }
        assertTrue(called)
    }
}
