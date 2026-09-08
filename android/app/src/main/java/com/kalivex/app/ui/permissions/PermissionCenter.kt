package com.kalivex.app.ui.permissions

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
import android.Manifest
import android.content.Intent
import android.provider.Settings

@Composable
fun PermissionCenter(onClose: () -> Unit) {
    val context = LocalContext.current
    val micPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_GRANTED
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Permission Center")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Microphone: ${if (micPermissionGranted) "Granted" else "Not granted"}")
        Spacer(modifier = Modifier.height(8.dp))
        if (!micPermissionGranted) {
            Button(onClick = { launcher.launch(Manifest.permission.RECORD_AUDIO) }) { Text("Request Microphone") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = android.net.Uri.parse("package:${context.packageName}")
                context.startActivity(intent)
            }) { Text("Open App Settings") }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onClose) { Text("Close") }
    }
}
