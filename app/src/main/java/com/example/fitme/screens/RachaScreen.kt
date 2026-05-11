package com.example.fitme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.fitme.viewmodel.CheckRachaViewModel

@Composable
fun RachaScreen(onIrCheckDiario: () -> Unit) {
    val strings = LocalAppStrings.current
    val vm: CheckRachaViewModel = viewModel()
    val racha by vm.racha.collectAsState()

    val rachaActual = racha.diasConsecutivos
    val mejorRacha = racha.rachaMaxima
    val fechaHoy = vm.fechaHoy()
    val hoyCompletado = racha.ultimaActividad == fechaHoy

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
                    TarjetaStat(strings.totalDaysLabel, "$rachaActual ${strings.daysLabel}", Modifier.weight(1f))
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
                                text = if (hoyCompletado) strings.exerciseCheckChip else strings.noCheckYetMsg,
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

                Spacer(modifier = Modifier.height(80.dp))
            }
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
