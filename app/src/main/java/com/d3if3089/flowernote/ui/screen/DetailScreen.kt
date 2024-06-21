package com.d3if3089.flowernote.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d3if3089.flowernote.database.CatatanDb
import com.d3if3089.flowernote.ui.theme.Poppins
import com.d3if3089.flowernote.util.ViewModelFactory

@Composable
fun DetailScreen(navHostController: NavHostController, id: Int? = null) {
    val context = LocalContext.current
    val db = CatatanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    var showDialog by remember { mutableStateOf(false) }


    var namaDoggy by rememberSaveable {
        mutableStateOf("")
    }
    var jenisBunga by rememberSaveable {
        mutableStateOf("")
    }
    var catatan by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getCatatan(id) ?: return@LaunchedEffect
        namaDoggy = data.namaBunga
        jenisBunga = data.jenis
        catatan = data.deskripsi
    }
    ScaffoldComponent(
        navHostController = navHostController,
        title = if (id == null) "Tambah Data" else "Edit Data",
        actions = {
            IconButton(onClick = {
                if (namaDoggy.isEmpty() || catatan.isEmpty() ||
                    jenisBunga.isEmpty()
                ) {
                    Toast.makeText(context, "Harap isikan semua data!", Toast.LENGTH_LONG).show()
                    return@IconButton
                }
                if (id == null) {
                    viewModel.insert(namaDoggy, catatan, jenisBunga)
                } else {
                    viewModel.update(id, namaDoggy, catatan, jenisBunga)
                }
                navHostController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Simpan")
            }
            if (id != null) {
                DeleteAction { showDialog = true }
                DisplayAlertDialog(
                    openDialog = showDialog,
                    onDismissRequest = { showDialog = false }) {
                    showDialog = false
                    viewModel.delete(id)
                    navHostController.popBackStack()
                }
            }
        }
    ) {
        AddNotesForm(
            modifier = it,
            namaBunga = namaDoggy, onNameChange = { namaDoggy = it },
            jenisBunga = jenisBunga, onJenisChange = { jenisBunga = it },
            catatan = catatan, onCatatanChange = { catatan = it },
        )
    }
}

@Composable
fun AddNotesForm(
    modifier: Modifier,
    namaBunga: String, onNameChange: (String) -> Unit,
    jenisBunga: String, onJenisChange: (String) -> Unit,
    catatan: String, onCatatanChange: (String) -> Unit,
) {
    val options = listOf(
        "Indoor",
        "Outdoor",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 72.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegularText(
            text = "Nama Bunga", modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = namaBunga,
            maxLines = 1,
            onValueChange = { onNameChange(it) },
            textStyle = TextStyle(fontFamily = Poppins),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                options.forEach() {
                    RadioForm(
                        modifier = Modifier.fillMaxWidth(),
                        text = it,
                        isSelected = jenisBunga == it
                    ) {
                        onJenisChange(it)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        RegularText(
            text = "Deskripsi",
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = catatan,
            onValueChange = { onCatatanChange(it) },
            textStyle = TextStyle(fontFamily = Poppins),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun RadioForm(modifier: Modifier = Modifier, text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = isSelected, onClick = { onClick() })
        Text(text = text, fontFamily = Poppins)
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Lainnya",
            tint = Color.White
        )
        DropdownMenu(
            modifier = Modifier.background(Color.White),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { SmallText(text = "Hapus") },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}