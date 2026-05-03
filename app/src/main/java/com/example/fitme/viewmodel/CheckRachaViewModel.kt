package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.AppDatabase
import com.example.fitme.data.entity.CheckDiario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CheckRachaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtenerInstancia(application).checkDiarioDao()

    val checks: StateFlow<List<CheckDiario>> = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun guardarCheck(ejercicioHecho: Boolean, dietaHecha: Boolean) {
        val fecha = fechaHoy()
        viewModelScope.launch {
            dao.insertar(CheckDiario(fecha = fecha, ejercicioHecho = ejercicioHecho, dietaHecha = dietaHecha))
        }
    }

    fun calcularRachaActual(lista: List<CheckDiario>): Int {
        if (lista.isEmpty()) return 0
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechasConCheck = lista
            .filter { it.ejercicioHecho || it.dietaHecha }
            .map { sdf.parse(it.fecha) }
            .filterNotNull()
            .sortedDescending()

        if (fechasConCheck.isEmpty()) return 0

        val cal = Calendar.getInstance()
        cal.time = fechasConCheck[0]
        val hoy = Calendar.getInstance()

        // Si el último check no es de hoy ni de ayer, la racha está rota
        val difDias = ((hoy.timeInMillis - cal.timeInMillis) / 86400000L).toInt()
        if (difDias > 1) return 0

        var racha = 1
        for (i in 1 until fechasConCheck.size) {
            val anterior = Calendar.getInstance().apply { time = fechasConCheck[i - 1] }
            val actual = Calendar.getInstance().apply { time = fechasConCheck[i] }
            val diff = ((anterior.timeInMillis - actual.timeInMillis) / 86400000L).toInt()
            if (diff == 1) racha++ else break
        }
        return racha
    }

    fun calcularMejorRacha(lista: List<CheckDiario>): Int {
        if (lista.isEmpty()) return 0
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechas = lista
            .filter { it.ejercicioHecho || it.dietaHecha }
            .map { sdf.parse(it.fecha) }
            .filterNotNull()
            .sortedDescending()

        if (fechas.isEmpty()) return 0

        var mejor = 1
        var actual = 1
        for (i in 1 until fechas.size) {
            val anterior = Calendar.getInstance().apply { time = fechas[i - 1] }
            val curr = Calendar.getInstance().apply { time = fechas[i] }
            val diff = ((anterior.timeInMillis - curr.timeInMillis) / 86400000L).toInt()
            if (diff == 1) {
                actual++
                if (actual > mejor) mejor = actual
            } else {
                actual = 1
            }
        }
        return mejor
    }

    fun fechaHoy(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}
