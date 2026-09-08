package com.kalivex.app.ui

import android.content.Context
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialogWindow(context: Context, commandId: Int, onConfirmed: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Confirmation Required") },
        text = { Text("This action requires confirmation. Confirm to proceed.") },
        confirmButton = {
            Button(onClick = { onConfirmed() }) { Text("Confirm") }
        },
        dismissButton = {
            Button(onClick = { /* no-op: dismiss handled by parent */ }) { Text("Cancel") }
        }
    )
}
