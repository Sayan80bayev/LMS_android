package com.example.tutorial.presentation.screen.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.tutorial.data.local.entities.Role
import com.example.tutorial.presentation.viewmodel.AuthViewModel
import com.example.tutorial.util.SessionManager

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val authViewModel = remember { AuthViewModel(context) }

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Email
        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password
        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        // Login button
        Button(
            onClick = {
                if (emailState.value.isBlank() || passwordState.value.isBlank()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    authViewModel.login(
                        email = emailState.value,
                        password = passwordState.value,
                        onSuccess = {
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                            if (SessionManager.getUser()?.role  == Role.ADMIN){
                                navController.navigate("admin_home")
                            }else if (SessionManager.getUser()?.role == Role.TEACHER) {
                                navController.navigate("teacher_screen")
                            } else{
                                navController.navigate("home") // или на нужный экран
                            }
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        // Link to Register
        Text(
            text = buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Register")
                }
            },
            modifier = Modifier.clickable {
                navController.navigate("register")
            }
        )
    }
}