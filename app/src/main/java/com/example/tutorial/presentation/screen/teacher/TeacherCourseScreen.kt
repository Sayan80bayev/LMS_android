package com.example.tutorial.presentation.screen.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tutorial.data.local.entities.Task
import com.example.tutorial.presentation.components.AddTaskBottomSheet
import com.example.tutorial.presentation.viewmodel.TeacherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherCourseScreen(navController: NavController, courseId: Int) {
    val context = LocalContext.current
    val viewModel: TeacherViewModel = viewModel(factory = TeacherViewModel.Companion.Factory(context))
    val tasks by viewModel.tasks.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.loadCourseTasks(courseId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Course Tasks", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            if (tasks.isEmpty()) {
                Text("No tasks yet", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onTaskClick = {
                                navController.navigate("teacher_task_screen/$courseId/${task.id}")
                            },
                            onDelete = {
                                viewModel.deleteTask(task.id, courseId)
                            }
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
                AddTaskBottomSheet(
                    onConfirm = { taskName ->
                        viewModel.createTask(taskName, courseId)
                        showSheet = false
                    },
                    onDismiss = { showSheet = false }
                )
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = task.name,
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onTaskClick),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete task")
            }
        }
    }
}