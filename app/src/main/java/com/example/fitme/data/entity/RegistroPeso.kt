package com.example.fitme.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registro_peso")
data class RegistroPeso(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: String,
    val peso: Float,
    val imc: Float
)
