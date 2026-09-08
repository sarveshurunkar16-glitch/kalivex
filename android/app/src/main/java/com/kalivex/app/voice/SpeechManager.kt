package com.kalivex.app.voice

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpeechManager(private val context: Context) {
    private var speechRecognizer: SpeechRecognizer? = null
    var onResult: ((String) -> Unit)? = null
    var onStatus: ((String) -> Unit)? = null

    init {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) { onStatus?.invoke("ready") }
                override fun onBeginningOfSpeech() { onStatus?.invoke("listening") }
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() { onStatus?.invoke("processing") }
                override fun onError(error: Int) { onStatus?.invoke("error:$error") }
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.joinToString(separator = " ") ?: ""
                    onResult?.invoke(text)
                    onStatus?.invoke("done")
                }
                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.joinToString(separator = " ") ?: ""
                    onStatus?.invoke("partial:$text")
                }
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        } else {
            onStatus?.invoke("unavailable")
        }
    }

    fun startListening() {
        CoroutineScope(Dispatchers.Main).launch {
            onStatus?.invoke("starting")
            try {
                val intent = RecognizerIntent().apply {
                    action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                }
                speechRecognizer?.startListening(intent)
            } catch (ex: Exception) {
                onStatus?.invoke("error:${ex.message}")
            }
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {}
        onStatus?.invoke("stopped")
    }

    fun destroy() {
        try {
            speechRecognizer?.destroy()
        } catch (_: Exception) {}
        speechRecognizer = null
    }
}
