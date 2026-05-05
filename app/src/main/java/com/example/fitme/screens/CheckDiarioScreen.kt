package com.example.fitme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.R
import com.example.fitme.ui.theme.FitMeTheme
import com.example.fitme.viewmodel.CheckRachaViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CheckDiarioScreen(onVolver: () -> Unit = {}) {
    val strings = LocalAppStrings.current
    val vm: CheckRachaViewModel = viewModel()
    val checks by vm.checks.collectAsState()

    val fechaHoy = vm.fechaHoy()
    val checkHoy = checks.find { it.fecha == fechaHoy }

    var ejercicioHecho by remember(checkHoy) { mutableStateOf(checkHoy?.ejercicioHecho ?: false) }
    var dietaHecha by remember(checkHoy) { mutableStateOf(checkHoy?.dietaHecha ?: false) }
    var guardado by remember { mutableStateOf(checkHoy != null) }

    val fechaFormateada = remember {
        val sdf = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
        sdf.format(Date()).replaceFirstChar { it.uppercase() }
    }

    GymBackground {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(strings.checkDiarioTitle, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(fechaFormateada, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                }
                LanguageToggleButton()
            }

            Spacer(modifier = Modifier.height(32.dp))

            val completados = (if (ejercicioHecho) 1 else 0) + (if (dietaHecha) 1 else 0)
            Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { completados / 2f },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF00C853),
                    trackColor = Color.White.copy(alpha = 0.1f),
                    strokeWidth = 10.dp
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$completados/2", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C853))
                    Text(strings.completedLabel, fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            TarjetaSwitch(
                icono = "💪",
                titulo = strings.exerciseSwitchTitle,
                descripcion = strings.exerciseSwitchDesc,
                valor = ejercicioHecho,
                onCambio = { ejercicioHecho = it; guardado = false },
                bloqueado = checkHoy != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            TarjetaSwitch(
                icono = "🥗",
                titulo = strings.dietSwitchTitle,
                descripcion = strings.dietSwitchDesc,
                valor = dietaHecha,
                onCambio = { dietaHecha = it; guardado = false },
                bloqueado = checkHoy != null
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (checkHoy != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00C853).copy(alpha = 0.15f))
                ) {
                    Text(text = strings.checkAlreadySavedMsg, color = Color(0xFF00C853), fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp).fillMaxWidth(), textAlign = TextAlign.Center)
                }
            } else {
                if (guardado) {
                    Text(strings.savedCorrectlyMsg, color = Color(0xFF00C853), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { vm.guardarCheck(ejercicioHecho, dietaHecha); guardado = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !guardado
                ) {
                    Text(strings.saveCheckBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) { Text(strings.backToRachaBtn) }
        }
    }
}

@Composable
private fun TarjetaSwitch(icono: String, titulo: String, descripcion: String, valor: Boolean, onCambio: (Boolean) -> Unit, bloqueado: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (valor) Color(0xFF00C853).copy(alpha = 0.15f) else Color(0xFF1A1A1A).copy(alpha = 0.92f))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icono, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(descripcion, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
            Switch(
                checked = valor,
                onCheckedChange = if (!bloqueado) onCambio else null,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF00C853), uncheckedThumbColor = Color.Gray, uncheckedTrackColor = Color.DarkGray)
            )
        }
    }
}

/*Un ejemplo*/
@Preview(showBackground = true, device = "id:pixel_8")
@Composable
fun CheckDiarioPreview() {
    FitMeTheme {
        // 1. Forzamos un fondo oscuro para que resalte el diseño
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {

            // 2. Usamos un Box con la imagen (para verla en la preview)
            Box(modifier = Modifier.fillMaxSize().paint(
                painter = painterResource(id = R.drawable.gym_bg),
                contentScale = ContentScale.Crop
            )) {

                // 3. Importante: Como la pantalla real pide un ViewModel y Strings,
                // aquí solo simulamos la estructura visual básica.
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título simulado (en lugar de usar strings del VM)
                    Text("Check Diario", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Lunes, 5 de Mayo", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(32.dp))

                    // El círculo de progreso
                    Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { 0.5f }, // 1 de 2 completado
                            modifier = Modifier.fillMaxSize(),
                            color = Color(0xFF00C853),
                            trackColor = Color.White.copy(alpha = 0.1f),
                            strokeWidth = 10.dp
                        )
                        Text("1/2", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C853))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Una tarjeta de ejemplo para ver cómo se ve el diseño
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF00C853).copy(alpha = 0.15f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("💪", fontSize = 32.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Ejercicio", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("¿Entrenaste hoy?", color = Color.White.copy(alpha = 0.6f))
                            }
                            Switch(checked = true, onCheckedChange = {})
                        }
                    }
                }
            }
        }
    }
}