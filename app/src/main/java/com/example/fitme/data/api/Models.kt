package com.example.fitme.data.api

data class UsuarioDto(
    val id: Int = 0,
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val fechaRegistro: String? = null,
    val pesoActual: Float = 0f,
    val alturaCm: Float = 0f,
    val imcActual: Float = 0f
)

data class LoginRequest(
    val nombre: String,
    val password: String
)

data class RegistroPesoDto(
    val id: Int = 0,
    val usuarioId: Int = 0,
    val pesoKg: Float = 0f,
    val fecha: String = "",
    val nota: String? = null
)

data class RutinaDto(
    val id: Int = 0,
    val usuarioId: Int = 0,
    val nombre: String = "",
    val nivel: String? = null,
    val objetivo: String? = null,
    val activa: Boolean = true
)

data class EjercicioDto(
    val id: Int = 0,
    val rutinaId: Int = 0,
    val nombre: String = "",
    val grupoMuscular: String? = null,
    val series: Int = 0,
    val repeticiones: Int = 0,
    val descansoSeg: Int = 0
)

data class MenuSemanalDto(
    val id: Int = 0,
    val usuarioId: Int = 0,
    val semanaInicio: String = "",
    val semanaFin: String = "",
    val generadoEn: String? = null
)

data class DiaMenuDto(
    val id: Int = 0,
    val menuId: Int = 0,
    val diaSemana: Int = 0,
    val desayuno: String? = null,
    val almuerzo: String? = null,
    val cena: String? = null,
    val kcalTotales: Int? = null
)

data class RachaDto(
    val id: Int = 0,
    val usuarioId: Int = 0,
    val diasConsecutivos: Int = 0,
    val ultimaActividad: String? = null,
    val rachaMaxima: Int = 0
)

data class PreferenciaDto(
    val id: Int = 0,
    val usuarioId: Int = 0,
    val tipo: String = "",
    val descripcion: String? = null
)

data class GimnasioDto(
    val id: Int = 0,
    val usuarioId: Int = 0,
    val nombre: String = "",
    val direccion: String? = null,
    val distanciaKm: Float? = null,
    val valoracion: Float? = null
)
