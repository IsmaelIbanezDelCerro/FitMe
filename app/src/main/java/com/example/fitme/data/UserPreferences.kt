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

    var ciudad: String
        get() = prefs.getString("ciudad", "") ?: ""
        set(value) { prefs.edit().putString("ciudad", value).apply() }

    var usuarioId: Int
        get() = prefs.getInt("usuario_id", -1)
        set(value) { prefs.edit().putInt("usuario_id", value).apply() }

    val estaLogueado: Boolean
        get() = usuarioId != -1

    fun cerrarSesion() {
        limpiarDatosPerfil()
        prefs.edit().remove("usuario_id").apply()
    }

    fun guardarCredenciales(usuario: String, password: String, id: Int, nombreUsuario: String) {
        prefs.edit()
            .putString("cred_$usuario", password)
            .putInt("uid_$usuario", id)
            .putString("nombre_$usuario", nombreUsuario)
            .putString("email_$usuario", email)
            .putInt("edad_$usuario", edad)
            .putString("sexo_$usuario", sexo)
            .putFloat("altura_$usuario", altura)
            .putFloat("peso_$usuario", pesoActual)
            .putString("objetivo_$usuario", objetivo)
            .putInt("dias_$usuario", diasEntrenamiento)
            .apply()
    }

    fun loginLocal(usuario: String, password: String): Int {
        val stored = prefs.getString("cred_$usuario", null) ?: return -1
        if (stored != password) return -1
        val id = prefs.getInt("uid_$usuario", -1)
        if (id != -1) {
            limpiarDatosPerfil()
            nombre = prefs.getString("nombre_$usuario", null) ?: usuario
            prefs.getString("email_$usuario", null)?.let { email = it }
            prefs.getInt("edad_$usuario", 0).takeIf { it > 0 }?.let { edad = it }
            prefs.getString("sexo_$usuario", null)?.let { sexo = it }
            prefs.getFloat("altura_$usuario", 0f).takeIf { it > 0f }?.let { altura = it }
            prefs.getFloat("peso_$usuario", 0f).takeIf { it > 0f }?.let { pesoActual = it }
            prefs.getString("objetivo_$usuario", null)?.let { objetivo = it }
            prefs.getInt("dias_$usuario", 0).takeIf { it > 0 }?.let { diasEntrenamiento = it }
        }
        return id
    }

    private fun limpiarDatosPerfil() {
        prefs.edit()
            .remove("nombre").remove("email").remove("edad").remove("sexo")
            .remove("altura").remove("peso_actual").remove("objetivo")
            .remove("alimentos").remove("dias_entrenamiento")
            .apply()
    }

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
