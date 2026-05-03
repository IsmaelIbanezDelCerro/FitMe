package com.example.fitme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.fitme.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    onEditarPerfil: () -> Unit,
    onVerGraficaPeso: () -> Unit,
    onVerGraficaImc: () -> Unit
) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val vm: PerfilViewModel = viewModel()
    val registros by vm.registros.collectAsState()

    val imc = prefs.calcularImc()
    val categoriaImc = prefs.categoriaImc()
    val colorImc = when {
        imc <= 0f -> Color.Gray
        imc < 18.5f -> Color(0xFF2196F3)
        imc < 25f -> Color(0xFF00C853)
        imc < 30f -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    GymBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = strings.perfilTitle, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                LanguageToggleButton()
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = prefs.nombre.ifEmpty { strings.defaultUserLabel },
                        fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C853)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FilaDato(label = strings.emailLabel, valor = prefs.email.ifEmpty { "—" })
                    FilaDato(label = strings.ageLabel, valor = if (prefs.edad > 0) "${prefs.edad} ${strings.ageUnit}" else "—")
                    FilaDato(label = strings.genderLabel, valor = prefs.sexo.ifEmpty { "—" })
                    FilaDato(label = strings.goalLabel, valor = prefs.objetivo.ifEmpty { "—" })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = strings.physicalDataTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        DatoFisicoItem(valor = if (prefs.altura > 0) "${prefs.altura.toInt()} cm" else "—", etiqueta = strings.heightLabel)
                        DatoFisicoItem(valor = if (prefs.pesoActual > 0) "${"%.1f".format(prefs.pesoActual)} kg" else "—", etiqueta = strings.weightLabel)
                        DatoFisicoItem(valor = if (imc > 0) "${"%.1f".format(imc)}" else "—", etiqueta = strings.imcLabel, color = colorImc)
                    }
                    if (imc > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = categoriaImc, color = colorImc, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    if (registros.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "${strings.weightRecordsLabel} ${registros.size}", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onEditarPerfil,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.editPhysicalBtn, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onVerGraficaPeso,
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C853)),
                    border = BorderStroke(1.dp, Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(strings.pesoChartBtn, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = onVerGraficaImc,
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C853)),
                    border = BorderStroke(1.dp, Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(strings.imcChartBtn, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun FilaDato(label: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
        Text(text = valor, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DatoFisicoItem(valor: String, etiqueta: String, color: Color = Color(0xFF00C853)) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = etiqueta, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
    }
}
