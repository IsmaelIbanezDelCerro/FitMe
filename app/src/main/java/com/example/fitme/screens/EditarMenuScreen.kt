package com.example.fitme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.LocalIsSpanish
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.api.DiaMenuDto
import com.example.fitme.viewmodel.MenuPersonalViewModel

private data class MealEditing(val diaSemana: Int, val momento: String)

@Composable
fun EditarMenuScreen(vm: MenuPersonalViewModel, onVolver: () -> Unit = {}) {
    val strings = LocalAppStrings.current
    val isSpanish = LocalIsSpanish.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val diasMenu by vm.diasMenu.collectAsState()

    val MOMENTOS = listOf("desayuno" to strings.breakfastLabel, "almuerzo" to strings.lunchLabel, "cena" to strings.dinnerLabel)

    val menuLocal = remember(diasMenu, isSpanish) {
        val base = generarMenu(prefs.objetivo, isSpanish)
        mutableStateMapOf<Int, DiaMenuDto>().also { map ->
            (0..6).forEach { idx ->
                val dto = diasMenu.firstOrNull { it.diaSemana == idx }
                val baseDia = base.getOrNull(idx)
                map[idx] = dto ?: DiaMenuDto(
                    diaSemana = idx,
                    desayuno = baseDia?.desayuno?.nombre,
                    almuerzo = baseDia?.almuerzo?.nombre,
                    cena = baseDia?.cena?.nombre,
                    kcalTotales = baseDia?.let { it.desayuno.calorias + it.almuerzo.calorias + it.cena.calorias }
                )
            }
        }
    }

    var mealEditando by remember { mutableStateOf<MealEditing?>(null) }

    if (mealEditando != null) {
        val key = mealEditando!!
        val dto = menuLocal[key.diaSemana] ?: return
        val valorActual = when (key.momento) {
            "desayuno" -> dto.desayuno ?: ""
            "almuerzo" -> dto.almuerzo ?: ""
            else -> dto.cena ?: ""
        }
        val momentoLabel = when (key.momento) {
            "desayuno" -> strings.breakfastLabel
            "almuerzo" -> strings.lunchLabel
            else -> strings.dinnerLabel
        }
        DialogEditarComida(
            titulo = "$momentoLabel — ${diasSemana(isSpanish)[key.diaSemana]}",
            valorActual = valorActual,
            strings = strings,
            onConfirmar = { nuevo ->
                menuLocal[key.diaSemana] = when (key.momento) {
                    "desayuno" -> dto.copy(desayuno = nuevo)
                    "almuerzo" -> dto.copy(almuerzo = nuevo)
                    else -> dto.copy(cena = nuevo)
                }
                mealEditando = null
            },
            onDescartar = { mealEditando = null }
        )
    }

    GymBackground {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(strings.editMenuTitle, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(strings.editMenuHint, color = Color.White.copy(alpha = 0.55f), fontSize = 13.sp)
                    }
                    LanguageToggleButton()
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { vm.limpiar() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White.copy(alpha = 0.6f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text(strings.restoreDefaultMenuBtn, fontSize = 13.sp) }
                Spacer(modifier = Modifier.height(16.dp))
            }

            diasSemana(isSpanish).forEachIndexed { idx, dia ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(dia, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C853))
                            Spacer(modifier = Modifier.height(12.dp))
                            val dto = menuLocal[idx]
                            MOMENTOS.forEach { (clave, etiqueta) ->
                                val nombre = when (clave) {
                                    "desayuno" -> dto?.desayuno ?: "—"
                                    "almuerzo" -> dto?.almuerzo ?: "—"
                                    else -> dto?.cena ?: "—"
                                }
                                val kcal = if (clave == "desayuno") dto?.kcalTotales ?: 0 else 0
                                FilaComidaEditable(
                                    etiqueta = etiqueta,
                                    nombreComida = nombre,
                                    calorias = kcal,
                                    onEditar = { mealEditando = MealEditing(idx, clave) }
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { menuLocal.values.forEach { vm.guardarDia(it) } },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text(strings.saveMenuBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text(strings.backToMenuBtn) }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun FilaComidaEditable(etiqueta: String, nombreComida: String, calorias: Int, onEditar: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(etiqueta, color = Color.White.copy(alpha = 0.55f), fontSize = 11.sp)
            Text(nombreComida, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            if (calorias > 0) Text("$calorias kcal (total día)", color = Color(0xFF00C853), fontSize = 11.sp)
        }
        IconButton(onClick = onEditar) { Text("✏️", fontSize = 16.sp) }
    }
}

@Composable
private fun DialogEditarComida(
    titulo: String,
    valorActual: String,
    strings: com.example.fitme.AppStrings,
    onConfirmar: (String) -> Unit,
    onDescartar: () -> Unit
) {
    var nombre by remember { mutableStateOf(valorActual) }

    AlertDialog(
        onDismissRequest = onDescartar,
        containerColor = Color(0xFF1E1E1E),
        title = {
            Text(text = titulo, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text(strings.foodFieldLabel, color = Color.White.copy(alpha = 0.7f)) },
                colors = dialogCampoColores(),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirmar(nombre.trim().ifEmpty { valorActual }) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = RoundedCornerShape(8.dp)
            ) { Text(strings.saveBtn, fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDescartar) {
                Text(strings.cancelBtn, color = Color.White.copy(alpha = 0.6f))
            }
        }
    )
}

@Composable
private fun dialogCampoColores() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF00C853), unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
    focusedTextColor = Color.White, unfocusedTextColor = Color.White, cursorColor = Color(0xFF00C853),
    focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent
)
