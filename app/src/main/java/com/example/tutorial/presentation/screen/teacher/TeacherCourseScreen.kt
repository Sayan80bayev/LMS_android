package com.example.tutorial.presentation.screen.teacher

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tutorial.presentation.components.AddTaskBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeacherCourseScreen(navController: NavController, courseId: Int) {
    var showSheet by remember { mutableStateOf(false) }
    var tasks by remember { mutableStateOf(listOf("Task 1", "Task 2", "Task 3")) }

    // Dummy function to handle delete task
    fun deleteTask(taskIndex: Int) {
        tasks = tasks.filterIndexed { index, _ -> index != taskIndex }
    }

    // Sheet state
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить задание")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Задания для курса #$courseId", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            tasks.forEachIndexed { index, task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("teacher_task_screen/$courseId/$index")
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Задание: $task", style = MaterialTheme.typography.bodySmall)
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Task",
                            modifier = Modifier
                                .clickable { deleteTask(index) }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                // AddTaskBottomSheet is the content of the Modal Bottom Sheet
                AddTaskBottomSheet(onDismiss = { showSheet = false })
            }
        }
    }
}