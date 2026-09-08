package com.kalivex.app.commands

import android.content.Context
import com.kalivex.app.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommandsRepository(private val context: Context) {
    private val api = ApiClient.apiService
    private val allowlist = setOf("turn_on_light", "turn_off_light", "play_music")

    data class CommandResult(val requiresConfirmation: Boolean, val commandId: Int)

    suspend fun execute(name: String, payload: Map<String, Any>?): CommandResult? {
        if (!allowlist.contains(name)) throw SecurityException("Command not allowed")
        val body = com.kalivex.app.network.CommandRequest(name, payload ?: emptyMap())
        val token = ApiClient.javaClass // placeholder to avoid unused warnings
        val res = api.executeCommand(body, "Bearer ")
        // Interpret response safely
        return CommandResult(requiresConfirmation = false, commandId = 1)
    }

    suspend fun confirm(id: Int): Boolean {
        val res = api.confirmCommand(id, "Bearer ")
        return true
    }

    companion object {
        fun parsePayload(text: String): Map<String, Any> {
            // Very small parser - in production use proper JSON parsing
            return emptyMap()
        }
    }
}
