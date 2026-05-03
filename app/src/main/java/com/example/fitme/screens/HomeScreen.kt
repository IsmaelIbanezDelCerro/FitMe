package com.example.fitme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.data.UserPreferences
import com.example.fitme.viewmodel.CheckRachaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    onIrPerfil: () -> Unit,
    onIrMenu: () -> Unit,
    onIrRutina: () -> Unit,
    onIrRacha: () -> Unit,
    onLogout: () -> Unit
) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val vm: CheckRachaViewModel = viewModel()
    val checks by vm.checks.collectAsState()
    val rachaActual = remember(checks) { vm.calcularRachaActual(checks) }

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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${strings.helloPrefix}, ${prefs.nombre.ifEmpty { strings.defaultAthleteLabel }} 👋",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = strings.homeMotivation,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LanguageToggleButton()
                    TextButton(onClick = onLogout) {
                        Text(strings.logoutBtn, color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (rachaActual > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00C853).copy(alpha = 0.2f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🔥", fontSize = 36.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "¡${strings.helloPrefix.replaceFirstChar { it.uppercase() }}, llevas $rachaActual ${if (rachaActual == 1) strings.streakBannerDay else strings.streakBannerDays} ${strings.streakBannerSuffix}",
                                color = Color(0xFF00C853),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(strings.streakBannerKeepGoing, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(strings.whatTodayTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAcceso(icono = "👤", titulo = strings.cardPerfilTitle, descripcion = strings.cardPerfilDesc, onClick = onIrPerfil, modifier = Modifier.weight(1f))
                    TarjetaAcceso(icono = "🥗", titulo = strings.cardMenuTitle, descripcion = strings.cardMenuDesc, onClick = onIrMenu, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAcceso(icono = "💪", titulo = strings.cardRutinaTitle, descripcion = strings.cardRutinaDesc, onClick = onIrRutina, modifier = Modifier.weight(1f))
                    TarjetaAcceso(icono = "🔥", titulo = strings.cardRachaTitle, descripcion = strings.cardRachaDesc, onClick = onIrRacha, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun TarjetaAcceso(icono: String, titulo: String, descripcion: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(120.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(icono, fontSize = 30.sp)
            Column {
                Text(titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(descripcion, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
            }
        }
    }
}
