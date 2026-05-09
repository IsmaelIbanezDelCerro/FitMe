package com.example.fitme.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registro_ejercicio")
data class RegistroEjercicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: String,
    val nombreRutina: String,
    val nombreEjercicio: String,
    val series: Int,
    val repeticiones: String,
    val pesoKg: Float
)
