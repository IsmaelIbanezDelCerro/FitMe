package com.example.fitme.data.dao

import androidx.room.*
import com.example.fitme.data.entity.RegistroPeso
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroPesoDao {

    @Insert
    suspend fun insertar(registro: RegistroPeso)

    @Query("SELECT * FROM registro_peso ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<RegistroPeso>>

    @Query("SELECT * FROM registro_peso ORDER BY fecha DESC LIMIT 1")
    suspend fun obtenerUltimo(): RegistroPeso?
}
