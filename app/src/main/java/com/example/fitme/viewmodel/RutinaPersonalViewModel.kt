package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.api.EjercicioDto
import com.example.fitme.data.api.RutinaDto
import com.example.fitme.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RutinaPersonalViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)

    private val _ejercicios = MutableStateFlow<List<EjercicioDto>>(emptyList())
    val ejercicios: StateFlow<List<EjercicioDto>> = _ejercicios.asStateFlow()

    private var rutinaPersonalId: Int = -1

    init {
        viewModelScope.launch { cargarRutinaPersonal() }
    }

    private suspend fun cargarRutinaPersonal() {
        try {
            val rutinas = RetrofitClient.api.getRutinas(prefs.usuarioId)
            val personal = rutinas.firstOrNull { it.nombre == "Mi Rutina Personal" && it.activa }
            rutinaPersonalId = if (personal != null) {
                personal.id
            } else {
                RetrofitClient.api.addRutina(
                    prefs.usuarioId,
                    RutinaDto(nombre = "Mi Rutina Personal", activa = true)
                ).id
            }
            _ejercicios.value = RetrofitClient.api.getEjercicios(rutinaPersonalId)
        } catch (_: Exception) {}
    }

    fun agregar(ejercicio: EjercicioDto) {
        if (rutinaPersonalId == -1) return
        viewModelScope.launch {
            try {
                RetrofitClient.api.addEjercicio(rutinaPersonalId, ejercicio)
                _ejercicios.value = RetrofitClient.api.getEjercicios(rutinaPersonalId)
            } catch (_: Exception) {}
        }
    }

    fun actualizar(ejercicio: EjercicioDto) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.updateEjercicio(ejercicio.id, ejercicio)
                _ejercicios.value = RetrofitClient.api.getEjercicios(rutinaPersonalId)
            } catch (_: Exception) {}
        }
    }

    fun eliminar(ejercicio: EjercicioDto) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.deleteEjercicio(ejercicio.id)
                _ejercicios.value = _ejercicios.value.filter { it.id != ejercicio.id }
            } catch (_: Exception) {}
        }
    }

    fun cargarPredeterminada(lista: List<EjercicioDto>) {
        if (rutinaPersonalId == -1) return
        viewModelScope.launch {
            try {
                _ejercicios.value.forEach { RetrofitClient.api.deleteEjercicio(it.id) }
                lista.forEach { RetrofitClient.api.addEjercicio(rutinaPersonalId, it) }
                _ejercicios.value = RetrofitClient.api.getEjercicios(rutinaPersonalId)
            } catch (_: Exception) {}
        }
    }

    fun limpiar() {
        viewModelScope.launch {
            try {
                _ejercicios.value.forEach { RetrofitClient.api.deleteEjercicio(it.id) }
                _ejercicios.value = emptyList()
            } catch (_: Exception) {}
        }
    }
}
