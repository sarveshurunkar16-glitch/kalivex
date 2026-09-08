package com.kalivex.app

import com.kalivex.app.network.BackoffProvider
import org.junit.Assert
import org.junit.Test

class BackoffTest {
    @Test
    fun testBackoffIncreases() {
        val d1 = BackoffProvider.delayMs(1)
        val d2 = BackoffProvider.delayMs(2)
        val d3 = BackoffProvider.delayMs(3)
        Assert.assertTrue(d2 > d1)
        Assert.assertTrue(d3 > d2)
    }

    @Test
    fun testBackoffMax() {
        val d = BackoffProvider.delayMs(10)
        Assert.assertTrue(d <= 60_000L)
    }
}
