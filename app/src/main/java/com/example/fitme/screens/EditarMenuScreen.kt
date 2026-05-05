package com.example.fitme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.LocalIsSpanish
import com.example.fitme.LocalOnToggleLanguage
import com.example.fitme.R
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.entity.ComidaPersonal
import com.example.fitme.loadStrings
import com.example.fitme.login.AppShell
import com.example.fitme.login.LoginScreen
import com.example.fitme.login.RegisterScreen
import com.example.fitme.ui.theme.FitMeTheme
import com.example.fitme.viewmodel.MenuPersonalViewModel

private val DIAS = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

@Composable
fun EditarMenuScreen(onVolver: () -> Unit = {}) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val vm: MenuPersonalViewModel = viewModel()
    val comidasPersonales by vm.comidasPersonales.collectAsState()

    val MOMENTOS = listOf("desayuno" to strings.breakfastLabel, "almuerzo" to strings.lunchLabel, "cena" to strings.dinnerLabel)

    val menuLocal = remember(comidasPersonales) {
        val menuBase = generarMenu(prefs.objetivo)
        val mapa = mutableMapOf<Pair<String, String>, ComidaPersonal>()
        menuBase.forEach { dia ->
            mapa[dia.dia to "desayuno"] = ComidaPersonal(dia.dia, "desayuno", dia.desayuno.nombre, dia.desayuno.calorias, dia.desayuno.proteinas)
            mapa[dia.dia to "almuerzo"] = ComidaPersonal(dia.dia, "almuerzo", dia.almuerzo.nombre, dia.almuerzo.calorias, dia.almuerzo.proteinas)
            mapa[dia.dia to "cena"] = ComidaPersonal(dia.dia, "cena", dia.cena.nombre, dia.cena.calorias, dia.cena.proteinas)
        }
        comidasPersonales.forEach { mapa[it.dia to it.momento] = it }
        mapa.toMutableMap()
    }.let { remember { mutableStateOf(it) } }.value

    var comidaEditando by remember { mutableStateOf<Pair<String, String>?>(null) }

    if (comidaEditando != null) {
        val key = comidaEditando!!
        val actual = menuLocal[key] ?: return
        DialogEditarComida(
            comida = actual,
            strings = strings,
            onConfirmar = { nueva -> menuLocal[key] = nueva; comidaEditando = null },
            onDescartar = { comidaEditando = null }
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

            DIAS.forEach { dia ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(dia, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C853))
                            Spacer(modifier = Modifier.height(12.dp))
                            MOMENTOS.forEach { (clave, etiqueta) ->
                                val comida = menuLocal[dia to clave]
                                FilaComidaEditable(
                                    etiqueta = etiqueta,
                                    nombreComida = comida?.nombre ?: "—",
                                    calorias = comida?.calorias ?: 0,
                                    onEditar = { comidaEditando = dia to clave }
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
                    onClick = { vm.guardarTodo(menuLocal.values.toList()) },
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
            if (calorias > 0) Text("$calorias kcal", color = Color(0xFF00C853), fontSize = 11.sp)
        }
        IconButton(onClick = onEditar) { Text("✏️", fontSize = 16.sp) }
    }
}

@Composable
private fun DialogEditarComida(comida: ComidaPersonal, strings: com.example.fitme.AppStrings, onConfirmar: (ComidaPersonal) -> Unit, onDescartar: () -> Unit) {
    var nombre by remember { mutableStateOf(comida.nombre) }
    var calorias by remember { mutableStateOf(if (comida.calorias > 0) comida.calorias.toString() else "") }
    var proteinas by remember { mutableStateOf(if (comida.proteinas > 0) comida.proteinas.toString() else "") }

    val momentoLabel = when (comida.momento) {
        "desayuno" -> strings.breakfastLabel
        "almuerzo" -> strings.lunchLabel
        else -> strings.dinnerLabel
    }

    AlertDialog(
        onDismissRequest = onDescartar,
        containerColor = Color(0xFF1E1E1E),
        title = {
            Text(text = "$momentoLabel — ${comida.dia}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre, onValueChange = { nombre = it },
                    label = { Text(strings.foodFieldLabel, color = Color.White.copy(alpha = 0.7f)) },
                    colors = dialogCampoColores(), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = calorias, onValueChange = { calorias = it }, label = { Text(strings.kcalFieldLabel, color = Color.White.copy(alpha = 0.7f)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = dialogCampoColores(), shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = proteinas, onValueChange = { proteinas = it }, label = { Text(strings.proteinFieldLabel, color = Color.White.copy(alpha = 0.7f)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = dialogCampoColores(), shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f), singleLine = true)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmar(comida.copy(nombre = nombre.trim().ifEmpty { comida.nombre }, calorias = calorias.toIntOrNull() ?: comida.calorias, proteinas = proteinas.toIntOrNull() ?: comida.proteinas)) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = RoundedCornerShape(8.dp)
            ) { Text(strings.saveBtn, fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDescartar) { Text(strings.cancelBtn, color = Color.White.copy(alpha = 0.6f)) } }
    )
}

@Composable
private fun dialogCampoColores() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF00C853), unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
    focusedTextColor = Color.White, unfocusedTextColor = Color.White, cursorColor = Color(0xFF00C853),
    focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent
)

@Preview(showBackground = true)
@Composable
fun EditarMenuPreview() {
    FitMeTheme {
        // Usamos un Box para simular el fondo sin llamar a toda la lógica
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.gym_bg),
                    contentScale = ContentScale.Crop
                )
        ) {
            // En lugar de llamar a EditarMenuScreen(), dibujamos la estructura básica
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Editar Menú Semanal", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Personaliza tus comidas", color = Color.White.copy(alpha = 0.55f), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Simulamos un par de días para ver el diseño
                listOf("Lunes", "Martes").forEach { dia ->
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(dia, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C853))
                                Spacer(modifier = Modifier.height(12.dp))

                                // Fila de ejemplo
                                FilaComidaEditable(
                                    etiqueta = "Desayuno",
                                    nombreComida = "Avena con frutas",
                                    calorias = 350,
                                    onEditar = {}
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
