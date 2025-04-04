package com.example.tutorial.presentation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.tutorial.util.SessionManager

@Composable
fun HomeScreen() {
    val currentUser = SessionManager.getUser()
    val userName = currentUser?.name?.takeIf { it.isNotBlank() } ?: "Гость"

    Text(text = "Hello, $userName")
}