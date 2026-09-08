package com.kalivex.app.network

object BackoffProvider {
    fun delayMs(attempt: Int): Long {
        if (attempt <= 0) return 0
        val base = 1000L
        val max = 60_000L
        val delay = (base * Math.pow(2.0, (attempt - 1).toDouble())).toLong()
        return if (delay > max) max else delay
    }
}
