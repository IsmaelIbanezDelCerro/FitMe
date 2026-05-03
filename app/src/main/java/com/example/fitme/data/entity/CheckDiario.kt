package com.example.fitme.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_diario")
data class CheckDiario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: String,
    val ejercicioHecho: Boolean,
    val dietaHecha: Boolean
)
