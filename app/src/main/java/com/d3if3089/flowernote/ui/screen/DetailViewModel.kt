package com.d3if3089.flowernote.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d3if3089.flowernote.database.CatatanDao
import com.d3if3089.flowernote.model.Catatan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: CatatanDao): ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(nama: String, isi: String, lamaMenginap: String) {
        val catatan = Catatan(
            tanggal = formatter.format(Date()),
            namaBunga = nama,
            deskripsi = isi,
            jenis = lamaMenginap
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(catatan)
        }
    }

    suspend fun getCatatan(id: Int): Catatan? {
        return dao.getCatatanById(id)
    }

    fun update(id: Int, nama: String, isi: String, lamaMenginap: String) {
        val catatan = Catatan(
            id = id,
            tanggal = formatter.format(Date()),
            namaBunga = nama,
            deskripsi = isi,
            jenis = lamaMenginap
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(catatan)
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}
