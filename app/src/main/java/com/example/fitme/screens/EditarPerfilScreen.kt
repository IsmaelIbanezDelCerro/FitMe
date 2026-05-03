package com.example.fitme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import com.example.fitme.GymBackground
import com.example.fitme.LanguageToggleButton
import com.example.fitme.LocalAppStrings
import com.example.fitme.data.UserPreferences
import com.example.fitme.viewmodel.PerfilViewModel

@Composable
fun EditarPerfilScreen(onVolver: () -> Unit) {
    val strings = LocalAppStrings.current
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val vm: PerfilViewModel = viewModel()

    var altura by remember { mutableStateOf(if (prefs.altura > 0) prefs.altura.toInt().toString() else "") }
    var peso by remember { mutableStateOf(if (prefs.pesoActual > 0) prefs.pesoActual.toString() else "") }
    var edad by remember { mutableStateOf(if (prefs.edad > 0) prefs.edad.toString() else "") }
    var sexo by remember { mutableStateOf(prefs.sexo) }
    var guardado by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    GymBackground {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = strings.editPerfilTitle, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                LanguageToggleButton()
            }
            Spacer(modifier = Modifier.height(28.dp))

            CampoNumerico(valor = altura, onCambio = { altura = it; guardado = false }, etiqueta = strings.heightField, placeholder = strings.heightPlaceholder)
            Spacer(modifier = Modifier.height(12.dp))
            CampoNumerico(valor = peso, onCambio = { peso = it; guardado = false }, etiqueta = strings.currentWeightField, placeholder = strings.weightPlaceholder, decimal = true)
            Spacer(modifier = Modifier.height(12.dp))
            CampoNumerico(valor = edad, onCambio = { edad = it; guardado = false }, etiqueta = strings.ageField, placeholder = strings.agePlaceholder)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = sexo,
                onValueChange = { sexo = it; guardado = false },
                label = { Text(strings.genderField, color = Color.White.copy(alpha = 0.8f)) },
                placeholder = { Text(strings.genderPlaceholder, color = Color.White.copy(alpha = 0.4f)) },
                colors = campoColores(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMsg, color = Color.Red, fontSize = 13.sp)
            }
            if (guardado) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = strings.dataSavedOk, color = Color(0xFF00C853), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    val alturaF = altura.toFloatOrNull()
                    val pesoF = peso.toFloatOrNull()
                    when {
                        alturaF == null || alturaF <= 0 -> errorMsg = strings.invalidHeightMsg
                        pesoF == null || pesoF <= 0 -> errorMsg = strings.invalidWeightMsg
                        else -> {
                            errorMsg = ""
                            prefs.altura = alturaF
                            prefs.pesoActual = pesoF
                            edad.toIntOrNull()?.let { prefs.edad = it }
                            if (sexo.isNotEmpty()) prefs.sexo = sexo
                            vm.registrarPeso(pesoF, alturaF)
                            guardado = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(strings.saveBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) { Text(strings.backBtn) }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun CampoNumerico(valor: String, onCambio: (String) -> Unit, etiqueta: String, placeholder: String, decimal: Boolean = false) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta, color = Color.White.copy(alpha = 0.8f)) },
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.4f)) },
        keyboardOptions = KeyboardOptions(keyboardType = if (decimal) KeyboardType.Decimal else KeyboardType.Number),
        colors = campoColores(),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun campoColores() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF00C853),
    unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = Color(0xFF00C853),
    cursorColor = Color(0xFF00C853)
)
