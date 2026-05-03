package com.example.fitme.data

import android.content.Context

class UserPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("fitme_prefs", Context.MODE_PRIVATE)

    var nombre: String
        get() = prefs.getString("nombre", "") ?: ""
        set(value) { prefs.edit().putString("nombre", value).apply() }

    var email: String
        get() = prefs.getString("email", "") ?: ""
        set(value) { prefs.edit().putString("email", value).apply() }

    var edad: Int
        get() = prefs.getInt("edad", 0)
        set(value) { prefs.edit().putInt("edad", value).apply() }

    var sexo: String
        get() = prefs.getString("sexo", "") ?: ""
        set(value) { prefs.edit().putString("sexo", value).apply() }

    var altura: Float
        get() = prefs.getFloat("altura", 0f)
        set(value) { prefs.edit().putFloat("altura", value).apply() }

    var pesoActual: Float
        get() = prefs.getFloat("peso_actual", 0f)
        set(value) { prefs.edit().putFloat("peso_actual", value).apply() }

    var objetivo: String
        get() = prefs.getString("objetivo", "") ?: ""
        set(value) { prefs.edit().putString("objetivo", value).apply() }

    var alimentosSeleccionados: String
        get() = prefs.getString("alimentos", "") ?: ""
        set(value) { prefs.edit().putString("alimentos", value).apply() }

    var diasEntrenamiento: Int
        get() = prefs.getInt("dias_entrenamiento", 3)
        set(value) { prefs.edit().putInt("dias_entrenamiento", value).apply() }

    fun calcularImc(): Float {
        if (altura <= 0 || pesoActual <= 0) return 0f
        val alturaM = altura / 100f
        return pesoActual / (alturaM * alturaM)
    }

    fun categoriaImc(): String {
        val imc = calcularImc()
        return when {
            imc <= 0f -> "Sin datos"
            imc < 18.5f -> "Bajo peso"
            imc < 25f -> "Peso normal"
            imc < 30f -> "Sobrepeso"
            else -> "Obesidad"
        }
    }
}
