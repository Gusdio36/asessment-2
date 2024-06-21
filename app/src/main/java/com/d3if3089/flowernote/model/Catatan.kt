package com.d3if3089.flowernote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catatan_bunga")
data class Catatan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaBunga: String,
    val deskripsi: String,
    val jenis: String,
    val tanggal: String
)
