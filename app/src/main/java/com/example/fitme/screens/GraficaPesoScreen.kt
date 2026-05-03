package com.example.fitme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.data.entity.RegistroPeso
import com.example.fitme.viewmodel.PerfilViewModel

@Composable
fun GraficaPesoScreen(onVolver: () -> Unit) {
    val strings = LocalAppStrings.current
    val vm: PerfilViewModel = viewModel()
    val registros by vm.registros.collectAsState()
    val datos = registros.sortedBy { it.fecha }

    GymBackground {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(strings.weightEvolutionTitle, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                LanguageToggleButton()
            }
            Spacer(modifier = Modifier.height(20.dp))

            if (datos.size < 2) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(220.dp).background(Color(0xFF1A1A1A).copy(alpha = 0.92f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (datos.isEmpty()) strings.noRecordsYetMsg else strings.need2RecordsMsg,
                        color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp, textAlign = TextAlign.Center
                    )
                }
            } else {
                LineaGrafica(puntos = datos.map { it.peso }, color = Color(0xFF00C853), etiquetaY = "kg")
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(strings.historyLabel, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            if (registros.isEmpty()) {
                Text(strings.noRecordsMsg, color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(registros) { r -> FilaRegistroPeso(r) }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) { Text(strings.backBtn) }
        }
    }
}

@Composable
fun GraficaImcScreen(onVolver: () -> Unit) {
    val strings = LocalAppStrings.current
    val vm: PerfilViewModel = viewModel()
    val registros by vm.registros.collectAsState()
    val datos = registros.sortedBy { it.fecha }

    GymBackground {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(strings.imcEvolutionTitle, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                LanguageToggleButton()
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LeyendaImc("< 18.5", strings.underweightLabel, Color(0xFF2196F3))
                LeyendaImc("18.5–25", strings.normalLabel, Color(0xFF00C853))
                LeyendaImc("25–30", strings.overweightLabel, Color(0xFFFFC107))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (datos.size < 2) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(220.dp).background(Color(0xFF1A1A1A).copy(alpha = 0.92f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (datos.isEmpty()) strings.imcNoRecords else strings.imcNeed2,
                        color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp, textAlign = TextAlign.Center
                    )
                }
            } else {
                LineaGrafica(puntos = datos.map { it.imc }, color = Color(0xFFFFC107), etiquetaY = strings.imcLabel)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(strings.historyImcLabel, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            if (registros.isEmpty()) {
                Text(strings.noRecordsMsg, color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(registros) { r -> FilaRegistroImc(r) }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) { Text(strings.backBtn) }
        }
    }
}

@Composable
private fun LineaGrafica(puntos: List<Float>, color: Color, etiquetaY: String) {
    val minVal = puntos.min()
    val maxVal = puntos.max()
    val rango = if (maxVal - minVal < 0.01f) 1f else maxVal - minVal

    Box(
        modifier = Modifier.fillMaxWidth().height(200.dp).background(Color(0xFF1A1A1A).copy(alpha = 0.92f), RoundedCornerShape(16.dp)).padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val anchoDisp = size.width
            val altoDisp = size.height
            val paso = if (puntos.size > 1) anchoDisp / (puntos.size - 1) else anchoDisp
            val lineas = 4
            for (i in 0..lineas) {
                val y = altoDisp * i / lineas
                drawLine(Color.White.copy(alpha = 0.1f), Offset(0f, y), Offset(anchoDisp, y), strokeWidth = 1f)
            }
            val path = Path()
            puntos.forEachIndexed { index, valor ->
                val x = index * paso
                val y = altoDisp - ((valor - minVal) / rango) * altoDisp
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, color, style = Stroke(width = 3f))
            puntos.forEachIndexed { index, valor ->
                val x = index * paso
                val y = altoDisp - ((valor - minVal) / rango) * altoDisp
                drawCircle(color, radius = 6f, center = Offset(x, y))
                drawCircle(Color.Black, radius = 3f, center = Offset(x, y))
            }
        }
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            Text("${"%.1f".format(maxVal)} $etiquetaY", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
            Text("${"%.1f".format(minVal)} $etiquetaY", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
        }
    }
}

@Composable
private fun FilaRegistroPeso(registro: RegistroPeso) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(registro.fecha, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
        Text("${"%.1f".format(registro.peso)} kg", color = Color(0xFF00C853), fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
}

@Composable
private fun FilaRegistroImc(registro: RegistroPeso) {
    val color = when {
        registro.imc < 18.5f -> Color(0xFF2196F3)
        registro.imc < 25f -> Color(0xFF00C853)
        registro.imc < 30f -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(registro.fecha, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
        Text("${"%.2f".format(registro.imc)}", color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
}

@Composable
private fun LeyendaImc(rango: String, nombre: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Text("$rango $nombre", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
    }
}
