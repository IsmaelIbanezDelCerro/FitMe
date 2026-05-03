package com.example.fitme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitme.AppStrings
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.data.UserPreferences

@Composable
fun ObjetivoCalculadoScreen(onContinuar: () -> Unit) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }

    val imc = prefs.calcularImc()
    val resultado = remember(imc, strings) { calcularObjetivoPorImc(imc, strings) }

    LaunchedEffect(resultado) {
        if (imc > 0) prefs.objetivo = resultado.objetivoKey
    }

    val colorImc = when {
        imc <= 0f -> Color.Gray
        imc < 18.5f -> Color(0xFF2196F3)
        imc < 25f -> Color(0xFF00C853)
        imc < 30f -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    GymBackground {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                LanguageToggleButton()
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = resultado.emoji, fontSize = 80.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = strings.yourMainGoalLabel, fontSize = 16.sp, color = Color.White.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = resultado.objetivoDisplay, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C853), textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = strings.yourBMILabel, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (imc > 0) {
                        Text(text = "${"%.1f".format(imc)}", fontSize = 56.sp, fontWeight = FontWeight.Bold, color = colorImc)
                        Text(text = prefs.categoriaImc(), color = colorImc, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    } else {
                        Text(text = "—", fontSize = 48.sp, color = Color.White.copy(alpha = 0.3f))
                        Text(text = strings.enterWeightHeightMsg, color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp, textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = resultado.descripcion, color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp, textAlign = TextAlign.Center, lineHeight = 22.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = strings.bmiRangesTitle, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(10.dp))
                    FilaRango(strings.imcUnderweight, "→ ${strings.goalMuscle}", Color(0xFF2196F3), imc < 18.5f && imc > 0, strings.youHereMarker)
                    FilaRango(strings.imcNormal, "→ ${strings.goalSport}", Color(0xFF00C853), imc in 18.5f..24.9f, strings.youHereMarker)
                    FilaRango(strings.imcOverweight, "→ ${strings.goalWeightLoss}", Color(0xFFFFC107), imc in 25f..29.9f, strings.youHereMarker)
                    FilaRango(strings.imcObese, "→ ${strings.goalWeightLoss}", Color(0xFFF44336), imc >= 30f, strings.youHereMarker)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = onContinuar,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = strings.continueBtn, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FilaRango(rangoCategoria: String, objetivo: String, color: Color, esActual: Boolean, youMarker: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = rangoCategoria, color = if (esActual) Color.White else Color.White.copy(alpha = 0.5f), fontSize = 13.sp, fontWeight = if (esActual) FontWeight.Bold else FontWeight.Normal)
            Text(text = objetivo, color = if (esActual) color else Color.White.copy(alpha = 0.3f), fontSize = 11.sp)
        }
        if (esActual) Text(youMarker, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

private data class ResultadoObjetivo(val objetivoKey: String, val objetivoDisplay: String, val descripcion: String, val emoji: String)

private fun calcularObjetivoPorImc(imc: Float, strings: AppStrings): ResultadoObjetivo = when {
    imc <= 0f -> ResultadoObjetivo("Entrenamiento Deportivo", strings.goalSport, strings.descNoImc, "🏋️")
    imc < 18.5f -> ResultadoObjetivo("Ganancia Muscular", strings.goalMuscle, strings.descUnderweight, "💪")
    imc < 25f -> ResultadoObjetivo("Mantenimiento Deportivo", strings.goalSport, strings.descNormal, "🏃")
    imc < 30f -> ResultadoObjetivo("Pérdida de Peso", strings.goalWeightLoss, strings.descOverweight, "🔥")
    else -> ResultadoObjetivo("Pérdida de Peso", strings.goalWeightLoss, strings.descObese, "🌱")
}
