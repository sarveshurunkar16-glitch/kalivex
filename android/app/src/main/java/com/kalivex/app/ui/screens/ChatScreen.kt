package com.kalivex.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen(onNavigateBack: () -> Unit) {
    var messages by remember { mutableStateOf(listOf<String>()) }
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Chat")
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            messages.forEach { m -> Text(m) }
        }
        OutlinedTextField(value = input, onValueChange = { input = it }, label = { Text("Message") }, modifier = Modifier.fillMaxWidth().padding(16.dp))
        Button(onClick = {
            if (input.isNotBlank()) {
                messages = messages + input
                input = ""
            }
        }, modifier = Modifier.padding(16.dp)) { Text("Send") }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) { Text("Back") }
    }
}
