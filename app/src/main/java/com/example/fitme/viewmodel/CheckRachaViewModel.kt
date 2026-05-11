package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.api.RachaDto
import com.example.fitme.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CheckRachaViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)

    private val _racha = MutableStateFlow(RachaDto())
    val racha: StateFlow<RachaDto> = _racha.asStateFlow()

    init {
        cargarRacha()
    }

    private fun cargarRacha() {
        viewModelScope.launch {
            try {
                _racha.value = RetrofitClient.api.getRacha(prefs.usuarioId)
            } catch (_: Exception) {}
        }
    }

    fun guardarCheck(ejercicioHecho: Boolean, dietaHecha: Boolean) {
        if (!ejercicioHecho && !dietaHecha) return
        val hoy = fechaHoy()
        val actual = _racha.value
        val nuevosDias = when (actual.ultimaActividad) {
            hoy -> actual.diasConsecutivos
            ayer() -> actual.diasConsecutivos + 1
            else -> 1
        }
        val nuevaMax = maxOf(nuevosDias, actual.rachaMaxima)
        viewModelScope.launch {
            try {
                _racha.value = RetrofitClient.api.updateRacha(
                    prefs.usuarioId,
                    RachaDto(diasConsecutivos = nuevosDias, ultimaActividad = hoy, rachaMaxima = nuevaMax)
                )
            } catch (_: Exception) {}
        }
    }

    fun calcularRachaActual(): Int = _racha.value.diasConsecutivos
    fun calcularMejorRacha(): Int = _racha.value.rachaMaxima

    fun fechaHoy(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun ayer(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    }
}
