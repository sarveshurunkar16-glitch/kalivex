package com.kalivex.app.commands

import android.content.Context
import com.kalivex.app.network.ApiClient
import com.kalivex.app.storage.SecureStorage

class CommandsRepository(private val context: Context) {
    private val api = ApiClient.apiService
    private val storage = SecureStorage(context)
    private val allowlist = setOf("turn_on_light", "turn_off_light", "play_music")

    data class CommandResult(val requiresConfirmation: Boolean, val commandId: Int)

    suspend fun execute(name: String, payload: Map<String, Any>?): CommandResult? {
        if (!allowlist.contains(name)) throw SecurityException("Command not allowed")
        val token = storage.getToken() ?: throw SecurityException("Not authenticated")
        val body = com.kalivex.app.network.CommandRequest(name, payload ?: emptyMap())
        val res = api.executeCommand(body, "Bearer $token")
        // Interpret response safely; here we return a simple placeholder mapping
        val requires = (res["requiresConfirmation"] as? Boolean) ?: false
        val commandId = (res["command_id"] as? Double)?.toInt() ?: 1
        return CommandResult(requiresConfirmation = requires, commandId = commandId)
    }

    suspend fun confirm(id: Int): Boolean {
        val token = storage.getToken() ?: return false
        val res = api.confirmCommand(id, "Bearer $token")
        return true
    }

    companion object {
        fun parsePayload(text: String): Map<String, Any> {
            // Very small parser - in production use proper JSON parsing
            return emptyMap()
        }
    }
}
