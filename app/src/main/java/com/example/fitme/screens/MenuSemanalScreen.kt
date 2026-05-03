package com.example.fitme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.fitme.viewmodel.MenuPersonalViewModel

data class ComidaDia(val nombre: String, val calorias: Int, val proteinas: Int)
data class DiaMenu(val dia: String, val desayuno: ComidaDia, val almuerzo: ComidaDia, val cena: ComidaDia)

private val DIAS_SEMANA = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

@Composable
fun MenuSemanalScreen(onEditarMenu: () -> Unit) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val vm: MenuPersonalViewModel = viewModel()
    val comidasPersonales by vm.comidasPersonales.collectAsState()

    val menu = remember(comidasPersonales, prefs.objetivo) {
        if (comidasPersonales.isNotEmpty()) {
            val base = generarMenu(prefs.objetivo)
            DIAS_SEMANA.map { dia ->
                val base_dia = base.find { it.dia == dia }
                fun comidaParaMomento(momento: String, baseComida: ComidaDia): ComidaDia {
                    val personal = comidasPersonales.find { it.dia == dia && it.momento == momento }
                    return if (personal != null) ComidaDia(personal.nombre, personal.calorias, personal.proteinas)
                    else baseComida
                }
                DiaMenu(
                    dia = dia,
                    desayuno = comidaParaMomento("desayuno", base_dia?.desayuno ?: ComidaDia("Sin configurar", 0, 0)),
                    almuerzo = comidaParaMomento("almuerzo", base_dia?.almuerzo ?: ComidaDia("Sin configurar", 0, 0)),
                    cena = comidaParaMomento("cena", base_dia?.cena ?: ComidaDia("Sin configurar", 0, 0))
                )
            }
        } else {
            generarMenu(prefs.objetivo)
        }
    }

    val tieneMenuPersonal = comidasPersonales.isNotEmpty()

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
                        Text(text = strings.menuSemanalTitle, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            text = if (tieneMenuPersonal) strings.personalMenuActiveLabel
                                   else "${strings.goalPrefix} ${prefs.objetivo.ifEmpty { strings.generalGoalLabel }}",
                            color = Color(0xFF00C853), fontSize = 13.sp, fontWeight = FontWeight.Medium
                        )
                    }
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        LanguageToggleButton()
                        OutlinedButton(
                            onClick = onEditarMenu,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00C853)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00C853)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(strings.editBtnLabel, fontSize = 12.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(menu.size) { index ->
                TarjetaDia(menu[index], strings.breakfastLabel, strings.lunchLabel, strings.dinnerLabel, strings.proteinSuffix)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun TarjetaDia(dia: DiaMenu, breakfastLabel: String, lunchLabel: String, dinnerLabel: String, proteinSuffix: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A).copy(alpha = 0.92f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = dia.dia, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00C853))
            Spacer(modifier = Modifier.height(12.dp))
            FilaComida(breakfastLabel, dia.desayuno, proteinSuffix)
            Spacer(modifier = Modifier.height(8.dp))
            FilaComida(lunchLabel, dia.almuerzo, proteinSuffix)
            Spacer(modifier = Modifier.height(8.dp))
            FilaComida(dinnerLabel, dia.cena, proteinSuffix)
        }
    }
}

@Composable
private fun FilaComida(momento: String, comida: ComidaDia, proteinSuffix: String) {
    Column {
        Text(text = momento, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = comida.nombre, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            if (comida.calorias > 0) {
                Text(text = "${comida.calorias} kcal · ${comida.proteinas}g $proteinSuffix", color = Color(0xFF00C853), fontSize = 12.sp)
            }
        }
    }
}

