package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.AppDatabase
import com.example.fitme.data.entity.RegistroPeso
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtenerInstancia(application).registroPesoDao()

    val registros: StateFlow<List<RegistroPeso>> = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun registrarPeso(peso: Float, altura: Float) {
        if (peso <= 0 || altura <= 0) return
        val alturaM = altura / 100f
        val imc = peso / (alturaM * alturaM)
        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            dao.insertar(RegistroPeso(fecha = fecha, peso = peso, imc = imc))
        }
    }
}
