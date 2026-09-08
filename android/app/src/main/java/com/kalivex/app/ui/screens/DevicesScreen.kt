package com.kalivex.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kalivex.app.devices.PairingRepository
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun DevicesScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val pairingRepo = remember { PairingRepository(context) }
    var pairingStatus by remember { mutableStateOf("Not paired") }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Device Management")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Status: $pairingStatus")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            pairingStatus = "Pairing..."
            coroutineScope.launch {
                val (ok, msg) = pairingRepo.pairDevice("android-device-1")
                if (ok) pairingStatus = "Paired" else pairingStatus = "Pair failed: $msg"
            }
        }) { Text("Pair Device") }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) { Text("Back") }
    }
}
