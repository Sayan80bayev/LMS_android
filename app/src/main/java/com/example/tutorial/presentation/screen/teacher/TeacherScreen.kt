package com.example.tutorial.presentation.screen.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TeacherScreen(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Список курсов", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // Dummy list of courses
        repeat(3) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        navController.navigate("teacher_course_screen/$index")
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Курс #$index", style = MaterialTheme.typography.bodySmall)
                    Text("Статус: Активен")
                }
            }
        }
    }
}