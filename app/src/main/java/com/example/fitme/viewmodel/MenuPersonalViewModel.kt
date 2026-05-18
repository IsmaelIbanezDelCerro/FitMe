package com.example.fitme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.api.DiaMenuDto
import com.example.fitme.data.api.MenuSemanalDto
import com.example.fitme.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class MenuPersonalViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)

    private val _diasMenu = MutableStateFlow<List<DiaMenuDto>>(emptyList())
    val diasMenu: StateFlow<List<DiaMenuDto>> = _diasMenu.asStateFlow()

    private val _menuActual = MutableStateFlow<MenuSemanalDto?>(null)
    val menuActual: StateFlow<MenuSemanalDto?> = _menuActual.asStateFlow()

    init {
        cargarMenuSemana()
    }

    private fun cargarMenuSemana() {
        viewModelScope.launch {
            try {
                val menus = RetrofitClient.api.getMenus(prefs.usuarioId)
                val hoy = LocalDate.now()
                val menu = menus.firstOrNull {
                    val inicio = LocalDate.parse(it.semanaInicio)
                    val fin = LocalDate.parse(it.semanaFin)
                    !hoy.isBefore(inicio) && !hoy.isAfter(fin)
                } ?: crearMenuSemanaActual()
                _menuActual.value = menu
                menu?.let { _diasMenu.value = RetrofitClient.api.getDiasMenu(it.id) }
            } catch (_: Exception) {}
        }
    }

    private suspend fun crearMenuSemanaActual(): MenuSemanalDto? {
        val hoy = LocalDate.now()
        val inicio = hoy.with(DayOfWeek.MONDAY)
        val fin = inicio.plusDays(6)
        return try {
            RetrofitClient.api.addMenu(
                prefs.usuarioId,
                MenuSemanalDto(semanaInicio = inicio.toString(), semanaFin = fin.toString())
            )
        } catch (_: Exception) { null }
    }

    fun guardarDia(dia: DiaMenuDto) {
        viewModelScope.launch {
            try {
                val menuId = _menuActual.value?.id ?: return@launch
                val existente = _diasMenu.value.firstOrNull { it.diaSemana == dia.diaSemana }
                val saved = if (existente != null) {
                    RetrofitClient.api.updateDiaMenu(existente.id, dia.copy(id = existente.id, menuId = menuId))
                } else {
                    RetrofitClient.api.addDiaMenu(menuId, dia.copy(menuId = menuId))
                }
                // El backend puede no devolver los campos de kcal/proteínas por comida,
                // así que los fusionamos desde el dto que enviamos nosotros.
                val merged = saved.copy(
                    desayunoKcal = dia.desayunoKcal,
                    almuerzoKcal = dia.almuerzoKcal,
                    cenaKcal = dia.cenaKcal,
                    desayunoProteinas = dia.desayunoProteinas,
                    almuerzoProteinas = dia.almuerzoProteinas,
                    cenaProteinas = dia.cenaProteinas
                )
                _diasMenu.value = _diasMenu.value
                    .filter { it.diaSemana != dia.diaSemana }
                    .plus(merged)
                    .sortedBy { it.diaSemana }
            } catch (_: Exception) {}
        }
    }

    fun limpiar() {
        viewModelScope.launch {
            try {
                val menuId = _menuActual.value?.id ?: return@launch
                _diasMenu.value.forEach { RetrofitClient.api.deleteDiaMenu(it.id) }
                _diasMenu.value = emptyList()
            } catch (_: Exception) {}
        }
    }
}
