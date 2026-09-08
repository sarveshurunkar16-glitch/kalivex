package com.kalivex.app.auth

import android.content.Context
import com.kalivex.app.network.ApiClient
import com.kalivex.app.network.RegisterRequest
import com.kalivex.app.network.TokenResponse
import com.kalivex.app.storage.SecureStorage

class AuthRepository(private val context: Context) {
    private val api = ApiClient.apiService
    private val storage = SecureStorage(context)

    suspend fun register(email: String, password: String): TokenResponse {
        val r = api.register(RegisterRequest(email, password))
        storage.saveToken(r.access_token)
        return r
    }

    suspend fun login(email: String, password: String): TokenResponse {
        val r = api.login(RegisterRequest(email, password))
        storage.saveToken(r.access_token)
        return r
    }

    fun getToken(): String? = storage.getToken()
}
