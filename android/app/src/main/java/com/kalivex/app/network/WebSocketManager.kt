package com.kalivex.app.network

import okhttp3.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class WebSocketManager(private val url: String) {
    private var client: OkHttpClient = OkHttpClient.Builder()
        .pingInterval(15, TimeUnit.SECONDS)
        .build()
    private var webSocket: WebSocket? = null
    private var backoffMs = 1000L
    var onOpen: (() -> Unit)? = null
    var onMessage: ((String) -> Unit)? = null
    var onClose: (() -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null

    fun connect() {
        val req = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(req, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                backoffMs = 1000L
                onOpen?.invoke()
            }

            override fun onMessage(ws: WebSocket, text: String) {
                onMessage?.invoke(text)
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                onClose?.invoke()
                scheduleReconnect()
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                onError?.invoke(t)
                scheduleReconnect()
            }
        })
    }

    private fun scheduleReconnect() {
        thread {
            try { Thread.sleep(backoffMs) } catch (_: Exception) {}
            backoffMs = (backoffMs * 2).coerceAtMost(60_000L)
            connect()
        }
    }

    fun send(text: String) {
        webSocket?.send(text)
    }

    fun close() {
        webSocket?.close(1000, "Client closed")
    }
}
