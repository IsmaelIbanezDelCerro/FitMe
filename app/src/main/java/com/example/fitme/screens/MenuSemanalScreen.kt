package com.example.fitme.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitme.AppStrings
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.LocalIsSpanish
import com.example.fitme.data.UserPreferences
import com.example.fitme.data.api.DiaMenuDto
import com.example.fitme.viewmodel.MenuPersonalViewModel

data class ComidaDia(val nombre: String, val calorias: Int, val proteinas: Int)
data class DiaMenu(val dia: String, val desayuno: ComidaDia, val almuerzo: ComidaDia, val cena: ComidaDia)

fun diasSemana(isSpanish: Boolean) = if (isSpanish)
    listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
else
    listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

@Composable
fun MenuSemanalScreen(vm: MenuPersonalViewModel) {
    val strings = LocalAppStrings.current
    val isSpanish = LocalIsSpanish.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val diasMenu by vm.diasMenu.collectAsState()

    val menu = remember(diasMenu, prefs.objetivo, isSpanish) {
        val dias = diasSemana(isSpanish)
        val base = generarMenu(prefs.objetivo, isSpanish)
        if (diasMenu.isNotEmpty()) {
            dias.mapIndexed { idx, diaNombre ->
                val dto = diasMenu.firstOrNull { it.diaSemana == idx }
                val baseDia = base.find { it.dia == diaNombre }
                DiaMenu(
                    dia = diaNombre,
                    desayuno = if (dto?.desayuno != null)
                        ComidaDia(dto.desayuno, baseDia?.desayuno?.calorias ?: 0, baseDia?.desayuno?.proteinas ?: 0)
                    else (baseDia?.desayuno ?: ComidaDia("—", 0, 0)),
                    almuerzo = if (dto?.almuerzo != null)
                        ComidaDia(dto.almuerzo, baseDia?.almuerzo?.calorias ?: 0, baseDia?.almuerzo?.proteinas ?: 0)
                    else (baseDia?.almuerzo ?: ComidaDia("—", 0, 0)),
                    cena = if (dto?.cena != null)
                        ComidaDia(dto.cena, baseDia?.cena?.calorias ?: 0, baseDia?.cena?.proteinas ?: 0)
                    else (baseDia?.cena ?: ComidaDia("—", 0, 0))
                )
            }
        } else {
            base
        }
    }

    val tieneMenuPersonal = diasMenu.isNotEmpty()
    var mostrarDialogo by remember { mutableStateOf(false) }

    if (mostrarDialogo) {
        DialogAnadirAlimento(
            diasMenu = diasMenu,
            isSpanish = isSpanish,
            strings = strings,
            onGuardar = { dia -> vm.guardarDia(dia); mostrarDialogo = false },
            onDescartar = { mostrarDialogo = false }
        )
    }

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
                    LanguageToggleButton()
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(menu.size) { index ->
                TarjetaDia(menu[index], strings.breakfastLabel, strings.lunchLabel, strings.dinnerLabel, strings.proteinSuffix)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        FloatingActionButton(
            onClick = { mostrarDialogo = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 90.dp),
            containerColor = Color(0xFF00C853),
            contentColor = Color.Black
        ) {
            Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DialogAnadirAlimento(
    diasMenu: List<DiaMenuDto>,
    isSpanish: Boolean,
    strings: AppStrings,
    onGuardar: (DiaMenuDto) -> Unit,
    onDescartar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var diaIdx by remember { mutableStateOf(0) }
    var momento by remember { mutableStateOf("desayuno") }
    var kcal by remember { mutableStateOf("") }

    val dias = diasSemana(isSpanish)
    val momentos = listOf(
        "desayuno" to strings.breakfastShort,
        "almuerzo" to strings.lunchShort,
        "cena" to strings.dinnerShort
    )
    val campoColores = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF00C853),
        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color(0xFF00C853),
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent
    )

    AlertDialog(
        onDismissRequest = onDescartar,
        containerColor = Color(0xFF1E1E1E),
        title = {
            Text(strings.addFoodTitle, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(strings.foodFieldLabel, color = Color.White.copy(alpha = 0.7f)) },
                    colors = campoColores,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(strings.selectDayLabel, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    dias.forEachIndexed { idx, dia ->
                        FilterChip(
                            selected = diaIdx == idx,
                            onClick = { diaIdx = idx },
                            label = { Text(dia.take(3), fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF00C853),
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF2A2A2A),
                                labelColor = Color.White
                            )
                        )
                    }
                }

                Text(strings.selectMealLabel, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    momentos.forEach { (clave, etiqueta) ->
                        FilterChip(
                            selected = momento == clave,
                            onClick = { momento = clave },
                            label = { Text(etiqueta, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF00C853),
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF2A2A2A),
                                labelColor = Color.White
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = kcal,
                    onValueChange = { kcal = it },
                    label = { Text(strings.kcalFieldLabel, color = Color.White.copy(alpha = 0.7f)) },
                    colors = campoColores,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank()) {
                        val existing = diasMenu.firstOrNull { it.diaSemana == diaIdx }
                        val updated = when (momento) {
                            "desayuno" -> (existing ?: DiaMenuDto(diaSemana = diaIdx))
                                .copy(desayuno = nombre.trim(), kcalTotales = kcal.toIntOrNull())
                            "almuerzo" -> (existing ?: DiaMenuDto(diaSemana = diaIdx))
                                .copy(almuerzo = nombre.trim())
                            else -> (existing ?: DiaMenuDto(diaSemana = diaIdx))
                                .copy(cena = nombre.trim())
                        }
                        onGuardar(updated)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = RoundedCornerShape(8.dp)
            ) { Text(strings.saveBtn, fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDescartar) {
                Text(strings.cancelBtn, color = Color.White.copy(alpha = 0.6f))
            }
        }
    )
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

fun generarMenu(objetivo: String, isSpanish: Boolean = true): List<DiaMenu> {
    val esMuscular = objetivo.contains("Muscular", ignoreCase = true)
    val esGrasa = objetivo.contains("Grasa", ignoreCase = true) || objetivo.contains("Peso", ignoreCase = true)

    val dias = diasSemana(isSpanish)

    return if (isSpanish) listOf(
        DiaMenu(dias[0],
            desayuno = if (esMuscular) ComidaDia("Avena con plátano y proteína", 450, 35) else if (esGrasa) ComidaDia("Claras de huevo con espinacas", 220, 28) else ComidaDia("Tostada integral con aguacate", 320, 12),
            almuerzo = if (esMuscular) ComidaDia("Arroz con pechuga de pollo", 620, 50) else if (esGrasa) ComidaDia("Ensalada de atún con garbanzos", 380, 35) else ComidaDia("Pasta integral con verduras", 480, 20),
            cena = if (esMuscular) ComidaDia("Salmón con batata asada", 520, 40) else if (esGrasa) ComidaDia("Merluza al horno con brócoli", 300, 32) else ComidaDia("Lentejas con verduras", 400, 22)),
        DiaMenu(dias[1],
            desayuno = if (esMuscular) ComidaDia("Tortilla de 4 huevos con pan integral", 480, 38) else if (esGrasa) ComidaDia("Yogur natural con nueces", 200, 15) else ComidaDia("Muesli con leche desnatada", 300, 14),
            almuerzo = if (esMuscular) ComidaDia("Ternera con quinoa y espinacas", 650, 55) else if (esGrasa) ComidaDia("Pechuga a la plancha con ensalada", 350, 40) else ComidaDia("Pollo con arroz y verduras", 500, 35),
            cena = if (esMuscular) ComidaDia("Batido de proteína + plátano", 400, 45) else if (esGrasa) ComidaDia("Tofu salteado con espinacas", 280, 22) else ComidaDia("Salmón con ensalada verde", 380, 30)),
        DiaMenu(dias[2],
            desayuno = if (esMuscular) ComidaDia("Avena con frutos secos y miel", 500, 20) else if (esGrasa) ComidaDia("Kéfir con semillas de chía", 180, 12) else ComidaDia("Huevos revueltos con tostada", 350, 22),
            almuerzo = if (esMuscular) ComidaDia("Pasta con atún y tomate", 580, 45) else if (esGrasa) ComidaDia("Bacalao con pisto de verduras", 320, 30) else ComidaDia("Garbanzos con verduras salteadas", 420, 18),
            cena = if (esMuscular) ComidaDia("Pechuga a la plancha con arroz", 520, 48) else if (esGrasa) ComidaDia("Gambas al ajillo con ensalada", 260, 28) else ComidaDia("Tortilla francesa con ensalada", 300, 20)),
        DiaMenu(dias[3],
            desayuno = if (esMuscular) ComidaDia("Pan integral con mantequilla de cacahuete", 460, 22) else if (esGrasa) ComidaDia("Frutas del bosque con requesón", 190, 14) else ComidaDia("Batido de plátano y avena", 320, 10),
            almuerzo = if (esMuscular) ComidaDia("Arroz con pavo y brócoli", 600, 52) else if (esGrasa) ComidaDia("Ensalada de pollo con aguacate", 360, 38) else ComidaDia("Lentejas con arroz", 450, 20),
            cena = if (esMuscular) ComidaDia("Trucha al horno con patata", 490, 42) else if (esGrasa) ComidaDia("Sardinas con ensalada de tomate", 280, 25) else ComidaDia("Sopa de verduras con pollo", 320, 25)),
        DiaMenu(dias[4],
            desayuno = if (esMuscular) ComidaDia("Batido proteico de fresa", 380, 40) else if (esGrasa) ComidaDia("Huevo pochado con espinacas", 200, 18) else ComidaDia("Tostada con tomate y aceite de oliva", 280, 8),
            almuerzo = if (esMuscular) ComidaDia("Hamburguesa de ternera con arroz", 700, 58) else if (esGrasa) ComidaDia("Salmón con espárragos a la plancha", 340, 36) else ComidaDia("Pollo al curry con arroz", 520, 38),
            cena = if (esMuscular) ComidaDia("Caseína + almendras (antes de dormir)", 350, 30) else if (esGrasa) ComidaDia("Tortilla de claras con champiñones", 240, 26) else ComidaDia("Merluza con verduras al vapor", 300, 28)),
        DiaMenu(dias[5],
            desayuno = if (esMuscular) ComidaDia("Pancakes de avena con arándanos", 520, 28) else if (esGrasa) ComidaDia("Smoothie verde (espinacas + manzana)", 160, 6) else ComidaDia("Yogur con granola y frutas", 350, 12),
            almuerzo = if (esMuscular) ComidaDia("Paella de pollo y verduras", 680, 48) else if (esGrasa) ComidaDia("Ensalada mediterránea con atún", 320, 30) else ComidaDia("Arroz con verduras y huevo", 450, 18),
            cena = if (esMuscular) ComidaDia("Filete de ternera con quinoa", 560, 52) else if (esGrasa) ComidaDia("Boquerones al horno con ensalada", 270, 24) else ComidaDia("Revuelto de verduras con queso fresco", 310, 16)),
        DiaMenu(dias[6],
            desayuno = if (esMuscular) ComidaDia("Tortitas proteicas con plátano", 490, 36) else if (esGrasa) ComidaDia("Infusión + kéfir con semillas", 150, 10) else ComidaDia("Tostada integral con huevo y aguacate", 380, 18),
            almuerzo = if (esMuscular) ComidaDia("Pollo asado con batata y brócoli", 640, 55) else if (esGrasa) ComidaDia("Pavo al horno con verduras asadas", 360, 42) else ComidaDia("Cocido de legumbres", 480, 25),
            cena = if (esMuscular) ComidaDia("Salmón + arroz + aceite de oliva", 530, 44) else if (esGrasa) ComidaDia("Revuelto de claras con espárragos", 220, 24) else ComidaDia("Sopa minestrone con pan integral", 330, 14))
    ) else listOf(
        DiaMenu(dias[0],
            desayuno = if (esMuscular) ComidaDia("Oatmeal with banana and protein", 450, 35) else if (esGrasa) ComidaDia("Egg whites with spinach", 220, 28) else ComidaDia("Avocado toast", 320, 12),
            almuerzo = if (esMuscular) ComidaDia("Rice with chicken breast", 620, 50) else if (esGrasa) ComidaDia("Tuna and chickpea salad", 380, 35) else ComidaDia("Whole wheat pasta with vegetables", 480, 20),
            cena = if (esMuscular) ComidaDia("Salmon with roasted sweet potato", 520, 40) else if (esGrasa) ComidaDia("Baked hake with broccoli", 300, 32) else ComidaDia("Lentil stew", 400, 22)),
        DiaMenu(dias[1],
            desayuno = if (esMuscular) ComidaDia("4-egg omelette with whole wheat bread", 480, 38) else if (esGrasa) ComidaDia("Natural yogurt with walnuts", 200, 15) else ComidaDia("Muesli with skimmed milk", 300, 14),
            almuerzo = if (esMuscular) ComidaDia("Beef with quinoa and spinach", 650, 55) else if (esGrasa) ComidaDia("Grilled chicken with salad", 350, 40) else ComidaDia("Chicken with rice and vegetables", 500, 35),
            cena = if (esMuscular) ComidaDia("Protein shake + banana", 400, 45) else if (esGrasa) ComidaDia("Sautéed tofu with spinach", 280, 22) else ComidaDia("Salmon with green salad", 380, 30)),
        DiaMenu(dias[2],
            desayuno = if (esMuscular) ComidaDia("Oatmeal with nuts and honey", 500, 20) else if (esGrasa) ComidaDia("Kefir with chia seeds", 180, 12) else ComidaDia("Scrambled eggs on toast", 350, 22),
            almuerzo = if (esMuscular) ComidaDia("Pasta with tuna and tomato", 580, 45) else if (esGrasa) ComidaDia("Cod with ratatouille", 320, 30) else ComidaDia("Sautéed chickpeas with vegetables", 420, 18),
            cena = if (esMuscular) ComidaDia("Grilled chicken with rice", 520, 48) else if (esGrasa) ComidaDia("Prawns with salad", 260, 28) else ComidaDia("French omelette with salad", 300, 20)),
        DiaMenu(dias[3],
            desayuno = if (esMuscular) ComidaDia("Whole wheat bread with peanut butter", 460, 22) else if (esGrasa) ComidaDia("Forest berries with cottage cheese", 190, 14) else ComidaDia("Banana and oat smoothie", 320, 10),
            almuerzo = if (esMuscular) ComidaDia("Rice with turkey and broccoli", 600, 52) else if (esGrasa) ComidaDia("Chicken and avocado salad", 360, 38) else ComidaDia("Rice and lentils", 450, 20),
            cena = if (esMuscular) ComidaDia("Baked trout with potato", 490, 42) else if (esGrasa) ComidaDia("Sardines with tomato salad", 280, 25) else ComidaDia("Vegetable soup with chicken", 320, 25)),
        DiaMenu(dias[4],
            desayuno = if (esMuscular) ComidaDia("Strawberry protein shake", 380, 40) else if (esGrasa) ComidaDia("Poached egg with spinach", 200, 18) else ComidaDia("Toast with tomato and olive oil", 280, 8),
            almuerzo = if (esMuscular) ComidaDia("Beef burger with rice", 700, 58) else if (esGrasa) ComidaDia("Grilled salmon with asparagus", 340, 36) else ComidaDia("Chicken curry with rice", 520, 38),
            cena = if (esMuscular) ComidaDia("Casein + almonds (before bed)", 350, 30) else if (esGrasa) ComidaDia("Egg white omelette with mushrooms", 240, 26) else ComidaDia("Steamed hake with vegetables", 300, 28)),
        DiaMenu(dias[5],
            desayuno = if (esMuscular) ComidaDia("Oat pancakes with blueberries", 520, 28) else if (esGrasa) ComidaDia("Green smoothie (spinach + apple)", 160, 6) else ComidaDia("Yogurt with granola and fruit", 350, 12),
            almuerzo = if (esMuscular) ComidaDia("Chicken and vegetable paella", 680, 48) else if (esGrasa) ComidaDia("Mediterranean tuna salad", 320, 30) else ComidaDia("Rice with vegetables and egg", 450, 18),
            cena = if (esMuscular) ComidaDia("Beef steak with quinoa", 560, 52) else if (esGrasa) ComidaDia("Baked anchovies with salad", 270, 24) else ComidaDia("Vegetable scramble with fresh cheese", 310, 16)),
        DiaMenu(dias[6],
            desayuno = if (esMuscular) ComidaDia("Protein pancakes with banana", 490, 36) else if (esGrasa) ComidaDia("Infusion + kefir with seeds", 150, 10) else ComidaDia("Whole wheat toast with egg and avocado", 380, 18),
            almuerzo = if (esMuscular) ComidaDia("Roasted chicken with sweet potato and broccoli", 640, 55) else if (esGrasa) ComidaDia("Baked turkey with roasted vegetables", 360, 42) else ComidaDia("Legume stew", 480, 25),
            cena = if (esMuscular) ComidaDia("Salmon + rice + olive oil", 530, 44) else if (esGrasa) ComidaDia("Egg white scramble with asparagus", 220, 24) else ComidaDia("Minestrone soup with bread", 330, 14))
    )
}
