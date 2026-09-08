package com.kalivex.app.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalivex.app.voice.SpeechManager
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var listening by remember { mutableStateOf(false) }
    var lastText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF0F1720)), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(48.dp))
        Text("KALIVEX", color = Color.Cyan, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("◉ READY", color = Color.LightGray, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text("\"Hey Kali\"", color = Color.LightGray, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            // Launch SpeechRecognizer via helper
            coroutineScope.launch {
                listening = true
                val sm = SpeechManager(context)
                val res = sm.listenOnce()
                if (res != null) lastText = res
                listening = false
            }
        }, modifier = Modifier.padding(16.dp)) {
            Text(if (listening) "Listening..." else "🎙 Tap to Speak")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Last: $lastText", color = Color.White)

        Spacer(modifier = Modifier.weight(1f))
    }
}
