package com.example.fitme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.LocalIsSpanish
import com.example.fitme.LocalOnToggleLanguage
import com.example.fitme.R
import com.example.fitme.loadStrings
import com.example.fitme.ui.theme.FitMeTheme


data class Comida(
    val nombreEs: String, // Cambiado para tener ambos
    val nombreEn: String, // nombres disponibles
    val iconos: List<String>,
    val seleccionada: Boolean = false
)

@Composable
fun InteresesScreen(navController: NavController) {
    val strings = LocalAppStrings.current

    val comidas = remember {
        mutableStateListOf(
            // 🥩 CARNES
            Comida("Pollo", "Chicken", listOf("🍗")),
            Comida("Ternera", "Beef", listOf("🥩")),
            Comida("Pavo", "Turkey", listOf("🍗")),
            Comida("Conejo", "Rabbit", listOf("🍖")),
            Comida("Cerdo", "Pork", listOf()),
            Comida("Cordero", "Lamb", listOf()),
            Comida("Jamón", "Ham", listOf("🍖")),
            Comida("Pechuga de pollo", "Chicken breast", listOf()),

            // 🐟 PESCADO Y MARISCO
            Comida("Salmón", "Salmon", listOf("🐟")),
            Comida("Atún", "Tuna", listOf("🐟")),
            Comida("Gambas", "Prawns", listOf("🦐")),
            Comida("Merluza", "Hake", listOf("🐟")),
            Comida("Bacalao", "Cod", listOf("🐟")),
            Comida("Sardinas", "Sardines", listOf()),
            Comida("Trucha", "Trout", listOf("🐟")),
            Comida("Boquerones", "Anchovies", listOf()),

            // 🥚 HUEVOS
            Comida("Huevos", "Eggs", listOf("🥚")),

            // 🥛 LÁCTEOS
            Comida("Yogur", "Yogurt", listOf("🥛")),
            Comida("Queso", "Cheese", listOf("🧀")),
            Comida("Proteína", "Protein", listOf()),
            Comida("Leche", "Milk", listOf("🥛")),
            Comida("Kéfir", "Kefir", listOf("🥛")),
            Comida("Requesón", "Cottage cheese", listOf()),
            Comida("Cuajada", "Curd", listOf("🥛")),
            Comida("Leche desnatada", "Skimmed milk", listOf()),

            // 🌱 VEGETAL
            Comida("Tofu", "Tofu", listOf("🌱")),
            Comida("Lentejas", "Lentils", listOf("🌱")),
            Comida("Garbanzos", "Chickpeas", listOf("🌱")),
            Comida("Quinoa", "Quinoa", listOf("🌱")),
            Comida("Soja", "Soy", listOf("🌱")),
            Comida("Alubias", "Beans", listOf()),
            Comida("Espinacas", "Spinach", listOf()),
            Comida("Brócoli", "Broccoli", listOf("🌱")),

            // 🥜 FRUTOS SECOS
            Comida("Cacahuete", "Peanut", listOf()),
            Comida("Almendras", "Almonds", listOf()),
            Comida("Nueces", "Walnuts", listOf("🌰")),
            Comida("Anacardos", "Cashews", listOf()),
            Comida("Pistachos", "Pistachios", listOf()),
            Comida("Avellanas", "Hazelnuts", listOf()),
            Comida("Piñones", "Pine nuts", listOf("🌰")),
            Comida("Macadamia", "Macadamia", listOf()),

            // 🌾 GLUTEN
            Comida("Avena", "Oats", listOf("🌾")),
            Comida("Pan integral", "Wholemeal bread", listOf()),
            Comida("Pasta", "Pasta", listOf("🍝")),
            Comida("Cebada", "Barley", listOf("🌾")),
            Comida("Centeno", "Rye", listOf()),
            Comida("Cuscús", "Couscous", listOf()),
            Comida("Harina", "Flour", listOf("🌾")),
            Comida("Pan blanco", "White bread", listOf()),

            // 🧂 OTROS
            Comida("Mostaza", "Mustard", listOf("🌭")),
            Comida("Apio", "Celery", listOf("🥬")),
            Comida("Soja (salsa)", "Soy sauce", listOf()),
            Comida("Vinagre", "Vinegar", listOf()),
            Comida("Picante", "Spicy sauce", listOf()),
            Comida("Mayonesa", "Mayonnaise", listOf()),
            Comida("Ketchup", "Ketchup", listOf()),

            // 💪 FITNESS EXTRA
            Comida("Claras de huevo", "Egg whites", listOf()),
            Comida("Batido", "Shake", listOf("🥤")),
            Comida("Arroz", "Rice", listOf("🍚")),
            Comida("Patata", "Potato", listOf("🥔")),
            Comida("Batata", "Sweet potato", listOf("🍠")),
            Comida("Avena fitness", "Fitness oats", listOf()),
            Comida("Plátano", "Banana", listOf("🍌")),
            Comida("Creatina", "Creatine", listOf())
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

            // Recuperamos si está activo el español
            val isSpanish = LocalIsSpanish.current

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
                        isSpanish = isSpanish, // <-- Pasamos el estado del idioma aquí
                        onClick = { comidas[index] = comida.copy(seleccionada = !comida.seleccionada) }
                    )
                }
            }

            val selectedCount = comidas.count { it.seleccionada }

            if (selectedCount < 10) {
                Text(
                    strings.minFoodsMsg,
                    color = Color(0xFFFF5252),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    navController.navigate("app") {
                        popUpTo("intereses") { inclusive = true }
                    }
                },
                enabled = selectedCount >= 10,
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
fun ItemComida(comida: Comida, isSpanish: Boolean, onClick: () -> Unit) { // <-- Añadido isSpanish
    val isSelected = comida.seleccionada

    Card(
        modifier = Modifier.wrapContentSize().clickable { onClick() },
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF00C853) else Color.DarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Elegimos el nombre correcto según el idioma activo
            val nombreMostrar = if (isSpanish) comida.nombreEs else comida.nombreEn
            val tamannoFuente = if (nombreMostrar.length > 10) 10.sp else 12.sp

            comida.iconos.forEach { Text(text = it, fontSize = 16.sp) }

            Spacer(modifier = Modifier.width(6.dp))
            Text(text = nombreMostrar, color = Color.White, fontWeight = FontWeight.Medium, fontSize = tamannoFuente, maxLines = 1)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FitMeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.gym_bg),
                    contentScale = ContentScale.Crop
                ),
            color = Color.Transparent
        ) {

            var isSpanish by rememberSaveable { mutableStateOf(true) }
            val context = LocalContext.current
            val strings = remember(isSpanish) { loadStrings(context, isSpanish) }
            val onToggle: () -> Unit = { isSpanish = !isSpanish }

            CompositionLocalProvider(
                LocalAppStrings provides strings,
                LocalIsSpanish provides isSpanish,
                LocalOnToggleLanguage provides onToggle
            ) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "intereses") {
                    composable("intereses") {
                        InteresesScreen(navController = navController)
                    }

                }
            }

        }
    }
}