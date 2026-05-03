package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.AppDatabase
import com.example.fitme.data.entity.ComidaPersonal
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuPersonalViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtenerInstancia(application).comidaPersonalDao()

    val comidasPersonales: StateFlow<List<ComidaPersonal>> = dao.obtenerTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun guardarComida(comida: ComidaPersonal) {
        viewModelScope.launch { dao.insertar(comida) }
    }

    fun guardarTodo(comidas: List<ComidaPersonal>) {
        viewModelScope.launch { comidas.forEach { dao.insertar(it) } }
    }

    fun limpiar() {
        viewModelScope.launch { dao.limpiarTodo() }
    }
}
