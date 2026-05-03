package com.example.fitme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.entity.EjercicioPersonal
import com.example.fitme.viewmodel.RutinaPersonalViewModel

val CATALOGO_EJERCICIOS: List<Ejercicio> = listOf(
    // Pecho
    Ejercicio("Press de banca", 4, "8-10", "90 seg", "Pectoral mayor — agarre medio"),
    Ejercicio("Press inclinado con mancuernas", 3, "10-12", "75 seg", "Pectoral superior"),
    Ejercicio("Press declinado", 3, "10-12", "75 seg", "Pectoral inferior"),
    Ejercicio("Fondos en paralelas", 3, "Al fallo", "60 seg", "Pectoral inferior y tríceps"),
    Ejercicio("Aperturas con mancuernas", 3, "12-15", "60 seg", "Estiramiento de pectoral"),
    Ejercicio("Flexiones de brazo", 4, "15-20", "45 seg", "Sin material, pecho y tríceps"),
    // Espalda
    Ejercicio("Dominadas", 4, "Al fallo", "90 seg", "Dorsal ancho, bíceps"),
    Ejercicio("Remo con barra", 4, "8-10", "90 seg", "Espalda media, trapecios"),
    Ejercicio("Jalón al pecho", 3, "12-15", "75 seg", "Dorsal ancho"),
    Ejercicio("Remo con mancuerna", 3, "10-12 por lado", "60 seg", "Espalda unilateral"),
    Ejercicio("Peso muerto", 4, "6-8", "120 seg", "Cadena posterior completa"),
    Ejercicio("Peso muerto rumano", 4, "8-10", "90 seg", "Isquiotibiales y glúteos"),
    // Piernas
    Ejercicio("Sentadilla", 4, "8-10", "120 seg", "Cuádriceps, glúteos"),
    Ejercicio("Prensa de piernas", 3, "12-15", "90 seg", "Cuádriceps, glúteos"),
    Ejercicio("Zancadas con mancuernas", 3, "12 por pierna", "75 seg", "Cuádriceps unilateral"),
    Ejercicio("Sentadilla búlgara", 3, "8 por pierna", "90 seg", "Pie trasero elevado"),
    Ejercicio("Curl femoral tumbado", 4, "12-15", "75 seg", "Isquiotibiales"),
    Ejercicio("Hip thrust con barra", 4, "10-12", "90 seg", "Glúteo mayor"),
    Ejercicio("Elevaciones de gemelo", 4, "15-20", "60 seg", "Gemelos"),
    Ejercicio("Sentadilla sumo", 4, "12-15", "75 seg", "Aductores y glúteos"),
    // Hombros
    Ejercicio("Press militar con barra", 4, "8-10", "90 seg", "Deltoides anterior y medial"),
    Ejercicio("Press de hombros con mancuernas", 3, "10-12", "75 seg", "Deltoides"),
    Ejercicio("Elevaciones laterales", 4, "12-15", "60 seg", "Deltoides medial"),
    Ejercicio("Pájaros (elevaciones posteriores)", 3, "15", "60 seg", "Deltoides posterior"),
    // Bíceps
    Ejercicio("Curl de bíceps con barra", 4, "10-12", "60 seg", "Bíceps braquial"),
    Ejercicio("Curl de bíceps alterno con mancuernas", 3, "12-15", "60 seg", "Bíceps y braquiorradial"),
    Ejercicio("Curl martillo", 3, "12-15", "60 seg", "Braquiorradial"),
    Ejercicio("Curl concentrado", 3, "12 por brazo", "60 seg", "Pico del bíceps"),
    // Tríceps
    Ejercicio("Press francés", 3, "10-12", "75 seg", "Larga cabeza del tríceps"),
    Ejercicio("Extensiones en polea alta", 4, "12-15", "60 seg", "Tríceps completo"),
    Ejercicio("Fondos en banco", 3, "15", "45 seg", "Tríceps con el peso corporal"),
    Ejercicio("Patada de tríceps", 3, "15", "45 seg", "Cabeza lateral del tríceps"),
    // Core
    Ejercicio("Plancha abdominal", 3, "60 seg", "45 seg", "Core completo — cuerpo recto"),
    Ejercicio("Plancha lateral", 3, "45 seg/lado", "45 seg", "Oblicuos"),
    Ejercicio("Crunches abdominales", 3, "20", "45 seg", "Recto abdominal"),
    Ejercicio("Elevación de piernas", 3, "15", "45 seg", "Abdomen bajo"),
    Ejercicio("Russian twists", 3, "20 total", "30 seg", "Oblicuos — con o sin peso"),
    Ejercicio("Dead bug", 3, "10 por lado", "45 seg", "Estabilidad lumbar"),
    // Cardio / HIIT
    Ejercicio("Burpees", 4, "15", "30 seg", "Full body — alta intensidad"),
    Ejercicio("Mountain climbers", 4, "30 seg", "15 seg", "Core + cardio"),
    Ejercicio("Jumping jacks", 3, "45 seg", "20 seg", "Calentamiento o HIIT"),
    Ejercicio("Sentadillas con salto", 3, "20", "30 seg", "Piernas + cardio"),
    Ejercicio("Sprint en estático", 4, "20 seg", "40 seg", "Máxima velocidad posible"),
    Ejercicio("Saltos al cajón", 3, "10", "90 seg", "Potencia de piernas"),
    // Movilidad
    Ejercicio("Movilidad de cadera", 2, "10 por lado", "30 seg", "Círculos lentos y controlados"),
    Ejercicio("Estiramiento de isquiotibiales", 2, "30 seg/lado", "20 seg", "Sin forzar"),
    Ejercicio("Rotación torácica", 2, "10 por lado", "30 seg", "Movilidad de columna")
)

