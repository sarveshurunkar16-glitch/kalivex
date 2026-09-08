package com.kalivex.app.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialog(title: String, message: String, onConfirm: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = { Button(onClick = onConfirm) { Text("Confirm") } },
        dismissButton = { Button(onClick = onCancel) { Text("Cancel") } }
    )
}
