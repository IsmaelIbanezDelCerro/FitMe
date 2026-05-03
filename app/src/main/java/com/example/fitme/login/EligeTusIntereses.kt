package com.example.fitme.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.R


data class Comida(
    val nombre: String,
    val iconos: List<String>,
    val seleccionada: Boolean = false
)

@Composable
fun InteresesScreen(navController: NavController) {
    val strings = LocalAppStrings.current

    val comidas = remember {
        mutableStateListOf(
            // 🥩 CARNES
            Comida("Pollo", listOf("🍗")),
            Comida("Ternera", listOf("🥩")),
            Comida("Pavo", listOf("🍗")),
            Comida("Conejo", listOf("🍖")),
            Comida("Cerdo", listOf()),
            Comida("Cordero", listOf()),
            Comida("Jamón", listOf("🍖")),
            Comida("Pechuga de pollo", listOf()),

            // 🐟 PESCADO Y MARISCO
            Comida("Salmón", listOf("🐟")),
            Comida("Atún", listOf("🐟")),
            Comida("Gambas", listOf("🦐")),
            Comida("Merluza", listOf("🐟")),
            Comida("Bacalao", listOf("🐟")),
            Comida("Sardinas", listOf()),
            Comida("Trucha", listOf("🐟")),
            Comida("Boquerones", listOf()),

            // 🥚 HUEVOS
            Comida("Huevos", listOf("🥚")),

            // 🥛 LÁCTEOS
            Comida("Yogur", listOf("🥛")),
            Comida("Queso", listOf("🧀")),
            Comida("Proteína", listOf()),
            Comida("Leche", listOf("🥛")),
            Comida("Kéfir", listOf("🥛")),
            Comida("Requesón", listOf()),
            Comida("Cuajada", listOf("🥛")),
            Comida("Leche desnatada", listOf()),

            // 🌱 VEGETAL / VEGANO
            Comida("Tofu", listOf("🌱")),
            Comida("Lentejas", listOf("🌱")),
            Comida("Garbanzos", listOf("🌱")),
            Comida("Quinoa", listOf("🌱")),
            Comida("Soja", listOf("🌱")),
            Comida("Alubias", listOf()),
            Comida("Espinacas", listOf()),
            Comida("Brócoli", listOf("🌱")),

            // 🥜 FRUTOS SECOS
            Comida("Cacahuete", listOf()),
            Comida("Almendras", listOf()),
            Comida("Nueces", listOf("🌰")),
            Comida("Anacardos", listOf()),
            Comida("Pistachos", listOf()),
            Comida("Avellanas", listOf()),
            Comida("Piñones", listOf("🌰")),
            Comida("Macadamia", listOf()),

            // 🌾 GLUTEN
            Comida("Avena", listOf("🌾")),
            Comida("Pan integral", listOf()),
            Comida("Pasta", listOf("🍝")),
            Comida("Cebada", listOf("🌾")),
            Comida("Centeno", listOf()),
            Comida("Cuscús", listOf()),
            Comida("Harina", listOf("🌾")),
            Comida("Pan blanco", listOf()),

            // 🧂 OTROS
            Comida("Mostaza", listOf("🌭")),
            Comida("Apio", listOf("🥬")),
            Comida("Soja (salsa)", listOf()),
            Comida("Vinagre", listOf()),
            Comida("Picante", listOf()),
            Comida("Mayonesa", listOf()),
            Comida("Ketchup", listOf()),

            // 💪 FITNESS EXTRA
            Comida("Claras de huevo", listOf()),
            Comida("Batido", listOf("🥤")),
            Comida("Arroz", listOf("🍚")),
            Comida("Patata", listOf("🥔")),
            Comida("Batata", listOf("🍠")),
            Comida("Avena fitness", listOf()),
            Comida("Plátano", listOf("🍌")),
            Comida("Creatina", listOf())
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.gym_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)))

        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(strings.chooseFoodsTitle, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(strings.selectPreferencesHint, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                }
                LanguageToggleButton()
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(comidas.size) { index ->
                    val comida = comidas[index]
                    ItemComida(
                        comida = comida,
                        onClick = { comidas[index] = comida.copy(seleccionada = !comida.seleccionada) }
                    )
                }
            }

            Button(
                onClick = {
                    navController.navigate("app") {
                        popUpTo("intereses") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.continueBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ItemComida(comida: Comida, onClick: () -> Unit) {
    val isSelected = comida.seleccionada

    Card(
        modifier = Modifier.wrapContentSize().clickable { onClick() },
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF00C853) else Color.DarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                comida.iconos.forEach { Text(text = it, fontSize = 16.sp) }
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = comida.nombre, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 13.sp, maxLines = 1)
        }
    }
}
