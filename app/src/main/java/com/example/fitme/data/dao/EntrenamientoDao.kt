package com.example.fitme.data.dao

import androidx.room.*
import com.example.fitme.data.entity.EntrenamientoHistorial
import kotlinx.coroutines.flow.Flow

@Dao
interface EntrenamientoDao {

    @Insert
    suspend fun insertar(entrenamiento: EntrenamientoHistorial)

    @Query("SELECT * FROM entrenamiento_historial ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<EntrenamientoHistorial>>
}
