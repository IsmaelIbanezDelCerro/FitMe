package com.example.fitme.data.dao

import androidx.room.*
import com.example.fitme.data.entity.ComidaPersonal
import kotlinx.coroutines.flow.Flow

@Dao
interface ComidaPersonalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(comida: ComidaPersonal)

    @Query("SELECT * FROM comida_personal")
    fun obtenerTodas(): Flow<List<ComidaPersonal>>

    @Query("DELETE FROM comida_personal")
    suspend fun limpiarTodo()
}
