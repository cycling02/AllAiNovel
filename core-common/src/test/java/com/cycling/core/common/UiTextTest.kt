package com.cycling.core.common

import org.junit.Test
import org.junit.Assert.*

class UiTextTest {
    @Test
    fun `DynamicString holds value`() {
        val uiText = UiText.DynamicString("test")
        assertEquals("test", uiText.value)
    }
    
    @Test
    fun `StringResource holds resId and args`() {
        val uiText = UiText.StringResource(123, listOf("arg1", "arg2"))
        assertEquals(123, uiText.resId)
        assertEquals(2, uiText.args.size)
    }
    
    @Test
    fun `dynamic creates DynamicString`() {
        val uiText = UiText.dynamic("test")
        assertTrue(uiText is UiText.DynamicString)
        assertEquals("test", (uiText as UiText.DynamicString).value)
    }
    
    @Test
    fun `resource creates StringResource`() {
        val uiText = UiText.resource(123, "arg1")
        assertTrue(uiText is UiText.StringResource)
        assertEquals(123, (uiText as UiText.StringResource).resId)
    }
    
    @Test
    fun `combined creates Combined`() {
        val uiText = UiText.combined(
            UiText.DynamicString("a"),
            UiText.DynamicString("b"),
            separator = ", "
        )
        assertTrue(uiText is UiText.Combined)
        assertEquals(2, (uiText as UiText.Combined).texts.size)
        assertEquals(", ", uiText.separator)
    }
}
