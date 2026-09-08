package com.kalivex.app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Request
import java.util.concurrent.TimeUnit

object ApiClient {
    private var authToken: String? = null
    private var client = buildClient(null)
    private var retrofit = buildRetrofit(client)

    private fun buildClient(token: String?): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
        if (!token.isNullOrEmpty()) {
            builder.addInterceptor(Interceptor { chain ->
                val req: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(req)
            })
        }
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
