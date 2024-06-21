package com.d3if3089.flowernote.ui.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.d3if3089.flowernote.R
import com.d3if3089.flowernote.ui.theme.DarkBlueDefault
import com.d3if3089.flowernote.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldComponent(
    navHostController: NavHostController,
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    isMain: Boolean = false,
    fab: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (!isMain) {
                        IconButton(onClick = { navHostController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Icon Kembali"
                            )

                        }
                    }
                },
                title = {
                        Text(text = title, style = Typography.displayMedium)
                },
                actions = actions,
                colors = topAppBarColors(
                    containerColor = DarkBlueDefault,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = fab
    ) {
        content(Modifier.padding(it))
    }
}

@Preview
@Composable
private fun Prev() {
    ScaffoldComponent(isMain = false, navHostController = rememberNavController(), title = "Test")
}