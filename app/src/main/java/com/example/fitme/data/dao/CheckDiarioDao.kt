package com.example.fitme.data.dao

import androidx.room.*
import com.example.fitme.data.entity.CheckDiario
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckDiarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(check: CheckDiario)

    @Query("SELECT * FROM check_diario WHERE fecha = :fecha LIMIT 1")
    suspend fun obtenerPorFecha(fecha: String): CheckDiario?

    @Query("SELECT * FROM check_diario ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<CheckDiario>>
}
