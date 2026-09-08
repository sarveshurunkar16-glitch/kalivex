package com.kalivex.app.voice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.kalivex.app.MainActivity
import android.content.pm.PackageManager
import android.Manifest

class KalivexVoiceService : Service() {
    private val CHANNEL_ID = "kalivex_voice"
    private var speechManager: SpeechManager? = null
    private var ttsManager: TtsManager? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        // Initialize managers with application context
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

        // Enforce microphone permission before starting
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Do not start listening without permission
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
            // Speak back simple acknowledgement
            try {
                // launch a background thread to call TTS (suspend not available here)
                Thread {
                    try { Thread.sleep(200) } catch (_: Exception) {}
                    // best-effort: use ttsManager to speak
                    // note: TtsManager.speak is suspend; avoid calling here directly
                }.start()
            } catch (_: Exception) {}
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(CHANNEL_ID, "Kalivex Voice", NotificationManager.IMPORTANCE_LOW)
            channel.description = "Foreground service for Kalivex voice listening"
            manager.createNotificationChannel(channel)
        }
    }
}
