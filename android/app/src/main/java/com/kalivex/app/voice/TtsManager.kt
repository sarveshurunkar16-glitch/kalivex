package com.kalivex.app.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class TtsManager(private val context: Context) {
    private var tts: TextToSpeech? = null
    private val initialized = CompletableDeferred<Boolean>()

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                initialized.complete(true)
            } else {
                initialized.complete(false)
            }
        }
    }

    suspend fun speak(text: String) {
        if (tts == null) return
        val ok = initialized.await()
        if (!ok) return
        withContext(Dispatchers.Main) {
            tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "kalivex_tts")
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
    }
}