@Composable
fun EditarRutinaScreen(onVolver: () -> Unit) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val vm: RutinaPersonalViewModel = viewModel()
    val misEjercicios by vm.ejercicios.collectAsState()

    var busqueda by remember { mutableStateOf("") }
    var expandidoId by remember { mutableStateOf<Int?>(null) }

    var mostrarFormPersonal by remember { mutableStateOf(false) }
    var nombrePersonal by remember { mutableStateOf("") }
    var seriesPersonal by remember { mutableStateOf("3") }
    var repsPersonal by remember { mutableStateOf("12") }
    var descansoPersonal by remember { mutableStateOf("60 seg") }
    var descripcionPersonal by remember { mutableStateOf("") }

    val catalogoFiltrado = remember(busqueda) {
        if (busqueda.isEmpty()) CATALOGO_EJERCICIOS
        else CATALOGO_EJERCICIOS.filter { it.nombre.contains(busqueda, ignoreCase = true) }
    }

    GymBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(strings.editarRutinaTitle, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(strings.editarRutinaHint, color = Color.White.copy(alpha = 0.55f), fontSize = 13.sp)
                    }
                    LanguageToggleButton()
                }
                Spacer(modifier = Modifier.height(16.dp))

                val rutinaDefault = obtenerRutinaDia(prefs.diasEntrenamiento)
                OutlinedButton(
                    onClick = {
                        val lista = rutinaDefault.ejercicios.mapIndexed { i, ej ->
                            EjercicioPersonal(
                                nombre = ej.nombre, series = ej.series,
                                repeticiones = ej.repeticiones, descanso = ej.descanso,
                                descripcion = ej.descripcion, orden = i
                            )
                        }
                        vm.cargarPredeterminada(lista)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C853)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("${strings.loadDefaultRoutinePrefix} \"${rutinaDefault.nombre}\"", fontSize = 13.sp)
                }

                if (misEjercicios.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    TextButton(
                        onClick = { vm.limpiar() },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red.copy(alpha = 0.7f))
                    ) {
                        Text(strings.clearPersonalRoutineBtn, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(
                    text = "${strings.myCurrentRoutinePrefix} (${misEjercicios.size} ${strings.exercisesCountSuffix})",
                    fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (misEjercicios.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                            Text(
                                strings.noExercisesYetMsg,
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                items(misEjercicios, key = { it.id }) { ep ->
                    val expandido = expandidoId == ep.id
                    TarjetaEjercicioEditable(
                        ejercicio = ep,
                        expandido = expandido,
                        onToggleExpand = { expandidoId = if (expandido) null else ep.id },
                        onActualizar = { vm.actualizar(it) },
                        onEliminar = { vm.eliminar(ep) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(strings.addOwnExerciseTitle, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    IconButton(onClick = { mostrarFormPersonal = !mostrarFormPersonal }) {
                        Text(if (mostrarFormPersonal) "▲" else "▼", color = Color(0xFF00C853), fontSize = 16.sp)
                    }
                }
            }

            if (mostrarFormPersonal) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            CampoEdicion(strings.exerciseNameFieldLabel, nombrePersonal) { nombrePersonal = it }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                CampoEdicion(strings.seriesFieldLabel, seriesPersonal, Modifier.weight(1f), KeyboardType.Number) { seriesPersonal = it }
                                CampoEdicion(strings.repsFieldLabel, repsPersonal, Modifier.weight(1f)) { repsPersonal = it }
                                CampoEdicion(strings.restFieldLabel, descansoPersonal, Modifier.weight(1f)) { descansoPersonal = it }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            CampoEdicion(strings.descriptionFieldLabel, descripcionPersonal) { descripcionPersonal = it }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    if (nombrePersonal.isNotEmpty()) {
                                        vm.agregar(
                                            EjercicioPersonal(
                                                nombre = nombrePersonal,
                                                series = seriesPersonal.toIntOrNull() ?: 3,
                                                repeticiones = repsPersonal,
                                                descanso = descansoPersonal,
                                                descripcion = descripcionPersonal,
                                                orden = misEjercicios.size
                                            )
                                        )
                                        nombrePersonal = ""; seriesPersonal = "3"; repsPersonal = "12"
                                        descansoPersonal = "60 seg"; descripcionPersonal = ""
                                        mostrarFormPersonal = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(strings.addToRoutineBtn, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))
                Text(strings.exerciseCatalogTitle, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    placeholder = { Text(strings.searchExercisePlaceholder, color = Color.White.copy(alpha = 0.4f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00C853),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF00C853)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(catalogoFiltrado) { ej ->
                val yaEnRutina = misEjercicios.any { it.nombre == ej.nombre }
                FilaCatalogo(
                    ejercicio = ej,
                    yaAnadido = yaEnRutina,
                    onAnadir = {
                        if (!yaEnRutina) {
                            vm.agregar(
                                EjercicioPersonal(
                                    nombre = ej.nombre, series = ej.series,
                                    repeticiones = ej.repeticiones, descanso = ej.descanso,
                                    descripcion = ej.descripcion, orden = misEjercicios.size
                                )
                            )
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text(strings.backToRoutineBtn) }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun TarjetaEjercicioEditable(
    ejercicio: EjercicioPersonal,
    expandido: Boolean,
    onToggleExpand: () -> Unit,
    onActualizar: (EjercicioPersonal) -> Unit,
    onEliminar: () -> Unit
) {
    val strings = LocalAppStrings.current
    var series by remember(ejercicio.id) { mutableStateOf(ejercicio.series.toString()) }
    var reps by remember(ejercicio.id) { mutableStateOf(ejercicio.repeticiones) }
    var descanso by remember(ejercicio.id) { mutableStateOf(ejercicio.descanso) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onToggleExpand() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(ejercicio.nombre, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(
                        "${ejercicio.series} ${strings.seriesChip} · ${ejercicio.repeticiones} ${strings.repsChip} · ${ejercicio.descanso}",
                        color = Color.White.copy(alpha = 0.55f), fontSize = 12.sp
                    )
                }
                Text(if (expandido) "▲" else "▼", color = Color(0xFF00C853), fontSize = 13.sp)
            }

            if (expandido) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CampoEdicion(strings.seriesFieldLabel, series, Modifier.weight(1f), KeyboardType.Number) { series = it }
                    CampoEdicion(strings.repsFieldLabel, reps, Modifier.weight(1f)) { reps = it }
                    CampoEdicion(strings.restFieldLabel, descanso, Modifier.weight(1f)) { descanso = it }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            onActualizar(
                                ejercicio.copy(
                                    series = series.toIntOrNull() ?: ejercicio.series,
                                    repeticiones = reps,
                                    descanso = descanso
                                )
                            )
                            onToggleExpand()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text(strings.saveExerciseBtn, fontSize = 13.sp, fontWeight = FontWeight.Bold) }

                    OutlinedButton(
                        onClick = onEliminar,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red.copy(alpha = 0.8f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text(strings.deleteExerciseBtn, fontSize = 13.sp) }
                }
            }
        }
    }
}

@Composable
private fun FilaCatalogo(ejercicio: Ejercicio, yaAnadido: Boolean, onAnadir: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(ejercicio.nombre, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(
                "${ejercicio.series} series · ${ejercicio.repeticiones} · ${ejercicio.descanso}",
                color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp
            )
            if (ejercicio.descripcion.isNotEmpty()) {
                Text(ejercicio.descripcion, color = Color.White.copy(alpha = 0.35f), fontSize = 11.sp)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (yaAnadido) {
            Text("✓", color = Color(0xFF00C853), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        } else {
            IconButton(onClick = onAnadir) {
                Text("+", color = Color(0xFF00C853), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
    HorizontalDivider(color = Color.White.copy(alpha = 0.07f))
}

@Composable
private fun CampoEdicion(
    label: String,
    valor: String,
    modifier: Modifier = Modifier,
    teclado: KeyboardType = KeyboardType.Text,
    onCambio: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF00C853),
            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color(0xFF00C853)
        ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = modifier,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 13.sp)
    )
}
