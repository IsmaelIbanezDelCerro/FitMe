package com.example.fitme.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitme.AppStrings
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.LocalIsSpanish
import com.example.fitme.LocalOnToggleLanguage
import com.example.fitme.loadStrings
import com.example.fitme.screens.*
import com.example.fitme.ui.theme.FitMeTheme
import com.example.fitme.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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
                        NavHost(navController = navController, startDestination = "login") {
                            composable("login") {
                                LoginScreen(navController = navController)
                            }
                            composable("registro") {
                                RegisterScreen(navController = navController)
                            }
                            composable("intereses") {
                                InteresesScreen(navController = navController)
                            }
                            composable("objetivo_calculado") {
                                ObjetivoCalculadoScreen(onContinuar = {
                                    navController.navigate("intereses") {
                                        popUpTo("objetivo_calculado") { inclusive = true }
                                    }
                                })
                            }
                            composable("app") {
                                AppShell(onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                })
                            }
                        }
                    }

                }
            }


        }
    }
}

@Composable
fun AppShell(onLogout: () -> Unit) {
    val strings = LocalAppStrings.current
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStack?.destination?.route

    val rutasConBottomBar = setOf("home", "perfil", "menu_semanal", "rutina_dia", "racha")

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (rutaActual in rutasConBottomBar) {
                BottomBar(navController = navController, rutaActual = rutaActual, strings = strings)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        onIrPerfil = { navController.navigate("perfil") },
                        onIrMenu = { navController.navigate("menu_semanal") },
                        onIrRutina = { navController.navigate("rutina_dia") },
                        onIrRacha = { navController.navigate("racha") },
                        onLogout = onLogout
                    )
                }
                composable("perfil") {
                    PerfilScreen(
                        onEditarPerfil = { navController.navigate("editar_perfil") },
                        onVerGraficaPeso = { navController.navigate("grafica_peso") },
                        onVerGraficaImc = { navController.navigate("grafica_imc") }
                    )
                }
                composable("editar_perfil") {
                    EditarPerfilScreen(onVolver = { navController.popBackStack() })
                }
                composable("grafica_peso") {
                    GraficaPesoScreen(onVolver = { navController.popBackStack() })
                }
                composable("grafica_imc") {
                    GraficaImcScreen(onVolver = { navController.popBackStack() })
                }
                composable("menu_semanal") {
                    MenuSemanalScreen(onEditarMenu = { navController.navigate("editar_menu") })
                }
                composable("editar_menu") {
                    EditarMenuScreen(onVolver = { navController.popBackStack() })
                }
                composable("rutina_dia") {
                    RutinaDelDiaScreen(
                        onVerHistorial = { navController.navigate("historial") },
                        onEditarRutina = { navController.navigate("editar_rutina") }
                    )
                }
                composable("editar_rutina") {
                    EditarRutinaScreen(onVolver = { navController.popBackStack() })
                }
                composable("historial") {
                    HistorialScreen(onVolver = { navController.popBackStack() })
                }
                composable("racha") {
                    RachaScreen(onIrCheckDiario = { navController.navigate("check_diario") })
                }
                composable("check_diario") {
                    CheckDiarioScreen(onVolver = { navController.popBackStack() })
                }
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController, rutaActual: String?, strings: AppStrings) {
    data class ItemNav(val ruta: String, val icono: String, val etiqueta: String)

    val items = listOf(
        ItemNav("home", "🏠", strings.navHome),
        ItemNav("perfil", "👤", strings.navPerfil),
        ItemNav("menu_semanal", "🥗", strings.navMenu),
        ItemNav("rutina_dia", "💪", strings.navRutina),
        ItemNav("racha", "🔥", strings.navRacha)
    )

    NavigationBar(containerColor = Color(0xFF111111), tonalElevation = 0.dp) {
        items.forEach { item ->
            val seleccionado = rutaActual == item.ruta
            NavigationBarItem(
                selected = seleccionado,
                onClick = {
                    navController.navigate(item.ruta) {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Text(text = item.icono, fontSize = if (seleccionado) 22.sp else 20.sp)
                },
                label = {
                    Text(
                        text = item.etiqueta,
                        fontSize = 10.sp,
                        color = if (seleccionado) Color(0xFF00C853) else Color.White.copy(alpha = 0.5f),
                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF00C853),
                    indicatorColor = Color(0xFF00C853).copy(alpha = 0.15f)
                )
            )
        }
    }
}

// ── Pantallas de autenticación ─────────────────────────────────────────────────

@Composable
fun LoginScreen(navController: NavController) {
    val strings = LocalAppStrings.current
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)))

        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp, 40.dp),
            horizontalArrangement = Arrangement.End
        ) {
            LanguageToggleButton()
        }

        Column(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                    painter = painterResource(
                        id = R.drawable.nombre_fitme
                    ),
                    contentDescription = "", // Texto de accesibilidad
                    modifier = Modifier
                        .size(300.dp)
                        .padding(vertical = 0.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = strings.loginSubtitle, fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.height(40.dp))

            CustomField(value = usuario, onValueChange = { usuario = it }, label = strings.usernameLoginLabel)
            Spacer(modifier = Modifier.height(16.dp))
            CustomField(value = password, onValueChange = { password = it }, label = strings.passwordLoginLabel, isPassword = true)

            if (showError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = strings.loginErrorMsg, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (usuario.isNotEmpty() && password.isNotEmpty()) {
                        showError = false
                        navController.navigate("app") { popUpTo("login") { inclusive = true } }
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = strings.loginBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = strings.noAccountLink,
                color = Color.White,
                modifier = Modifier.clickable { navController.navigate("registro") }.padding(8.dp),
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController) {
    val strings = LocalAppStrings.current
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var diasEntrenamiento by remember { mutableStateOf(3f) }
    var showError by remember { mutableStateOf("") }
    var registroExitoso by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val isFormValid = usuario.isNotEmpty() && email.isNotEmpty() &&
            password.isNotEmpty() && edad.isNotEmpty() &&
            peso.isNotEmpty() && altura.isNotEmpty()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = strings.createAccountTitle, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = strings.completeDetailsHint, fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(30.dp))

            CustomField(value = usuario, onValueChange = { usuario = it; registroExitoso = false }, label = strings.usernameRegisterField)
            CustomField(value = email, onValueChange = { email = it; registroExitoso = false }, label = strings.emailRegisterField)
            CustomField(value = password, onValueChange = { password = it; registroExitoso = false }, label = strings.passwordRegisterField, isPassword = true)
            CustomField(value = edad, onValueChange = { edad = it; registroExitoso = false }, label = strings.ageRegisterField)
            CustomField(value = sexo, onValueChange = { sexo = it }, label = strings.genderRegisterField)
            CustomField(value = peso, onValueChange = { peso = it; registroExitoso = false }, label = strings.weightRegisterField)
            CustomField(value = altura, onValueChange = { altura = it; registroExitoso = false }, label = strings.heightRegisterField)

            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Días de entrenamiento por semana", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("${diasEntrenamiento.toInt()} días", color = Color(0xFF00C853), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Slider(
                    value = diasEntrenamiento,
                    onValueChange = { diasEntrenamiento = it },
                    valueRange = 1f..7f,
                    steps = 5,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF00C853),
                        activeTrackColor = Color(0xFF00C853),
                        inactiveTrackColor = Color(0xFF00C853).copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = when {
                        diasEntrenamiento.toInt() <= 2 -> "Pocas sesiones · Intensidad alta"
                        diasEntrenamiento.toInt() <= 4 -> "Frecuencia media · Intensidad media"
                        else -> "Alta frecuencia · Intensidad baja por sesión"
                    },
                    color = Color.White.copy(alpha = 0.55f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF00C853).copy(alpha = 0.12f))
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🧮", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = strings.bmiAutoCalculatedMsg, color = Color(0xFF00C853), fontSize = 12.sp)
                }
            }

            if (showError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = showError, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (!registroExitoso) {
                Button(
                    onClick = {
                        when {
                            !isFormValid -> showError = strings.fillAllRequiredMsg
                            peso.toFloatOrNull() == null -> showError = strings.enterValidWeightMsg
                            altura.toFloatOrNull() == null -> showError = strings.enterValidHeightMsg
                            else -> {
                                showError = ""
                                val prefs = com.example.fitme.data.UserPreferences(context)
                                prefs.nombre = usuario
                                prefs.email = email
                                edad.toIntOrNull()?.let { prefs.edad = it }
                                prefs.sexo = sexo
                                peso.toFloatOrNull()?.let { prefs.pesoActual = it }
                                altura.toFloatOrNull()?.let { prefs.altura = it }
                                registroExitoso = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = strings.signupBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00C853).copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✓", color = Color(0xFF00C853), fontSize = 36.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = strings.accountCreatedOkMsg, color = Color(0xFF00C853), fontWeight = FontWeight.Bold, fontSize = 15.sp, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = strings.nowCalculatingGoalMsg, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp, textAlign = TextAlign.Center)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate("objetivo_calculado") {
                            popUpTo("registro") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = strings.continueRegisterBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            if (!registroExitoso) {
                Text(
                    text = strings.alreadyAccountLink,
                    color = Color.White,
                    modifier = Modifier.clickable { navController.popBackStack() }.padding(8.dp),
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun CustomField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color.White.copy(alpha = 0.8f)) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF00C853),
            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color(0xFF00C853),
            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
            cursorColor = Color(0xFF00C853)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    )
}

@Composable
fun ObjetivoCard(texto: String, valor: String, seleccionado: String, onSelect: (String) -> Unit) {
    val isSelected = seleccionado == valor
    Card(
        modifier = Modifier.padding(4.dp).clickable { onSelect(valor) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFF00C853) else Color.DarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 8.dp), contentAlignment = Alignment.Center) {
            Text(
                text = texto, color = Color.White, fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
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
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("registro") {
                        RegisterScreen(navController = navController)
                    }
                    composable("intereses") {
                        InteresesScreen(navController = navController)
                    }
                    composable("objetivo_calculado") {
                        ObjetivoCalculadoScreen(onContinuar = {
                            navController.navigate("intereses") {
                                popUpTo("objetivo_calculado") { inclusive = true }
                            }
                        })
                    }
                    composable("app") {
                        AppShell(onLogout = {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        })
                    }
                }
            }

        }
    }
}
