package com.kalivex.app.devices

import android.content.Context
import com.kalivex.app.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PairingRepository(private val context: Context) {
    private val api = ApiClient.apiService

    fun pairDevice(id: String, callback: (Boolean, String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val body = com.kalivex.app.network.PairRequest(id)
                val res = api.pair(body, "Bearer ")
                callback(true, null)
            } catch (ex: Exception) {
                callback(false, ex.message)
            }
        }
    }
}
