package com.kalivex.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalivex.app.auth.AuthRepository
import com.kalivex.app.storage.SecureStorage
import com.kalivex.app.ui.permissions.PermissionCenter

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val storage = remember { SecureStorage(context) }
    val auth = remember { AuthRepository(context) }
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

        Button(onClick = { onNavigate("voice") }, modifier = Modifier.padding(8.dp)) {
            Text("Open Voice Assistant")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onNavigate("chat") }, modifier = Modifier.padding(8.dp)) {
            Text("Chat")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onNavigate("devices") }, modifier = Modifier.padding(8.dp)) {
            Text("Devices")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onNavigate("settings") }, modifier = Modifier.padding(8.dp)) {
            Text("Settings")
        }

        Spacer(modifier = Modifier.weight(1f))

        PermissionCenter(onClose = {})
    }
}
