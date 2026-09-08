package com.kalivex.app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

object ApiClient {
    private var authToken: String? = null
    private var client = buildClient(null)
    private var retrofit = buildRetrofit(client)

    // Callback for auth expiration to allow UI to react (e.g., navigate to login)
    var onAuthExpired: (() -> Unit)? = null

    private fun buildClient(token: String?): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        // Add interceptor to attach token and to detect 401 responses
        builder.addInterceptor(Interceptor { chain ->
            val original: Request = chain.request()
            val reqBuilder = original.newBuilder()
            if (!token.isNullOrEmpty()) {
                reqBuilder.addHeader("Authorization", "Bearer $token")
            }
            val req = reqBuilder.build()
            val resp: Response = chain.proceed(req)
            if (resp.code == 401) {
                // notify UI that auth expired
                try { onAuthExpired?.invoke() } catch (_: Exception) {}
            }
            return@Interceptor resp
        })

        return builder.build()
    }

    private fun buildRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BACKEND_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService get() = retrofit.create(ApiService::class.java)

    fun setAuthToken(token: String?) {
        authToken = token
        client = buildClient(authToken)
        retrofit = buildRetrofit(client)
    }
}
