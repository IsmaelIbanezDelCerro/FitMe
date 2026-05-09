package com.example.fitme.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitme.data.entity.RegistroEjercicio
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroEjercicioDao {

    @Insert
    suspend fun insertar(registro: RegistroEjercicio)

    @Query("SELECT * FROM registro_ejercicio WHERE fecha = :fecha ORDER BY id ASC")
    fun obtenerPorFecha(fecha: String): Flow<List<RegistroEjercicio>>

    @Query("SELECT * FROM registro_ejercicio WHERE nombreEjercicio = :nombre ORDER BY fecha DESC LIMIT 10")
    fun obtenerHistorialEjercicio(nombre: String): Flow<List<RegistroEjercicio>>

    @Query("DELETE FROM registro_ejercicio")
    suspend fun borrarTodos()
}
