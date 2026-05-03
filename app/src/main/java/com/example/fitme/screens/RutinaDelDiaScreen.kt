package com.example.fitme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.entity.EjercicioPersonal
import com.example.fitme.viewmodel.EntrenamientoViewModel
import com.example.fitme.viewmodel.RutinaPersonalViewModel
import java.util.*

data class Ejercicio(
    val nombre: String,
    val series: Int,
    val repeticiones: String,
    val descanso: String,
    val descripcion: String
)

data class Rutina(val nombre: String, val duracionMin: Int, val ejercicios: List<Ejercicio>)

fun EjercicioPersonal.toEjercicio() = Ejercicio(
    nombre = nombre, series = series, repeticiones = repeticiones,
    descanso = descanso, descripcion = descripcion
)

@Composable
fun RutinaDelDiaScreen(onVerHistorial: () -> Unit, onEditarRutina: () -> Unit) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val vmEntrenamiento: EntrenamientoViewModel = viewModel()
    val vmPersonal: RutinaPersonalViewModel = viewModel()

    val historial by vmEntrenamiento.historial.collectAsState()
    val ejerciciosPersonales by vmPersonal.ejercicios.collectAsState()

    val tieneRutinaPersonal = ejerciciosPersonales.isNotEmpty()
    val rutinaDefault = remember(prefs.diasEntrenamiento) { obtenerRutinaDia(prefs.diasEntrenamiento) }

    val nombreRutina = if (tieneRutinaPersonal) strings.myPersonalRoutineLabel else rutinaDefault.nombre
    val duracion = if (tieneRutinaPersonal) ejerciciosPersonales.size * 7 else rutinaDefault.duracionMin

    var completada by remember { mutableStateOf(false) }
    val yaCompletadaHoy = historial.any { it.fecha == fechaHoyStr() && it.nombreRutina == nombreRutina }

    GymBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(strings.rutinaTitle, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(nombreRutina, color = Color(0xFF00C853), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("⏱ $duracion ${strings.estimatedMinutes}", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        LanguageToggleButton()
                        OutlinedButton(
                            onClick = onEditarRutina,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C853)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00C853)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(strings.editBtnLabel, fontSize = 12.sp)
                        }
                    }
                }
                if (tieneRutinaPersonal) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(strings.personalRoutinaActiveLabel, color = Color(0xFF00C853).copy(alpha = 0.8f), fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (tieneRutinaPersonal) {
                items(ejerciciosPersonales) { ep ->
                    TarjetaEjercicio(ep.toEjercicio())
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                items(rutinaDefault.ejercicios) { ej ->
                    TarjetaEjercicio(ej)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                if (yaCompletadaHoy || completada) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF00C853).copy(alpha = 0.2f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("✓", color = Color(0xFF00C853), fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(strings.trainingDoneTodayMsg, color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            vmEntrenamiento.guardarEntrenamiento(nombreRutina, duracion)
                            completada = true
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(strings.markDoneBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onVerHistorial,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C853)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(strings.viewHistorialBtn)
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun TarjetaEjercicio(ej: Ejercicio) {
    val strings = LocalAppStrings.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(ej.nombre, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipInfo("${ej.series} ${strings.seriesChip}")
                ChipInfo("${ej.repeticiones} ${strings.repsChip}")
                ChipInfo("${ej.descanso} ${strings.restChip}")
            }
            if (ej.descripcion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(ej.descripcion, color = Color.White.copy(alpha = 0.55f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ChipInfo(texto: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF00C853).copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(texto, color = Color(0xFF00C853), fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

fun obtenerRutinaDia(diasEntrenamiento: Int): Rutina {
    val dia = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    val rutinas = when {
        diasEntrenamiento <= 2 -> rutinasGrupoMuscularAlta
        diasEntrenamiento <= 4 -> rutinasGrupoMuscularMedia
        else -> rutinasGrupoMuscularBaja
    }
    return rutinas[dia % rutinas.size]
}

fun fechaHoyStr(): String {
    val cal = Calendar.getInstance()
    return "%04d-%02d-%02d".format(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
}

val rutinasGrupoMuscularBaja = listOf(
    Rutina("Pecho", 40, listOf(
        Ejercicio("Flexiones de brazo", 3, "10-12", "90 seg", "Manos a la anchura de los hombros"),
        Ejercicio("Press de pecho con mancuernas", 3, "12", "75 seg", "Peso ligero, rango completo"),
        Ejercicio("Aperturas con mancuernas", 2, "12", "60 seg", "Movimiento en arco controlado"),
        Ejercicio("Fondos en silla", 2, "10", "60 seg", "Baja 90 grados")
    )),
    Rutina("Espalda", 40, listOf(
        Ejercicio("Jalón al pecho con goma", 3, "15", "75 seg", "Escápulas hacia abajo y juntas"),
        Ejercicio("Remo con mancuerna", 3, "12 por lado", "75 seg", "Espalda recta y paralela al suelo"),
        Ejercicio("Superman", 3, "12", "60 seg", "Contrae los glúteos al subir"),
        Ejercicio("Remo invertido en barra", 2, "10", "60 seg", "Pecho hacia la barra")
    )),
    Rutina("Piernas", 45, listOf(
        Ejercicio("Sentadilla con peso corporal", 3, "15", "60 seg", "Rodillas alineadas con los pies"),
        Ejercicio("Zancadas alternas", 3, "10 por pierna", "60 seg", "Paso largo y bajada controlada"),
        Ejercicio("Glute bridge", 3, "15", "45 seg", "Aprieta glúteos arriba"),
        Ejercicio("Elevaciones de gemelo", 3, "20", "45 seg", "Rango completo de movimiento")
    )),
    Rutina("Hombros y Core", 35, listOf(
        Ejercicio("Press de hombros con mancuernas", 3, "12", "75 seg", "Sentado, espalda recta"),
        Ejercicio("Elevaciones laterales", 3, "12", "60 seg", "Codos ligeramente flexionados"),
        Ejercicio("Plancha", 3, "30 seg", "45 seg", "Cuerpo recto como una tabla"),
        Ejercicio("Crunches abdominales", 3, "15", "45 seg", "Sin tirar del cuello")
    )),
    Rutina("Bíceps y Tríceps", 35, listOf(
        Ejercicio("Curl de bíceps con mancuernas", 3, "12", "60 seg", "Codos pegados al cuerpo"),
        Ejercicio("Fondos en silla", 3, "10", "60 seg", "Espalda cerca de la silla"),
        Ejercicio("Curl martillo", 2, "12", "60 seg", "Agarre neutro"),
        Ejercicio("Extensión de tríceps con mancuerna", 2, "12", "60 seg", "Control total en la bajada")
    )),
    Rutina("Full Body", 45, listOf(
        Ejercicio("Sentadilla + press de hombros", 3, "10", "75 seg", "Mancuernas ligeras"),
        Ejercicio("Peso muerto con mancuernas", 3, "10", "75 seg", "Espalda recta"),
        Ejercicio("Flexiones de brazo", 3, "10", "60 seg", "Manos a anchura de hombros"),
        Ejercicio("Plancha abdominal", 3, "30 seg", "45 seg", "Core activado")
    )),
    Rutina("Descanso Activo", 25, listOf(
        Ejercicio("Estiramientos generales", 2, "2 min", "—", "Todo el cuerpo sin forzar"),
        Ejercicio("Caminata suave", 1, "15 min", "—", "Ritmo tranquilo"),
        Ejercicio("Respiración profunda", 1, "5 min", "—", "Inhala 4s, exhala 4s")
    ))
)

val rutinasGrupoMuscularMedia = listOf(
    Rutina("Pecho", 55, listOf(
        Ejercicio("Press de banca", 4, "8-10", "90 seg", "Agarre ligeramente más ancho que los hombros"),
        Ejercicio("Press inclinado con mancuernas", 3, "10-12", "75 seg", "Ángulo 30-45 grados"),
        Ejercicio("Fondos en paralelas", 3, "Al fallo", "60 seg", "Inclina hacia adelante para trabajar el pecho"),
        Ejercicio("Aperturas en banco plano", 3, "12-15", "60 seg", "Codos ligeramente flexionados"),
        Ejercicio("Flexiones diamante", 2, "12", "45 seg", "Manos formando un diamante")
    )),
    Rutina("Espalda", 55, listOf(
        Ejercicio("Dominadas", 4, "Al fallo", "90 seg", "Agarre prono, amplitud de hombros"),
        Ejercicio("Remo con barra", 4, "8-10", "90 seg", "Espalda recta, pecho al frente"),
        Ejercicio("Jalón al pecho", 3, "12-15", "75 seg", "Barra hasta la clavícula"),
        Ejercicio("Remo con mancuerna", 3, "12 por lado", "75 seg", "Codo por encima de la cadera"),
        Ejercicio("Pull-over con mancuerna", 2, "12", "60 seg", "Gran dorsal y serrato")
    )),
    Rutina("Piernas", 60, listOf(
        Ejercicio("Sentadilla", 4, "8-10", "120 seg", "Profundidad hasta paralelo o más"),
        Ejercicio("Prensa de piernas", 3, "12-15", "90 seg", "Pies a la anchura de los hombros"),
        Ejercicio("Zancadas con mancuernas", 3, "12 por pierna", "75 seg", "Rodilla trasera casi toca el suelo"),
        Ejercicio("Curl femoral tumbado", 4, "12-15", "75 seg", "Isquiotibiales"),
        Ejercicio("Elevaciones de gemelo de pie", 4, "15-20", "60 seg", "Rango completo de movimiento")
    )),
    Rutina("Hombros y Core", 50, listOf(
        Ejercicio("Press militar con barra", 4, "8-10", "90 seg", "De pie o sentado"),
        Ejercicio("Elevaciones laterales", 4, "12-15", "60 seg", "Codos ligeramente flexionados"),
        Ejercicio("Pájaros con mancuernas", 3, "15", "60 seg", "Deltoides posterior"),
        Ejercicio("Plancha abdominal", 3, "60 seg", "45 seg", "Cuerpo recto como una tabla"),
        Ejercicio("Encogimientos con peso", 4, "12-15", "60 seg", "Control en la bajada")
    )),
    Rutina("Bíceps y Tríceps", 50, listOf(
        Ejercicio("Curl de bíceps con barra", 4, "10-12", "60 seg", "Sin balanceo del cuerpo"),
        Ejercicio("Press francés", 3, "10-12", "75 seg", "Barra EZ o mancuernas"),
        Ejercicio("Curl concentrado", 3, "12 por brazo", "60 seg", "Codo apoyado en muslo"),
        Ejercicio("Extensión de tríceps en polea", 4, "12-15", "60 seg", "Codos pegados al cuerpo"),
        Ejercicio("Curl martillo alterno", 3, "12", "60 seg", "Trabaja el braquiorradial")
    )),
    Rutina("Full Body", 60, listOf(
        Ejercicio("Peso muerto", 4, "6-8", "120 seg", "Movimiento rey de fuerza total"),
        Ejercicio("Press de banca", 3, "10", "90 seg", "Pecho completo"),
        Ejercicio("Dominadas", 3, "Al fallo", "90 seg", "Espalda amplia"),
        Ejercicio("Sentadilla goblet", 3, "12", "75 seg", "Con mancuerna o kettlebell"),
        Ejercicio("Plancha lateral", 3, "45 seg/lado", "45 seg", "Core lateral")
    )),
    Rutina("Descanso Activo", 30, listOf(
        Ejercicio("Movilidad de cadera", 2, "10 por lado", "30 seg", "Círculos lentos y controlados"),
        Ejercicio("Estiramientos de isquiotibiales", 2, "30 seg/lado", "20 seg", "Sin forzar"),
        Ejercicio("Rodillo de espuma", 1, "5 min", "—", "Espalda, piernas, glúteos"),
        Ejercicio("Respiración y yoga", 1, "10 min", "—", "Reduce el cortisol y mejora la recuperación")
    ))
)

val rutinasGrupoMuscularAlta = listOf(
    Rutina("Pecho", 70, listOf(
        Ejercicio("Press de banca", 5, "6-8", "90 seg", "Cargas pesadas, técnica prioritaria"),
        Ejercicio("Press inclinado con barra", 4, "8-10", "75 seg", "Ángulo 30-45 grados"),
        Ejercicio("Fondos en paralelas lastrados", 4, "8-10", "90 seg", "Añade peso si es posible"),
        Ejercicio("Aperturas en cable", 4, "12-15", "60 seg", "Contracción máxima en el centro"),
        Ejercicio("Press declinado", 3, "10-12", "60 seg", "Parte inferior del pecho"),
        Ejercicio("Flexiones explosivas", 3, "8", "75 seg", "Manos se despegan del suelo")
    )),
    Rutina("Espalda", 70, listOf(
        Ejercicio("Peso muerto", 5, "5", "120 seg", "Técnica prioritaria"),
        Ejercicio("Dominadas lastradas", 4, "6-8", "90 seg", "Añade peso si es posible"),
        Ejercicio("Remo con barra pendlay", 4, "6-8", "90 seg", "Barra toca el suelo cada rep"),
        Ejercicio("Jalón al pecho agarre cerrado", 4, "10-12", "75 seg", "Mayor amplitud de movimiento"),
        Ejercicio("Remo en polea baja", 3, "12-15", "60 seg", "Escápulas bien retractadas"),
        Ejercicio("Face pull", 3, "15", "45 seg", "Salud del manguito rotador")
    )),
    Rutina("Piernas", 75, listOf(
        Ejercicio("Sentadilla", 5, "6-8", "120 seg", "Cargas máximas, profundidad completa"),
        Ejercicio("Sentadilla búlgara", 4, "8 por pierna", "90 seg", "Pie trasero elevado"),
        Ejercicio("Prensa de piernas", 4, "12-15", "90 seg", "Pies altos para isquiotibiales"),
        Ejercicio("Peso muerto rumano", 4, "10", "90 seg", "Cadena posterior"),
        Ejercicio("Hip thrust con barra", 4, "10", "75 seg", "Empuje máximo de cadera"),
        Ejercicio("Elevaciones de gemelo sentado", 4, "20", "60 seg", "Soleo")
    )),
    Rutina("Hombros y Core", 65, listOf(
        Ejercicio("Press militar con barra", 5, "6-8", "90 seg", "De pie, máximo peso"),
        Ejercicio("Elevaciones laterales", 5, "12-15", "45 seg", "Con pausa en la parte alta"),
        Ejercicio("Pájaros en máquina", 4, "15", "45 seg", "Deltoides posterior"),
        Ejercicio("Press Arnold", 3, "10-12", "75 seg", "Rotación completa del movimiento"),
        Ejercicio("Plancha dinámica", 4, "45 seg", "30 seg", "Movimiento constante"),
        Ejercicio("Rueda abdominal", 3, "10", "60 seg", "Core profundo")
    )),
    Rutina("Bíceps y Tríceps", 60, listOf(
        Ejercicio("Curl de bíceps con barra", 5, "8-10", "60 seg", "Progresión lineal de carga"),
        Ejercicio("Press de tríceps en polea", 4, "10-12", "60 seg", "Codos pegados al cuerpo"),
        Ejercicio("Curl inclinado con mancuernas", 4, "12", "60 seg", "Estiramiento máximo del bíceps"),
        Ejercicio("Extensión de tríceps sobre cabeza", 4, "12-15", "60 seg", "Larga cabeza del tríceps"),
        Ejercicio("Curl en banco Scott", 3, "10-12", "75 seg", "Aislamiento del bíceps"),
        Ejercicio("Press cerrado en banco", 3, "10", "75 seg", "Tríceps y pecho interior")
    )),
    Rutina("Full Body", 75, listOf(
        Ejercicio("Sentadilla", 4, "6", "120 seg", "Carga máxima del día"),
        Ejercicio("Press de banca", 4, "6", "120 seg", "Carga máxima del día"),
        Ejercicio("Peso muerto", 4, "5", "120 seg", "Carga máxima del día"),
        Ejercicio("Press militar", 3, "8", "90 seg", "Potencia de hombros"),
        Ejercicio("Dominadas lastradas", 3, "6-8", "90 seg", "Espalda y bíceps"),
        Ejercicio("Remo en máquina", 3, "10", "60 seg", "Finaliza la sesión")
    )),
    Rutina("Descanso Activo", 35, listOf(
        Ejercicio("Movilidad articular general", 2, "5 min", "—", "Todas las articulaciones"),
        Ejercicio("Foam rolling intensivo", 1, "10 min", "—", "Zonas tensas del entrenamiento"),
        Ejercicio("Estiramientos pasivos", 2, "30 seg/postura", "—", "Mantén sin forzar"),
        Ejercicio("Baño frío o contraste", 1, "10 min", "—", "Recuperación activa óptima")
    ))
)
