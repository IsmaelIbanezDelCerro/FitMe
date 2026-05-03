package com.example.fitme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
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
import com.example.fitme.data.entity.EntrenamientoHistorial
import com.example.fitme.viewmodel.EntrenamientoViewModel

@Composable
fun HistorialScreen(onVolver: () -> Unit) {
    val strings = LocalAppStrings.current
    val vm: EntrenamientoViewModel = viewModel()
    val historial by vm.historial.collectAsState()

    GymBackground {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(strings.historialTitle, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("${historial.size} ${strings.sessionsCompletedLabel}", color = Color(0xFF00C853), fontSize = 14.sp)
                }
                LanguageToggleButton()
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (historial.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💪", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(strings.noTrainingYetMsg, color = Color.White.copy(alpha = 0.6f), fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(historial) { sesion ->
                        TarjetaHistorial(sesion)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) { Text(strings.backBtn) }
        }
    }
}

@Composable
private fun TarjetaHistorial(sesion: EntrenamientoHistorial) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).background(Color(0xFF00C853).copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) { Text("💪", fontSize = 22.sp) }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(sesion.nombreRutina, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(sesion.fecha, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${sesion.duracionMinutos}", color = Color(0xFF00C853), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("min", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
            }
        }
    }
}
