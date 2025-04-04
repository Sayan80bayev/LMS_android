package com.example.tutorial.presentation.screen.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun StudentScreen(navController: NavController) {
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
                        navController.navigate("student_course_screen/$index")
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