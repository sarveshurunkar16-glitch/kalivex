package com.kalivex.app.auth

import android.content.Context
import com.kalivex.app.network.ApiClient
import com.kalivex.app.network.CommandRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class CommandResult(val commandId: Int, val requiresConfirmation: Boolean)

class CommandsRepository(private val context: Context) {
    private val api = ApiClient.apiService

    suspend fun execute(name: String, payload: Map<String, Any>?): CommandResult? = withContext(Dispatchers.IO) {
        val token = ApiClient
        val res = api.executeCommand(CommandRequest(name, null, payload), "Bearer ")
        // res expected to have status, command_id, requires_confirmation
        val commandId = (res["command_id"] as? Double)?.toInt() ?: (res["command_id"] as? Int)
        val requires = (res["requires_confirmation"] as? Boolean) ?: false
        return@withContext CommandResult(commandId as Int, requires)
    }

    suspend fun confirm(commandId: Int): Boolean = withContext(Dispatchers.IO) {
        val res = api.confirmCommand(commandId, "Bearer ")
        return@withContext (res["status"] == "sent" || res["status"] == "queued")
    }

    companion object {
        // very small JSON parser for payload input in UI (not robust; in production use a proper JSON parser)
        fun parsePayload(payload: String): Map<String, Any>? {
            // naive parse expecting simple JSON objects like {"key":"value"}
            val cleaned = payload.trim()
            if (cleaned.isEmpty()) return null
            if (!cleaned.startsWith("{")) return null
            val map = mutableMapOf<String, Any>()
            val inner = cleaned.removePrefix("{").removeSuffix("}").trim()
            if (inner.isEmpty()) return map
            val parts = inner.split(",")
            for (p in parts) {
                val kv = p.split(":")
                if (kv.size == 2) {
                    val k = kv[0].trim().removeSurrounding("\"")
                    var v = kv[1].trim()
                    v = v.removeSurrounding("\"")
                    map[k] = v
                }
            }
            return map
        }
    }
}
