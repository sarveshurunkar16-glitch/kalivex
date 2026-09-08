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
import android.widget.Toast

@Composable
fun DevicesScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val pairingRepo = remember { PairingRepository(context) }
    var pairingStatus by remember { mutableStateOf("Not paired") }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Device Management")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Status: $pairingStatus")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            pairingStatus = "Pairing..."
            // Simple pairing demo; in production call suspend functions properly
            pairingRepo.pairDevice("android-device-1") { ok, msg ->
                if (ok) pairingStatus = "Paired"
                else pairingStatus = "Pair failed: $msg"
            }
        }) { Text("Pair Device") }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onNavigateBack, modifier = Modifier.padding(16.dp)) { Text("Back") }
    }
}
