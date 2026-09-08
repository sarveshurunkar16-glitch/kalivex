package com.kalivex.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    var wakeWordEnabled by remember { mutableStateOf(false) }
    var backendUrl by remember { mutableStateOf("https://your-backend-host.example.com") }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Settings")
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Wake-word (Hey Kali)")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = wakeWordEnabled, onCheckedChange = { wakeWordEnabled = it })
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = backendUrl, onValueChange = { backendUrl = it }, label = { Text("Backend URL") }, modifier = Modifier.fillMaxWidth().padding(16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) { Text("Back") }
    }
}
