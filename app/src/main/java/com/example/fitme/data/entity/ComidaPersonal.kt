package com.example.fitme.data.entity

import androidx.room.Entity

@Entity(tableName = "comida_personal", primaryKeys = ["dia", "momento"])
data class ComidaPersonal(
    val dia: String,
    val momento: String, // "desayuno", "almuerzo" o "cena"
    val nombre: String,
    val calorias: Int = 0,
    val proteinas: Int = 0
)
