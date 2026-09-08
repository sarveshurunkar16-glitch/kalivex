package com.kalivex.app.ui.permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import android.os.Build

@Composable
fun PermissionCenter(onClose: () -> Unit) {
    val context = LocalContext.current
    val micPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    var notifPermissionGranted by remember { mutableStateOf(true) }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        notifPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }

    val micLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> }
    val notifLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Permission Center")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Microphone: ${if (micPermissionGranted) "Granted" else "Not granted"}")
        Spacer(modifier = Modifier.height(8.dp))
        if (!micPermissionGranted) {
            Button(onClick = { micLauncher.launch(Manifest.permission.RECORD_AUDIO) }) { Text("Request Microphone") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivity(intent)
            }) { Text("Open App Settings") }
        }

        Spacer(modifier = Modifier.height(12.dp))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Text("Notifications: ${if (notifPermissionGranted) "Granted" else "Not granted"}")
            Spacer(modifier = Modifier.height(8.dp))
            if (!notifPermissionGranted) {
                Button(onClick = { notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }) { Text("Request Notifications") }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onClose) { Text("Close") }
    }
}
