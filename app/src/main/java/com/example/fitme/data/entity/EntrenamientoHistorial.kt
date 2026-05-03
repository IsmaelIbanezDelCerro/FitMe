package com.example.fitme.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entrenamiento_historial")
data class EntrenamientoHistorial(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: String,
    val nombreRutina: String,
    val duracionMinutos: Int
)
