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
import com.example.fitme.LocalIsSpanish
import com.example.fitme.R


data class Comida(
    val nombre: String,
    val iconos: List<String>,
    val seleccionada: Boolean = false
)

private fun buildComidas(isSpanish: Boolean) = mutableStateListOf(
    // Carnes / Meats
    Comida(if (isSpanish) "Pollo" else "Chicken", listOf("🍗")),
    Comida(if (isSpanish) "Ternera" else "Beef", listOf("🥩")),
    Comida(if (isSpanish) "Pavo" else "Turkey", listOf("🍗")),
    Comida(if (isSpanish) "Conejo" else "Rabbit", listOf("🍖")),
    Comida(if (isSpanish) "Cerdo" else "Pork", listOf()),
    Comida(if (isSpanish) "Cordero" else "Lamb", listOf()),
    Comida(if (isSpanish) "Jamón" else "Ham", listOf("🍖")),
    Comida(if (isSpanish) "Pechuga de pollo" else "Chicken Breast", listOf()),
    // Pescado / Fish
    Comida(if (isSpanish) "Salmón" else "Salmon", listOf("🐟")),
    Comida(if (isSpanish) "Atún" else "Tuna", listOf("🐟")),
    Comida(if (isSpanish) "Gambas" else "Prawns", listOf("🦐")),
    Comida(if (isSpanish) "Merluza" else "Hake", listOf("🐟")),
    Comida(if (isSpanish) "Bacalao" else "Cod", listOf("🐟")),
    Comida(if (isSpanish) "Sardinas" else "Sardines", listOf()),
    Comida(if (isSpanish) "Trucha" else "Trout", listOf("🐟")),
    Comida(if (isSpanish) "Boquerones" else "Anchovies", listOf()),
    // Huevos / Eggs
    Comida(if (isSpanish) "Huevos" else "Eggs", listOf("🥚")),
    // Lácteos / Dairy
    Comida(if (isSpanish) "Yogur" else "Yogurt", listOf("🥛")),
    Comida(if (isSpanish) "Queso" else "Cheese", listOf("🧀")),
    Comida(if (isSpanish) "Proteína" else "Protein", listOf()),
    Comida(if (isSpanish) "Leche" else "Milk", listOf("🥛")),
    Comida(if (isSpanish) "Kéfir" else "Kefir", listOf("🥛")),
    Comida(if (isSpanish) "Requesón" else "Cottage Cheese", listOf()),
    Comida(if (isSpanish) "Cuajada" else "Curd", listOf("🥛")),
    Comida(if (isSpanish) "Mantequilla" else "Butter", listOf("🧈")),
    Comida(if (isSpanish) "Nata" else "Cream", listOf()),
    // Vegetal / Plant
    Comida(if (isSpanish) "Tofu" else "Tofu", listOf("🌱")),
    Comida(if (isSpanish) "Lentejas" else "Lentils", listOf("🌱")),
    Comida(if (isSpanish) "Garbanzos" else "Chickpeas", listOf("🌱")),
    Comida(if (isSpanish) "Quinoa" else "Quinoa", listOf("🌱")),
    Comida(if (isSpanish) "Soja" else "Soy", listOf("🌱")),
    Comida(if (isSpanish) "Alubias" else "Beans", listOf()),
    Comida(if (isSpanish) "Espinacas" else "Spinach", listOf()),
    Comida(if (isSpanish) "Brócoli" else "Broccoli", listOf("🌱")),
    Comida(if (isSpanish) "Aguacate" else "Avocado", listOf("🥑")),
    Comida(if (isSpanish) "Zanahoria" else "Carrot", listOf("🥕")),
    Comida(if (isSpanish) "Pimiento" else "Bell Pepper", listOf("🫑")),
    Comida(if (isSpanish) "Pepino" else "Cucumber", listOf("🥒")),
    // Frutos secos / Nuts
    Comida(if (isSpanish) "Cacahuete" else "Peanut", listOf()),
    Comida(if (isSpanish) "Almendras" else "Almonds", listOf()),
    Comida(if (isSpanish) "Nueces" else "Walnuts", listOf("🌰")),
    Comida(if (isSpanish) "Anacardos" else "Cashews", listOf()),
    Comida(if (isSpanish) "Pistachos" else "Pistachios", listOf()),
    Comida(if (isSpanish) "Avellanas" else "Hazelnuts", listOf()),
    Comida(if (isSpanish) "Piñones" else "Pine Nuts", listOf("🌰")),
    Comida(if (isSpanish) "Macadamia" else "Macadamia", listOf()),
    // Gluten / Grains
    Comida(if (isSpanish) "Avena" else "Oats", listOf("🌾")),
    Comida(if (isSpanish) "Pan integral" else "Whole Grain Bread", listOf()),
    Comida(if (isSpanish) "Pasta" else "Pasta", listOf("🍝")),
    Comida(if (isSpanish) "Cebada" else "Barley", listOf("🌾")),
    Comida(if (isSpanish) "Centeno" else "Rye", listOf()),
    Comida(if (isSpanish) "Cuscús" else "Couscous", listOf()),
    Comida(if (isSpanish) "Harina" else "Flour", listOf("🌾")),
    Comida(if (isSpanish) "Pan blanco" else "White Bread", listOf()),
    // Otros / Other
    Comida(if (isSpanish) "Mostaza" else "Mustard", listOf("🌭")),
    Comida(if (isSpanish) "Apio" else "Celery", listOf("🥬")),
    Comida(if (isSpanish) "Soja (salsa)" else "Soy Sauce", listOf()),
    Comida(if (isSpanish) "Vinagre" else "Vinegar", listOf()),
    Comida(if (isSpanish) "Picante" else "Spicy", listOf()),
    Comida(if (isSpanish) "Mayonesa" else "Mayonnaise", listOf()),
    Comida("Ketchup", listOf()),
    // Fitness extra
    Comida(if (isSpanish) "Claras de huevo" else "Egg Whites", listOf()),
    Comida(if (isSpanish) "Batido" else "Shake", listOf("🥤")),
    Comida(if (isSpanish) "Arroz" else "Rice", listOf("🍚")),
    Comida(if (isSpanish) "Patata" else "Potato", listOf("🥔")),
    Comida(if (isSpanish) "Batata" else "Sweet Potato", listOf("🍠")),
    Comida(if (isSpanish) "Avena fitness" else "Fitness Oats", listOf()),
    Comida(if (isSpanish) "Plátano" else "Banana", listOf("🍌")),
    Comida(if (isSpanish) "Creatina" else "Creatine", listOf()),
    Comida(if (isSpanish) "Manzana" else "Apple", listOf("🍎")),
    Comida(if (isSpanish) "Naranja" else "Orange", listOf("🍊")),
    Comida(if (isSpanish) "Fresas" else "Strawberries", listOf("🍓")),
    Comida(if (isSpanish) "Arándanos" else "Blueberries", listOf("🫐")),
    Comida(if (isSpanish) "Aceite de oliva" else "Olive Oil", listOf())
)

@Composable
fun InteresesScreen(navController: NavController) {
    val strings = LocalAppStrings.current
    val isSpanish = LocalIsSpanish.current

    val comidas = remember(isSpanish) { buildComidas(isSpanish) }

    var errorMsg by remember { mutableStateOf("") }

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

            if (errorMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMsg, color = Color(0xFFFF5252), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (comidas.count { it.seleccionada } < 10) {
                        errorMsg = strings.minFoodsMsg
                    } else {
                        navController.navigate("app") {
                            popUpTo("intereses") { inclusive = true }
                        }
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
