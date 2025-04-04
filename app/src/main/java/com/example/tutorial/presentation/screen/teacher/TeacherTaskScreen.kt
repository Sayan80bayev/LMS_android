package com.example.tutorial.presentation.screen.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTaskScreen(navController: NavController, courseId: Int, taskId: Int) {
    val students = listOf("Alice Johnson", "Bob Smith", "Charlie Brown", "David White", "Emily Green")
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedStudent by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf("") }
    var showSheet by remember { mutableStateOf(false) }

    val filteredStudents = students.filter {
        it.contains(searchQuery.text, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Оценка для задания #$taskId", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Поиск студента") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable student list
        LazyColumn(modifier = Modifier.fillMaxHeight(0.7f)) {
            items(filteredStudents.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedStudent = filteredStudents[index]
                            showSheet = true
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = filteredStudents[index],
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    // Bottom sheet for evaluation input
    if (showSheet && selectedStudent != null) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Оценка для ${selectedStudent}", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = score,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) score = newValue
                    },
                    label = { Text("Введите оценку") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showSheet = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}