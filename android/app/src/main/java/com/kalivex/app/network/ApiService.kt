package com.kalivex.app.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

data class RegisterRequest(val email: String, val password: String)
data class TokenResponse(val access_token: String, val token_type: String)

data class ChatRequest(val message: String, val conversation_id: Int?)
data class ChatResponse(val reply: String, val conversation_id: Int)

data class PairRequest(val name: String, val device_type: String)

data class CommandRequest(val name: String, val device_id: Int?, val payload: Map<String, Any>?)

interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(@Body body: RegisterRequest): TokenResponse

    @POST("/api/auth/login")
    suspend fun login(@Body body: RegisterRequest): TokenResponse

    @POST("/api/chat")
    suspend fun chat(@Body body: ChatRequest, @Header("Authorization") auth: String): ChatResponse

    @POST("/api/devices/pair")
    suspend fun pair(@Body body: PairRequest, @Header("Authorization") auth: String): Map<String, Any>

    @POST("/api/commands/execute")
    suspend fun executeCommand(@Body body: CommandRequest, @Header("Authorization") auth: String): Map<String, Any>
}
