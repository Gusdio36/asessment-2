package com.d3if3089.flowernote.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.d3if3089.flowernote.model.Catatan

@Dao
interface CatatanDao {

    @Insert
    suspend fun insert(catatan: Catatan)
    @Update
    suspend fun update(catatan: Catatan)

    @Query("SELECT * FROM catatan_bunga ORDER BY tanggal DESC")
    fun getCatatan(): Flow<List<Catatan>>

    @Query("SELECT * FROM catatan_bunga WHERE id = :id")
    suspend fun getCatatanById(id: Int): Catatan?

    @Query("DELETE FROM catatan_bunga WHERE id = :id")
    suspend fun deleteById(id: Int)
}