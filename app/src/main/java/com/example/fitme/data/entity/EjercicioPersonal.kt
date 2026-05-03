package com.example.fitme.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ejercicio_personal")
data class EjercicioPersonal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val series: Int,
    val repeticiones: String,
    val descanso: String,
    val descripcion: String = "",
    val orden: Int = 0
)
