package com.example.tutorial.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTaskBottomSheet(onDismiss: () -> Unit) {
    // Bottom sheet content to add a new task
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Добавить задание", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Add task input fields here
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Task Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Handle task creation logic here
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать задание")
        }
    }
}