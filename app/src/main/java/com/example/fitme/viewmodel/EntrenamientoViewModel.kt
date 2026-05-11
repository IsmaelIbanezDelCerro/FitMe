package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.api.RutinaDto
import com.example.fitme.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EntrenamientoViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)

    private val _historial = MutableStateFlow<List<RutinaDto>>(emptyList())
    val historial: StateFlow<List<RutinaDto>> = _historial.asStateFlow()

    init {
        cargarHistorial()
    }

    private fun cargarHistorial() {
        viewModelScope.launch {
            try {
                _historial.value = RetrofitClient.api.getRutinas(prefs.usuarioId)
            } catch (_: Exception) {}
        }
    }

    fun guardarEntrenamiento(nombreRutina: String, duracionMinutos: Int) {
        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            try {
                RetrofitClient.api.addRutina(
                    prefs.usuarioId,
                    RutinaDto(nombre = nombreRutina, objetivo = "$duracionMinutos min · $fecha", activa = false)
                )
                cargarHistorial()
            } catch (_: Exception) {}
        }
    }
}
