package com.d3if3089.flowernote.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.d3if3089.flowernote.ui.theme.DarkBlueDefault

@Composable
fun DisplayAlertDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            containerColor = Color.White,
            textContentColor = DarkBlueDefault,
            text = { SmallText(text = "Anda yakin ingin menghapus data ini?") },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmation() }) {
                    SmallText(text = "Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {onDismissRequest() }) {
                    SmallText(text = "Batal", color = DarkBlueDefault)
                }
            },
            onDismissRequest = { onDismissRequest() })
    }
}

@Preview
@Composable
private fun DialogPreview() {
    DisplayAlertDialog(openDialog = true, onDismissRequest = { /*TODO*/ }) {

    }
}