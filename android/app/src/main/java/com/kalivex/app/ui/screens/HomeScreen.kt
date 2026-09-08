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
import com.kalivex.app.commands.CommandsRepository
import com.kalivex.app.ui.components.ConfirmationDialog
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val storage = remember { SecureStorage(context) }
    val auth = remember { AuthRepository(context) }
    val commandsRepo = remember { CommandsRepository(context) }
    var loggedIn by remember { mutableStateOf(storage.getToken() != null) }

    var showConfirmation by remember { mutableStateOf(false) }
    var pendingCommandId by remember { mutableStateOf<Int?>(null) }

    var commandName by remember { mutableStateOf("") }
    var commandPayload by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF0F1720)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(48.dp))
        Text("KALIVEX", color = Color.Cyan, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("◉ READY", color = Color.LightGray, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text("\"Hey Kali\"", color = Color.LightGray, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))

        // Navigation buttons
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

        // Simple command UI
        OutlinedTextField(value = commandName, onValueChange = { commandName = it }, label = { Text("Command name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = commandPayload, onValueChange = { commandPayload = it }, label = { Text("Payload JSON (optional)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                try {
                    val payloadMap = if (commandPayload.isBlank()) null else CommandsRepository.parsePayload(commandPayload)
                    val res = commandsRepo.execute(commandName, payloadMap)
                    if (res != null && res.requiresConfirmation) {
                        pendingCommandId = res.commandId
                        showConfirmation = true
                    } else {
                        Toast.makeText(context, "Command queued", Toast.LENGTH_SHORT).show()
                    }
                } catch (ex: Exception) {
                    Toast.makeText(context, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }, modifier = Modifier.padding(8.dp)) { Text("Execute Command") }

        Spacer(modifier = Modifier.weight(1f))

        if (showConfirmation && pendingCommandId != null) {
            ConfirmationDialog(title = "Confirm Command", message = "This command requires confirmation. Confirm?", onConfirm = {
                coroutineScope.launch {
                    val ok = commandsRepo.confirm(pendingCommandId!!)
                    if (ok) Toast.makeText(context, "Confirmed", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(context, "Confirmation failed", Toast.LENGTH_SHORT).show()
                    showConfirmation = false
                    pendingCommandId = null
                }
            }, onCancel = {
                showConfirmation = false
                pendingCommandId = null
            })
        }

        Spacer(modifier = Modifier.weight(1f))
        PermissionCenter(onClose = {})
    }
}
