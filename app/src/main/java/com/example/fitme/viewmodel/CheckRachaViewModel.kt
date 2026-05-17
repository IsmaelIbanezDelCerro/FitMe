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

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    init {
        cargarRacha()
    }

    fun cargarRacha() {
        viewModelScope.launch {
            try {
                _racha.value = RetrofitClient.api.getRacha(prefs.usuarioId)
            } catch (_: Exception) {}
        }
    }

    fun guardarCheck(ejercicioHecho: Boolean, dietaHecha: Boolean) {
        if (!ejercicioHecho && !dietaHecha) return
        viewModelScope.launch {
            _guardando.value = true
            try {
                _racha.value = RetrofitClient.api.registrarCheck(prefs.usuarioId)
            } catch (_: Exception) {
            } finally {
                _guardando.value = false
            }
        }
    }

    fun fechaHoy(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}
