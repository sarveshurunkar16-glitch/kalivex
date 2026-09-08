package com.kalivex.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalivex.app.auth.AuthRepository
import com.kalivex.app.storage.SecureStorage
import com.kalivex.app.ui.permissions.PermissionCenter
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val storage = remember { SecureStorage(context) }
    val auth = remember { AuthRepository(context) }
    var loggedIn by remember { mutableStateOf(storage.getToken() != null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("KALIVEX", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("◉ READY")
        Spacer(modifier = Modifier.height(12.dp))
        Text("\"Hey Kali\"")
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

        Spacer(modifier = Modifier.height(16.dp))

        if (!loggedIn) {
            Button(onClick = { onNavigate("login") }) { Text("Login") }
        } else {
            Button(onClick = {
                coroutineScope.launch {
                    try {
                        auth.logout()
                        loggedIn = false
                        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                    } catch (ex: Exception) {
                        Toast.makeText(context, "Logout error: ${ex.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }) { Text("Logout") }
        }

        Spacer(modifier = Modifier.weight(1f))

        PermissionCenter(onClose = {})
    }
}
