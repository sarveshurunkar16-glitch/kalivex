package com.kalivex.app.ws

import android.os.Handler
import android.os.Looper
import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import kotlin.math.min

class WebSocketManager(private val serverUrl: String) {
    private var client: WebSocketClient? = null
    private var token: String? = null
    private var isConnected = false
    private var reconnectAttempt = 0
    private val handler = Handler(Looper.getMainLooper())

    private var onMessageCb: ((String) -> Unit)? = null
    private var onOpenCb: (() -> Unit)? = null
    private var onCloseCb: (() -> Unit)? = null

    fun connect(token: String, onMessage: (String) -> Unit, onOpen: () -> Unit, onClose: () -> Unit) {
        this.token = token
        this.onMessageCb = onMessage
        this.onOpenCb = onOpen
        this.onCloseCb = onClose
        startConnect()
    }

    private fun startConnect() {
        if (token == null) return
        val url = serverUrl.trimEnd('/') + "/api/ws?token=" + token
        client = object : WebSocketClient(URI(url)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.i("WS", "Connected")
                isConnected = true
                reconnectAttempt = 0
                onOpenCb?.invoke()
            }

            override fun onMessage(message: String?) {
                message?.let { onMessageCb?.invoke(it) }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.i("WS", "Closed: $reason")
                isConnected = false
                onCloseCb?.invoke()
                scheduleReconnect()
            }

            override fun onError(ex: Exception?) {
                Log.e("WS", "Error", ex)
            }
        }
        client?.connect()
    }

    private fun scheduleReconnect() {
        reconnectAttempt++
        val backoff = min(60_000, (1000 * Math.pow(2.0, reconnectAttempt.toDouble())).toLong())
        handler.postDelayed({ startConnect() }, backoff)
    }

    fun send(text: String) {
        client?.send(text)
    }

    fun disconnect() {
        client?.close()
    }
}
