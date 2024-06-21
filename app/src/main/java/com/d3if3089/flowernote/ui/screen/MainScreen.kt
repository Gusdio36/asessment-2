package com.d3if3089.flowernote.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d3if3089.flowernote.R
import com.d3if3089.flowernote.database.CatatanDb
import com.d3if3089.flowernote.model.Catatan
import com.d3if3089.flowernote.navigation.Screen
import com.d3if3089.flowernote.ui.theme.DarkBlueDefault
import com.d3if3089.flowernote.util.SettingsDataStore
import com.d3if3089.flowernote.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navHostController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)

    ScaffoldComponent(
        isMain = true,
        navHostController = navHostController,
        title = stringResource(id = R.string.app_name),
        fab = {
            FloatingActionButton(
                onClick = { navHostController.navigate(Screen.AddNotes.route) },
                containerColor = DarkBlueDefault,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "FAB"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.saveLayout(!showList)
                }
            }) {
                Icon(
                    painter = painterResource(
                        if (showList) R.drawable.baseline_grid_view_24
                        else R.drawable.baseline_view_list_24
                    ), contentDescription =
                        if (showList) "Grid"
                        else "List",
                    tint = Color.White
                )
            }
        }
    ) {
        ScreenContent(modifier = it, navHostController, showList)
    }
}

@Composable
private fun ScreenContent(modifier: Modifier = Modifier, navHostController: NavHostController, showList: Boolean) {
    val context = LocalContext.current
    val db = CatatanDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    if (data.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(206.dp, 210.dp),
                painter = painterResource(id = R.drawable.empty_state),
                contentDescription = null
            )
            RegularText(text = "Data kosong.")
        }
    } else {
        if (showList) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) {
                    ListItem(catatan = it) {
                        navHostController.navigate(Screen.EditNotes.withId(it.id))
                    }
                    Divider()
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ) {
                items(data) {
                    GridItem(catatan = it) {
                        navHostController.navigate(Screen.EditNotes.withId(it.id))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(catatan: Catatan, onClick: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RegularText(
                    text = catatan.namaBunga,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                SmallText(
                    text = catatan.jenis,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            SmallText(text = catatan.deskripsi, maxLines = 1, overflow = TextOverflow.Ellipsis)
            SmallText(text = catatan.tanggal)
        }
}

@Composable
fun GridItem(catatan: Catatan, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, DarkBlueDefault)
    )
    {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RegularText(
                    text = catatan.namaBunga,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                SmallText(
                    text = catatan.jenis,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
            SmallText(
                text = catatan.deskripsi
            )
            SmallText(
                text = catatan.tanggal
            )

        }
    }
}