package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.AppDatabase
import com.example.fitme.data.entity.EjercicioPersonal
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RutinaPersonalViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtenerInstancia(application).ejercicioPersonalDao()

    val ejercicios: StateFlow<List<EjercicioPersonal>> = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agregar(ejercicio: EjercicioPersonal) {
        viewModelScope.launch { dao.insertar(ejercicio) }
    }

    fun actualizar(ejercicio: EjercicioPersonal) {
        viewModelScope.launch { dao.actualizar(ejercicio) }
    }

    fun eliminar(ejercicio: EjercicioPersonal) {
        viewModelScope.launch { dao.eliminar(ejercicio) }
    }

    fun cargarPredeterminada(lista: List<EjercicioPersonal>) {
        viewModelScope.launch {
            dao.eliminarTodos()
            lista.forEach { dao.insertar(it) }
        }
    }

    fun limpiar() {
        viewModelScope.launch { dao.eliminarTodos() }
    }
}
