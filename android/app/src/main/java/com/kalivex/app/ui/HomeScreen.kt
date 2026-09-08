package com.kalivex.app.ui

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalivex.app.auth.AuthRepository
import com.kalivex.app.auth.CommandsRepository
import com.kalivex.app.storage.SecureStorage
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var listening by remember { mutableStateOf(false) }
    var lastText by remember { mutableStateOf("") }
    var connectionStatus by remember { mutableStateOf("disconnected") }
    var showPermissionCenter by remember { mutableStateOf(false) }

    val storage = remember { SecureStorage(context) }
    val authRepo = remember { AuthRepository(context) }
    val commandsRepo = remember { CommandsRepository(context) }

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

        // Connection status indicator
        Text("Connection: $connectionStatus", color = Color.Green, modifier = Modifier.padding(8.dp))

        Button(onClick = { showPermissionCenter = true }, modifier = Modifier.padding(8.dp)) {
            Text("Permission Center")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Simple input and button to execute a command
        var commandName by remember { mutableStateOf("") }
        var commandPayload by remember { mutableStateOf("") }
        OutlinedTextField(value = commandName, onValueChange = { commandName = it }, label = { Text("Command name") }, modifier = Modifier.fillMaxWidth().padding(16.dp))
        OutlinedTextField(value = commandPayload, onValueChange = { commandPayload = it }, label = { Text("Payload JSON (optional)") }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        Button(onClick = {
            coroutineScope.launch {
                val token = storage.getToken()
                if (token == null) {
                    Toast.makeText(context, "Not authenticated", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                try {
                    val payloadMap = if (commandPayload.isBlank()) null else CommandsRepository.parsePayload(commandPayload)
                    val res = commandsRepo.execute(commandName, payloadMap)
                    if (res != null && res.requiresConfirmation) {
                        // show confirmation dialog
                        ConfirmationDialogWindow(context = context, commandId = res.commandId, onConfirmed = {
                            coroutineScope.launch {
                                val ok = commandsRepo.confirm(res.commandId)
                                if (ok) Toast.makeText(context, "Confirmed", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        Toast.makeText(context, "Command queued", Toast.LENGTH_SHORT).show()
                    }
                } catch (ex: Exception) {
                    Toast.makeText(context, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }, modifier = Modifier.padding(16.dp)) {
            Text("Execute Command")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Last STT: $lastText", color = Color.White)

        Spacer(modifier = Modifier.weight(1f))

        if (showPermissionCenter) {
            PermissionCenter(onClose = { showPermissionCenter = false })
        }
    }
}
