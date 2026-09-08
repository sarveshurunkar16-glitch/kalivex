package com.kalivex.app.voice

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.kalivex.app.MainActivity

class KalivexVoiceService : Service() {
    private val CHANNEL_ID = "kalivex_voice"
    private var speechManager: SpeechManager? = null
    private var ttsManager: TtsManager? = null

    override fun onCreate() {
        super.onCreate()
        speechManager = SpeechManager(applicationContext)
        ttsManager = TtsManager(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == "STOP_LISTENING") {
            stopListening()
            stopSelf()
            return START_NOT_STICKY
        }

        startForeground(2501, buildNotification())
        startListening()
        return START_STICKY
    }

    private fun startListening() {
        speechManager?.onStatus = { status ->
            // Could broadcast status to app via LocalBroadcast or other mechanisms
        }
        speechManager?.onResult = { text ->
            // For service-level handling, we could send to backend or TTS
            ttsManager?.let { tm ->
                // simple speak back
                kotlin.concurrent.thread {
                    // Use coroutine ideally; keeping simple to avoid heavy deps
                    try { Thread.sleep(200) } catch (_: Exception) {}
                    // not calling suspend function here; use runOnUi thread in real app
                }
            }
        }
        speechManager?.startListening()
    }

    private fun stopListening() {
        speechManager?.stopListening()
        speechManager?.destroy()
        ttsManager?.shutdown()
    }

    override fun onDestroy() {
        stopListening()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(): Notification {
        val stopIntent = Intent(this, KalivexVoiceService::class.java).apply { action = "STOP_LISTENING" }
        val pendingStop = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingActivity = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Kalivex Listening")
            .setContentText("Tap to stop")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setContentIntent(pendingActivity)
            .addAction(android.R.drawable.ic_media_pause, "Stop", pendingStop)
            .setOngoing(true)
            .build()
    }
}
