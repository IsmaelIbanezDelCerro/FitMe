package com.example.fitme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.fitme.LocalIsSpanish
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.entity.RegistroEjercicio
import com.example.fitme.viewmodel.MenuPersonalViewModel
import com.example.fitme.viewmodel.RegistroEjercicioViewModel
import java.util.Calendar

@Composable
fun CalendarioScreen(onVolver: () -> Unit) {
    val strings = LocalAppStrings.current
    val isSpanish = LocalIsSpanish.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val vmMenu: MenuPersonalViewModel = viewModel()
    val comidasPersonales by vmMenu.comidasPersonales.collectAsState()

    val now = remember { Calendar.getInstance() }
    var currentYear by remember { mutableStateOf(now.get(Calendar.YEAR)) }
    var currentMonth by remember { mutableStateOf(now.get(Calendar.MONTH)) }
    var diaSeleccionado by remember { mutableStateOf<Int?>(null) }
    var rutinaExpandida by remember(diaSeleccionado, currentMonth, currentYear) { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var ejDialog by remember { mutableStateOf<Ejercicio?>(null) }
    var rutinaNameDialog by remember { mutableStateOf("") }
    var dialogSeries by remember { mutableStateOf("") }
    var dialogReps by remember { mutableStateOf("") }
    var dialogPeso by remember { mutableStateOf("") }

    val vmRegistro: RegistroEjercicioViewModel = viewModel()

    val registrosDia by produceState<List<RegistroEjercicio>>(
        initialValue = emptyList(),
        key1 = diaSeleccionado, key2 = currentMonth, key3 = currentYear
    ) {
        val d = diaSeleccionado
        if (d != null) {
            val fecha = "%04d-%02d-%02d".format(currentYear, currentMonth + 1, d)
            vmRegistro.obtenerRegistrosDia(fecha).collect { value = it }
        } else {
            value = emptyList()
        }
    }

    val todayYear = now.get(Calendar.YEAR)
    val todayMonth = now.get(Calendar.MONTH)
    val todayDay = now.get(Calendar.DAY_OF_MONTH)

    val monthNamesEs = listOf("Enero","Febrero","Marzo","Abril","Mayo","Junio",
        "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")
    val monthNamesEn = listOf("January","February","March","April","May","June",
        "July","August","September","October","November","December")
    val monthNames = if (isSpanish) monthNamesEs else monthNamesEn

    val dayHeadersEs = listOf("Lu","Ma","Mi","Ju","Vi","Sa","Do")
    val dayHeadersEn = listOf("Mo","Tu","We","Th","Fr","Sa","Su")
    val dayHeaders = if (isSpanish) dayHeadersEs else dayHeadersEn

    val menu = remember(comidasPersonales, prefs.objetivo) {
        val diasSemana = listOf("Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo")
        val base = generarMenu(prefs.objetivo)
        if (comidasPersonales.isNotEmpty()) {
            diasSemana.map { dia ->
                val baseDia = base.find { it.dia == dia }
                fun comidaParaMomento(momento: String, baseComida: ComidaDia): ComidaDia {
                    val personal = comidasPersonales.find { it.dia == dia && it.momento == momento }
                    return if (personal != null) ComidaDia(personal.nombre, personal.calorias, personal.proteinas)
                    else baseComida
                }
                DiaMenu(
                    dia = dia,
                    desayuno = comidaParaMomento("desayuno", baseDia?.desayuno ?: ComidaDia("—", 0, 0)),
                    almuerzo = comidaParaMomento("almuerzo", baseDia?.almuerzo ?: ComidaDia("—", 0, 0)),
                    cena = comidaParaMomento("cena", baseDia?.cena ?: ComidaDia("—", 0, 0))
                )
            }
        } else {
            base
        }
    }

    val firstOfMonth = remember(currentYear, currentMonth) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, currentMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val paddingDays = remember(currentYear, currentMonth) {
        val fdow = firstOfMonth.get(Calendar.DAY_OF_WEEK)
        (fdow + 5) % 7
    }
    val daysInMonth = remember(currentYear, currentMonth) {
        firstOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun dayOfWeekForDay(day: Int): Int {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, currentMonth)
            set(Calendar.DAY_OF_MONTH, day)
        }.get(Calendar.DAY_OF_WEEK)
    }

    fun nombreDiaSemana(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miércoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            Calendar.SATURDAY -> "Sábado"
            Calendar.SUNDAY -> "Domingo"
            else -> ""
        }
    }

    GymBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    strings.calendarTitle,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                LanguageToggleButton()
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Navegación de mes
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .clickable {
                                    if (currentMonth == 0) { currentMonth = 11; currentYear-- }
                                    else currentMonth--
                                    diaSeleccionado = null
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text("◀", color = Color(0xFF00C853), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Text(
                            "${monthNames[currentMonth]} $currentYear",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .clickable {
                                    if (currentMonth == 11) { currentMonth = 0; currentYear++ }
                                    else currentMonth++
                                    diaSeleccionado = null
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text("▶", color = Color(0xFF00C853), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Cabecera días de la semana
                    Row(modifier = Modifier.fillMaxWidth()) {
                        dayHeaders.forEach { header ->
                            Text(
                                header,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                color = Color(0xFF00C853),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Grid del calendario
                    val totalCells = paddingDays + daysInMonth
                    val rows = (totalCells + 6) / 7

                    for (row in 0 until rows) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (col in 0 until 7) {
                                val cellIndex = row * 7 + col
                                val day = cellIndex - paddingDays + 1

                                if (day < 1 || day > daysInMonth) {
                                    Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                                } else {
                                    val dow = dayOfWeekForDay(day)
                                    val rutina = obtenerRutinaParaDia(prefs.diasEntrenamiento, dow)
                                    val isTraining = rutina.nombre != "Descanso Activo"
                                    val isToday = currentYear == todayYear && currentMonth == todayMonth && day == todayDay
                                    val isSelected = diaSeleccionado == day

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .background(
                                                when {
                                                    isSelected -> Color(0xFF00C853).copy(alpha = 0.25f)
                                                    isToday -> Color.White.copy(alpha = 0.08f)
                                                    else -> Color.Transparent
                                                },
                                                RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                diaSeleccionado = if (diaSeleccionado == day) null else day
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                "$day",
                                                color = when {
                                                    isToday -> Color(0xFF00C853)
                                                    isSelected -> Color.White
                                                    else -> Color.White.copy(alpha = 0.85f)
                                                },
                                                fontSize = 13.sp,
                                                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .background(
                                                        if (isTraining) Color(0xFF00C853)
                                                        else Color.White.copy(alpha = 0.25f),
                                                        CircleShape
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (row < rows - 1) Spacer(modifier = Modifier.height(4.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Leyenda
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(7.dp).background(Color(0xFF00C853), CircleShape))
                            Text(strings.calendarTrainingLabel, color = Color.White.copy(alpha = 0.55f), fontSize = 11.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(7.dp).background(Color.White.copy(alpha = 0.25f), CircleShape))
                            Text("Descanso", color = Color.White.copy(alpha = 0.55f), fontSize = 11.sp)
                        }
                    }
                }
            }

            // Detalle del día seleccionado
            diaSeleccionado?.let { dia ->
                Spacer(modifier = Modifier.height(16.dp))

                val dow = dayOfWeekForDay(dia)
                val nombreDia = nombreDiaSemana(dow)
                val rutina = obtenerRutinaParaDia(prefs.diasEntrenamiento, dow)
                val diaMenu = menu.find { it.dia == nombreDia }
                val mesLabel = monthNames[currentMonth]

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.95f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            "$nombreDia, $dia de $mesLabel",
                            color = Color(0xFF00C853),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Entrenamiento
                        val esDescanso = rutina.nombre == "Descanso Activo"
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(
                                    if (!esDescanso) Modifier.clickable { rutinaExpandida = !rutinaExpandida }
                                    else Modifier
                                )
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text("💪", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        strings.calendarTrainingLabel,
                                        color = Color.White.copy(alpha = 0.55f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        buildString {
                                            append(rutina.nombre)
                                            if (!esDescanso) append(" · ${rutina.duracionMin} min")
                                        },
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp
                                    )
                                    if (!esDescanso && !rutinaExpandida) {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            "${rutina.ejercicios.size} ejercicios · Toca para ver",
                                            color = Color(0xFF00C853).copy(alpha = 0.7f),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                if (!esDescanso) {
                                    Text(
                                        if (rutinaExpandida) "▲" else "▼",
                                        color = Color(0xFF00C853),
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }

                            if (rutinaExpandida) {
                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                                Spacer(modifier = Modifier.height(10.dp))
                                rutina.ejercicios.forEach { ej ->
                                    TarjetaEjercicioCalendario(
                                        ej = ej,
                                        registrosHoy = registrosDia.filter { it.nombreEjercicio == ej.nombre },
                                        onAgregar = {
                                            ejDialog = ej
                                            rutinaNameDialog = rutina.nombre
                                            dialogSeries = ej.series.toString()
                                            dialogReps = ej.repeticiones
                                            dialogPeso = ""
                                            showDialog = true
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }

                        diaMenu?.let { m ->
                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(14.dp))

                            Row(verticalAlignment = Alignment.Top) {
                                Text("🥗", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        strings.calendarMealsLabel,
                                        color = Color.White.copy(alpha = 0.55f),
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    FilaComidaCalendario(strings.breakfastLabel, m.desayuno, strings.proteinSuffix)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    FilaComidaCalendario(strings.lunchLabel, m.almuerzo, strings.proteinSuffix)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    FilaComidaCalendario(strings.dinnerLabel, m.cena, strings.proteinSuffix)
                                }
                            }
                        }
                    }
                }

                if (showDialog && ejDialog != null) {
                    val fecha = "%04d-%02d-%02d".format(currentYear, currentMonth + 1, dia)
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        containerColor = Color(0xFF1E1E1E),
                        title = {
                            Text(
                                ejDialog!!.nombre,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedTextField(
                                    value = dialogSeries,
                                    onValueChange = { dialogSeries = it.filter { c -> c.isDigit() } },
                                    label = { Text("Series", color = Color.White.copy(alpha = 0.6f)) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF00C853),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        cursorColor = Color(0xFF00C853)
                                    )
                                )
                                OutlinedTextField(
                                    value = dialogReps,
                                    onValueChange = { dialogReps = it },
                                    label = { Text("Repeticiones", color = Color.White.copy(alpha = 0.6f)) },
                                    placeholder = { Text("ej: 10 o 8-12", color = Color.White.copy(alpha = 0.3f)) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF00C853),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        cursorColor = Color(0xFF00C853)
                                    )
                                )
                                OutlinedTextField(
                                    value = dialogPeso,
                                    onValueChange = { dialogPeso = it.filter { c -> c.isDigit() || c == '.' } },
                                    label = { Text("Peso (kg)", color = Color.White.copy(alpha = 0.6f)) },
                                    placeholder = { Text("0 si es peso corporal", color = Color.White.copy(alpha = 0.3f)) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF00C853),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        cursorColor = Color(0xFF00C853)
                                    )
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    vmRegistro.guardarRegistro(
                                        fecha = fecha,
                                        nombreRutina = rutinaNameDialog,
                                        nombreEjercicio = ejDialog!!.nombre,
                                        series = dialogSeries.toIntOrNull() ?: 0,
                                        repeticiones = dialogReps,
                                        pesoKg = dialogPeso.toFloatOrNull() ?: 0f
                                    )
                                    showDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                            ) {
                                Text("Guardar", fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancelar", color = Color.White.copy(alpha = 0.6f))
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.backBtn)
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun TarjetaEjercicioCalendario(
    ej: Ejercicio,
    registrosHoy: List<RegistroEjercicio>,
    onAgregar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    ej.nombre,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(Color(0xFF00C853).copy(alpha = 0.18f), RoundedCornerShape(8.dp))
                        .clickable { onAgregar() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text("+ Registrar", color = Color(0xFF00C853), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ChipInfo("${ej.series} series")
                ChipInfo("${ej.repeticiones} reps")
                ChipInfo(ej.descanso)
            }
            if (registrosHoy.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                Spacer(modifier = Modifier.height(6.dp))
                Text("Registrado hoy:", color = Color.White.copy(alpha = 0.45f), fontSize = 10.sp)
                Spacer(modifier = Modifier.height(4.dp))
                registrosHoy.forEach { r ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text("✓", color = Color(0xFF00C853), fontSize = 11.sp)
                        Text(
                            buildString {
                                append("${r.series} series")
                                if (r.repeticiones.isNotBlank()) append(" × ${r.repeticiones} reps")
                                if (r.pesoKg > 0f) append(" · ${r.pesoKg}kg")
                            },
                            color = Color(0xFF00C853),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaComidaCalendario(momento: String, comida: ComidaDia, proteinSuffix: String) {
    Column {
        Text(momento, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        Text(comida.nombre, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        if (comida.calorias > 0) {
            Text(
                "${comida.calorias} kcal · ${comida.proteinas}g $proteinSuffix",
                color = Color(0xFF00C853),
                fontSize = 11.sp
            )
        }
    }
}
