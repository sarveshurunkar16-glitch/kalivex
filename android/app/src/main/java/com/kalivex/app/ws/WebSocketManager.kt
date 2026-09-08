package com.kalivex.app.ws

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import android.util.Log
import org.json.JSONObject

class WebSocketManager(private val serverUrl: String) {
    private var client: WebSocketClient? = null

    fun connect(token: String, onMessage: (String)->Unit, onOpen: ()->Unit, onClose: ()->Unit) {
        val url = "$serverUrl/ws?token=$token"
        client = object : WebSocketClient(URI(url)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.i("WS", "Connected")
                onOpen()
            }

            override fun onMessage(message: String?) {
                if (message != null) onMessage(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.i("WS", "Closed: $reason")
                onClose()
            }

            override fun onError(ex: Exception?) {
                Log.e("WS", "Error", ex)
            }
        }
        client?.connect()
    }

    fun send(text: String) {
        client?.send(text)
    }

    fun disconnect() {
        client?.close()
    }
}
