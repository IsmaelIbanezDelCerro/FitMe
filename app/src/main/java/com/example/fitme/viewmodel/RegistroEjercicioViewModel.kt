package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.AppDatabase
import com.example.fitme.data.entity.RegistroEjercicio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RegistroEjercicioViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtenerInstancia(application).registroEjercicioDao()

    fun obtenerRegistrosDia(fecha: String): Flow<List<RegistroEjercicio>> =
        dao.obtenerPorFecha(fecha)

    fun obtenerHistorialEjercicio(nombre: String): Flow<List<RegistroEjercicio>> =
        dao.obtenerHistorialEjercicio(nombre)

    fun guardarRegistro(
        fecha: String,
        nombreRutina: String,
        nombreEjercicio: String,
        series: Int,
        repeticiones: String,
        pesoKg: Float
    ) {
        viewModelScope.launch {
            dao.insertar(
                RegistroEjercicio(
                    fecha = fecha,
                    nombreRutina = nombreRutina,
                    nombreEjercicio = nombreEjercicio,
                    series = series,
                    repeticiones = repeticiones,
                    pesoKg = pesoKg
                )
            )
        }
    }
}
