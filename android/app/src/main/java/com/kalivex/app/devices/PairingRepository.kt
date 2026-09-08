package com.kalivex.app.devices

import android.content.Context
import com.kalivex.app.network.ApiClient
import com.kalivex.app.storage.SecureStorage

class PairingRepository(private val context: Context) {
    private val api = ApiClient.apiService
    private val storage = SecureStorage(context)

    suspend fun pairDevice(deviceId: String): Pair<Boolean, String?> {
        try {
            val token = storage.getToken() ?: return Pair(false, "Not authenticated")
            val body = com.kalivex.app.network.PairRequest(deviceId)
            val res = api.pair(body, "Bearer $token")
            return Pair(true, null)
        } catch (ex: Exception) {
            return Pair(false, ex.message)
        }
    }
}
