package com.kalivex.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalivex.app.auth.AuthRepository
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    var wakeWordEnabled by remember { mutableStateOf(false) }
    var backendUrl by remember { mutableStateOf(BuildConfig.BACKEND_BASE_URL) }
    val context = LocalContext.current
    val auth = remember { AuthRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Settings", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Wake-word (Hey Kali)")
            Spacer(modifier = Modifier.width(8.dp))
            androidx.compose.material.Switch(checked = wakeWordEnabled, onCheckedChange = { wakeWordEnabled = it })
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = backendUrl, onValueChange = { backendUrl = it }, label = { Text("Backend URL") }, modifier = Modifier.fillMaxWidth().padding(16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                Toast.makeText(context, "Backend URL saved locally for debug", Toast.LENGTH_SHORT).show()
            }
        }) { Text("Save") }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) { Text("Back") }
    }
}
