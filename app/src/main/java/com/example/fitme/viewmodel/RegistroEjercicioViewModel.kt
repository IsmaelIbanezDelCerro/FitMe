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

class RegistroEjercicioViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)

    private val _ejerciciosDia = MutableStateFlow<List<EjercicioDto>>(emptyList())
    val ejerciciosDia: StateFlow<List<EjercicioDto>> = _ejerciciosDia.asStateFlow()

    private var rutinaTrackingId: Int = -1

    init {
        viewModelScope.launch { cargarRutinaTracking() }
    }

    private suspend fun cargarRutinaTracking() {
        try {
            val rutinas = RetrofitClient.api.getRutinas(prefs.usuarioId)
            val tracking = rutinas.firstOrNull { it.nombre == "Registro Diario" && it.activa }
            rutinaTrackingId = if (tracking != null) {
                tracking.id
            } else {
                RetrofitClient.api.addRutina(
                    prefs.usuarioId,
                    RutinaDto(nombre = "Registro Diario", activa = true)
                ).id
            }
            _ejerciciosDia.value = RetrofitClient.api.getEjercicios(rutinaTrackingId)
        } catch (_: Exception) {}
    }

    fun obtenerEjerciciosDia(): StateFlow<List<EjercicioDto>> = _ejerciciosDia

    fun guardarRegistro(
        nombreRutina: String,
        nombreEjercicio: String,
        series: Int,
        repeticiones: String,
        pesoKg: Float
    ) {
        if (rutinaTrackingId == -1) return
        viewModelScope.launch {
            try {
                RetrofitClient.api.addEjercicio(
                    rutinaTrackingId,
                    EjercicioDto(
                        nombre = nombreEjercicio,
                        grupoMuscular = nombreRutina,
                        series = series,
                        repeticiones = repeticiones.toIntOrNull() ?: 0,
                        descansoSeg = pesoKg.toInt()
                    )
                )
                _ejerciciosDia.value = RetrofitClient.api.getEjercicios(rutinaTrackingId)
            } catch (_: Exception) {}
        }
    }
}
