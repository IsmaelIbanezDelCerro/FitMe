package com.example.fitme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.data.entity.CheckDiario
import com.example.fitme.viewmodel.CheckRachaViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RachaScreen(onIrCheckDiario: () -> Unit) {
    val strings = LocalAppStrings.current
    val vm: CheckRachaViewModel = viewModel()
    val checks by vm.checks.collectAsState()

    val rachaActual = remember(checks) { vm.calcularRachaActual(checks) }
    val mejorRacha = remember(checks) { vm.calcularMejorRacha(checks) }
    val fechaHoy = vm.fechaHoy()
    val checkHoy = checks.find { it.fecha == fechaHoy }
    val hoyCompletado = checkHoy != null && (checkHoy.ejercicioHecho || checkHoy.dietaHecha)

    GymBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(strings.rachaTitle, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    LanguageToggleButton()
                }
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(
                            if (rachaActual > 0) Color(0xFF00C853).copy(alpha = 0.15f) else Color(0xFF1A1A1A),
                            CircleShape
                        )
                        .border(
                            width = 4.dp,
                            color = if (rachaActual > 0) Color(0xFF00C853) else Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = if (rachaActual > 0) "🔥" else "💤", fontSize = 40.sp)
                        Text(text = "$rachaActual", fontSize = 52.sp, fontWeight = FontWeight.Bold, color = if (rachaActual > 0) Color(0xFF00C853) else Color.White.copy(alpha = 0.4f))
                        Text(text = if (rachaActual == 1) strings.dayLabel else strings.daysLabel, fontSize = 16.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when {
                        rachaActual == 0 -> strings.streakMsg0
                        rachaActual < 7 -> strings.streakMsg7
                        rachaActual < 30 -> strings.streakMsg30
                        else -> strings.streakMsgLegend
                    },
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaStat(strings.bestStreakLabel, "$mejorRacha ${strings.daysLabel}", Modifier.weight(1f))
                    TarjetaStat(strings.totalDaysLabel, "${checks.count { it.ejercicioHecho || it.dietaHecha }}", Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = if (hoyCompletado) Color(0xFF00C853).copy(alpha = 0.15f) else Color(0xFF1A1A1A).copy(alpha = 0.92f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (hoyCompletado) "✅" else "⏳", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = strings.todayLabel, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                text = if (hoyCompletado) {
                                    val ej = if (checkHoy?.ejercicioHecho == true) strings.exerciseCheckChip else ""
                                    val diet = if (checkHoy?.dietaHecha == true) strings.dietCheckChip else ""
                                    listOf(ej, diet).filter { it.isNotEmpty() }.joinToString("  ")
                                } else strings.noCheckYetMsg,
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onIrCheckDiario,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (hoyCompletado) Color.DarkGray else Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = if (hoyCompletado) strings.viewCheckBtn else strings.registerCheckBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (checks.isNotEmpty()) {
                    Text(strings.dayHistoryTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (checks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(strings.noRegisteredDaysMsg, color = Color.White.copy(alpha = 0.5f), textAlign = TextAlign.Center, fontSize = 14.sp)
                    }
                }
            } else {
                items(checks) { check ->
                    FilaCheck(check)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun TarjetaStat(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor, color = Color(0xFF00C853), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(titulo, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun FilaCheck(check: CheckDiario) {
    val ambos = check.ejercicioHecho && check.dietaHecha
    val ninguno = !check.ejercicioHecho && !check.dietaHecha
    val colorFondo = when { ambos -> Color(0xFF00C853).copy(alpha = 0.15f); ninguno -> Color(0xFF1A1A1A); else -> Color(0xFFFFC107).copy(alpha = 0.1f) }
    val icono = when { ambos -> "🔥"; ninguno -> "—"; else -> "✓" }
    val colorIcono = when { ambos -> Color(0xFF00C853); ninguno -> Color.White.copy(alpha = 0.3f); else -> Color(0xFFFFC107) }

    val fechaDisplay = try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfOut = SimpleDateFormat("d MMM", Locale("es", "ES"))
        sdfOut.format(sdf.parse(check.fecha) ?: Date())
    } catch (e: Exception) { check.fecha }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = colorFondo)) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icono, fontSize = 20.sp, color = colorIcono)
            Spacer(modifier = Modifier.width(12.dp))
            Text(fechaDisplay, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipCheck("💪", check.ejercicioHecho)
                ChipCheck("🥗", check.dietaHecha)
            }
        }
    }
}

@Composable
private fun ChipCheck(icono: String, completado: Boolean) {
    Box(
        modifier = Modifier
            .background(if (completado) Color(0xFF00C853).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = icono, fontSize = 14.sp, color = if (completado) Color.White else Color.White.copy(alpha = 0.3f))
    }
}
