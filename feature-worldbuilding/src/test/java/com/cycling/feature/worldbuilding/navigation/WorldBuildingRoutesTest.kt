package com.cycling.feature.worldbuilding.navigation

import org.junit.Test
import org.junit.Assert.*

class WorldBuildingRoutesTest {

    @Test
    fun `WorldSettingList route has correct bookId`() {
        val bookId = 123L
        val route = WorldSettingList(bookId)
        
        assertEquals(bookId, route.bookId)
    }

    @Test
    fun `WorldSettingList route with zero bookId`() {
        val route = WorldSettingList(0L)
        
        assertEquals(0L, route.bookId)
    }

    @Test
    fun `WorldSettingList route with negative bookId`() {
        val bookId = -1L
        val route = WorldSettingList(bookId)
        
        assertEquals(bookId, route.bookId)
    }

    @Test
    fun `WorldSettingList route equality`() {
        val route1 = WorldSettingList(100L)
        val route2 = WorldSettingList(100L)
        
        assertEquals(route1, route2)
    }

    @Test
    fun `WorldSettingList route inequality`() {
        val route1 = WorldSettingList(100L)
        val route2 = WorldSettingList(200L)
        
        assertNotEquals(route1, route2)
    }

    @Test
    fun `WorldSettingList route hashCode consistency`() {
        val route1 = WorldSettingList(100L)
        val route2 = WorldSettingList(100L)
        
        assertEquals(route1.hashCode(), route2.hashCode())
    }
}
