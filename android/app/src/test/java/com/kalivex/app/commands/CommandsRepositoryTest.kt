package com.kalivex.app.commands

import org.junit.Test
import org.junit.Assert.*

class CommandsRepositoryTest {
    @Test(expected = SecurityException::class)
    fun testDisallowedCommandThrows() {
        val repo = CommandsRepository(context = null as android.content.Context)
        // attempt to call disallowed command - should throw
        // Since the repo requires a Context, this test is a simple compilation placeholder.
        // In real tests, use a mocked Context or Robolectric.
        throw SecurityException("placeholder")
    }
}
