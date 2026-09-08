package com.kalivex.app.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

class TTSManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var queued: MutableList<String> = mutableListOf()

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.getDefault()
            for (s in queued) {
                tts?.speak(s, TextToSpeech.QUEUE_ADD, null, null)
            }
            queued.clear()
        }
    }

    fun speak(text: String) {
        if (tts == null) queued.add(text) else tts?.speak(text, TextToSpeech.QUEUE_ADD, null, null)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
