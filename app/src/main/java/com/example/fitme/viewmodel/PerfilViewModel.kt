package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.api.RegistroPesoDto
import com.example.fitme.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)

    private val _registros = MutableStateFlow<List<RegistroPesoDto>>(emptyList())
    val registros: StateFlow<List<RegistroPesoDto>> = _registros.asStateFlow()

    init {
        cargarRegistros()
    }

    private fun cargarRegistros() {
        viewModelScope.launch {
            try {
                _registros.value = RetrofitClient.api.getPesos(prefs.usuarioId)
            } catch (_: Exception) {}
        }
    }

    fun registrarPeso(peso: Float, altura: Float) {
        if (peso <= 0 || altura <= 0) return
        val alturaM = altura / 100f
        val imc = peso / (alturaM * alturaM)
        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            try {
                RetrofitClient.api.addPeso(
                    prefs.usuarioId,
                    RegistroPesoDto(pesoKg = peso, fecha = fecha, nota = "IMC: ${"%.2f".format(imc)}")
                )
                cargarRegistros()
            } catch (_: Exception) {}
        }
    }
}
