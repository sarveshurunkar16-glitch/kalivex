package com.kalivex.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kalivex.app.voice.SpeechManager
import com.kalivex.app.voice.TtsManager
import kotlinx.coroutines.launch

@Composable
fun VoiceScreen(onNavigateBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val speechManager = remember { SpeechManager() }
    val ttsManager = remember { TtsManager() }

    var listening by remember { mutableStateOf(false) }
    var transcript by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("idle") }

    LaunchedEffect(Unit) {
        speechManager.onResult = { text ->
            transcript = text
            // For demo: speak back
            coroutineScope.launch {
                ttsManager.speak("You said: $text")
            }
        }
        speechManager.onStatus = { s -> status = s }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Voice Assistant")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Status: $status")
        Spacer(modifier = Modifier.height(8.dp))
        if (listening) {
            CircularProgressIndicator()
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            if (!listening) {
                listening = true
                speechManager.startListening()
            } else {
                listening = false
                speechManager.stopListening()
            }
        }) {
            Text(if (!listening) "Tap to Speak" else "Stop")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Transcript: $transcript")
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) { Text("Back") }
    }
}
