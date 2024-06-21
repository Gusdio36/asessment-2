package com.d3if3089.flowernote.navigation

const val KEY_ID = "idNotes"

sealed class Screen (val route: String) {
    data object Main: Screen("mainScreen")
    data object AddNotes: Screen("detailScreen")
    data object EditNotes: Screen("detailScreen/{$KEY_ID}") {
        fun withId(id: Int) = "detailScreen/$id"
    }
}