package com.kalivex.app.voice

import android.content.Context
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class SpeechManager(private val context: Context) {
    suspend fun listenOnce(timeoutSeconds: Int = 8): String? {
        return suspendCancellableCoroutine { cont ->
            val sr = SpeechRecognizer.createSpeechRecognizer(context)
            val intent = RecognizerIntent().apply {
                action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            }
            val listener = object : RecognitionListener {
                override fun onReadyForSpeech(params: android.os.Bundle?) {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onPartialResults(partialResults: android.os.Bundle?) {}
                override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    if (cont.isActive) cont.resume(null)
                }
                override fun onResults(results: android.os.Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.firstOrNull()
                    if (cont.isActive) cont.resume(text)
                }
            }
            sr.setRecognitionListener(listener)
            sr.startListening(intent)
            cont.invokeOnCancellation { sr.destroy() }
        }
    }
}
