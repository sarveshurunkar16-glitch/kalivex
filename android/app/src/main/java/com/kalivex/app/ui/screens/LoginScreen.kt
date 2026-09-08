package com.kalivex.app.ui.screens

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
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onNavigateBack: () -> Unit, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val auth = remember { AuthRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Login", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                loading = true
                try {
                    auth.login(email, password)
                    onLoginSuccess()
                } catch (ex: Exception) {
                    // show simple error
                } finally {
                    loading = false
                }
            }
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNavigateBack) { Text("Back") }
    }
}