fun generarMenu(objetivo: String): List<DiaMenu> {
    val esMuscular = objetivo.contains("Muscular", ignoreCase = true)
    val esGrasa = objetivo.contains("Grasa", ignoreCase = true) || objetivo.contains("Peso", ignoreCase = true)

    return listOf(
        DiaMenu("Lunes",
            desayuno = if (esMuscular) ComidaDia("Avena con plátano y proteína", 450, 35) else if (esGrasa) ComidaDia("Claras de huevo con espinacas", 220, 28) else ComidaDia("Tostada integral con aguacate", 320, 12),
            almuerzo = if (esMuscular) ComidaDia("Arroz con pechuga de pollo", 620, 50) else if (esGrasa) ComidaDia("Ensalada de atún con garbanzos", 380, 35) else ComidaDia("Pasta integral con verduras", 480, 20),
            cena = if (esMuscular) ComidaDia("Salmón con batata asada", 520, 40) else if (esGrasa) ComidaDia("Merluza al horno con brócoli", 300, 32) else ComidaDia("Lentejas con verduras", 400, 22)),
        DiaMenu("Martes",
            desayuno = if (esMuscular) ComidaDia("Tortilla de 4 huevos con pan integral", 480, 38) else if (esGrasa) ComidaDia("Yogur natural con nueces", 200, 15) else ComidaDia("Muesli con leche desnatada", 300, 14),
            almuerzo = if (esMuscular) ComidaDia("Ternera con quinoa y espinacas", 650, 55) else if (esGrasa) ComidaDia("Pechuga a la plancha con ensalada", 350, 40) else ComidaDia("Pollo con arroz y verduras", 500, 35),
            cena = if (esMuscular) ComidaDia("Batido de proteína + plátano", 400, 45) else if (esGrasa) ComidaDia("Tofu salteado con espinacas", 280, 22) else ComidaDia("Salmón con ensalada verde", 380, 30)),
        DiaMenu("Miércoles",
            desayuno = if (esMuscular) ComidaDia("Avena con frutos secos y miel", 500, 20) else if (esGrasa) ComidaDia("Kéfir con semillas de chía", 180, 12) else ComidaDia("Huevos revueltos con tostada", 350, 22),
            almuerzo = if (esMuscular) ComidaDia("Pasta con atún y tomate", 580, 45) else if (esGrasa) ComidaDia("Bacalao con pisto de verduras", 320, 30) else ComidaDia("Garbanzos con verduras salteadas", 420, 18),
            cena = if (esMuscular) ComidaDia("Pechuga a la plancha con arroz", 520, 48) else if (esGrasa) ComidaDia("Gambas al ajillo con ensalada", 260, 28) else ComidaDia("Tortilla francesa con ensalada", 300, 20)),
        DiaMenu("Jueves",
            desayuno = if (esMuscular) ComidaDia("Pan integral con mantequilla de cacahuete", 460, 22) else if (esGrasa) ComidaDia("Frutas del bosque con requesón", 190, 14) else ComidaDia("Batido de plátano y avena", 320, 10),
            almuerzo = if (esMuscular) ComidaDia("Arroz con pavo y brócoli", 600, 52) else if (esGrasa) ComidaDia("Ensalada de pollo con aguacate", 360, 38) else ComidaDia("Lentejas con arroz", 450, 20),
            cena = if (esMuscular) ComidaDia("Trucha al horno con patata", 490, 42) else if (esGrasa) ComidaDia("Sardinas con ensalada de tomate", 280, 25) else ComidaDia("Sopa de verduras con pollo", 320, 25)),
        DiaMenu("Viernes",
            desayuno = if (esMuscular) ComidaDia("Batido proteico de fresa", 380, 40) else if (esGrasa) ComidaDia("Huevo pochado con espinacas", 200, 18) else ComidaDia("Tostada con tomate y aceite de oliva", 280, 8),
            almuerzo = if (esMuscular) ComidaDia("Hamburguesa de ternera con arroz", 700, 58) else if (esGrasa) ComidaDia("Salmón con espárragos a la plancha", 340, 36) else ComidaDia("Pollo al curry con arroz", 520, 38),
            cena = if (esMuscular) ComidaDia("Caseína + almendras (antes de dormir)", 350, 30) else if (esGrasa) ComidaDia("Tortilla de claras con champiñones", 240, 26) else ComidaDia("Merluza con verduras al vapor", 300, 28)),
        DiaMenu("Sábado",
            desayuno = if (esMuscular) ComidaDia("Pancakes de avena con arándanos", 520, 28) else if (esGrasa) ComidaDia("Smoothie verde (espinacas + manzana)", 160, 6) else ComidaDia("Yogur con granola y frutas", 350, 12),
            almuerzo = if (esMuscular) ComidaDia("Paella de pollo y verduras", 680, 48) else if (esGrasa) ComidaDia("Ensalada mediterránea con atún", 320, 30) else ComidaDia("Arroz con verduras y huevo", 450, 18),
            cena = if (esMuscular) ComidaDia("Filete de ternera con quinoa", 560, 52) else if (esGrasa) ComidaDia("Boquerones al horno con ensalada", 270, 24) else ComidaDia("Revuelto de verduras con queso fresco", 310, 16)),
        DiaMenu("Domingo",
            desayuno = if (esMuscular) ComidaDia("Tortitas proteicas con plátano", 490, 36) else if (esGrasa) ComidaDia("Infusión + kéfir con semillas", 150, 10) else ComidaDia("Tostada integral con huevo y aguacate", 380, 18),
            almuerzo = if (esMuscular) ComidaDia("Pollo asado con batata y brócoli", 640, 55) else if (esGrasa) ComidaDia("Pavo al horno con verduras asadas", 360, 42) else ComidaDia("Cocido de legumbres", 480, 25),
            cena = if (esMuscular) ComidaDia("Salmón + arroz + aceite de oliva", 530, 44) else if (esGrasa) ComidaDia("Revuelto de claras con espárragos", 220, 24) else ComidaDia("Sopa minestrone con pan integral", 330, 14))
    )
}
