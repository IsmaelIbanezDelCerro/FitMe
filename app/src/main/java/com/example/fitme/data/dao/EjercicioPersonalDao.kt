package com.example.fitme.data.dao

import androidx.room.*
import com.example.fitme.data.entity.EjercicioPersonal
import kotlinx.coroutines.flow.Flow

@Dao
interface EjercicioPersonalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(ejercicio: EjercicioPersonal)

    @Update
    suspend fun actualizar(ejercicio: EjercicioPersonal)

    @Delete
    suspend fun eliminar(ejercicio: EjercicioPersonal)

    @Query("SELECT * FROM ejercicio_personal ORDER BY orden ASC")
    fun obtenerTodos(): Flow<List<EjercicioPersonal>>

    @Query("DELETE FROM ejercicio_personal")
    suspend fun eliminarTodos()
}
