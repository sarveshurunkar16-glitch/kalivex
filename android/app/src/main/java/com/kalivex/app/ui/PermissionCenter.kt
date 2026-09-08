package com.kalivex.app.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PermissionCenter(onClose: () -> Unit) {
    val context = LocalContext.current
    val micGranted = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        micGranted.value = granted
        Toast.makeText(context, if (granted) "Microphone granted" else "Microphone denied", Toast.LENGTH_SHORT).show()
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("KALIVEX PERMISSIONS", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text("\uD83C\uDF99 Microphone: ${if (micGranted.value) "Allowed" else "Denied"}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Required for: Voice commands, STT, wake-word (if enabled)")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { launcher.launch(Manifest.permission.RECORD_AUDIO) }) {
            Text(if (micGranted.value) "Revoke/Manage" else "Request Microphone")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onClose) { Text("Close") }
    }
}
