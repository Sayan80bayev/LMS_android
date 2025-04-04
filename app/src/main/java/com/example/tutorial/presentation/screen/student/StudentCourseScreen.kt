package com.example.tutorial.presentation.screen.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tutorial.data.local.entities.Grade
import com.example.tutorial.data.local.entities.Task
import com.example.tutorial.presentation.viewmodel.StudentsViewModel
import com.example.tutorial.util.SessionManager

@Composable
fun StudentCourseScreen(navController: NavController, courseId: Int) {
    val _studentId = SessionManager.getUser()?.id
    val studentId = _studentId ?: -1
    val context = LocalContext.current
    val viewModel: StudentsViewModel = viewModel(factory = StudentsViewModel.Companion.Factory(context))
    val grades by viewModel.grades.collectAsState()

    // Load tasks and grades when screen appears
    LaunchedEffect(Unit) {
        viewModel.loadStudentGrades(studentId, courseId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Course Details", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Your Tasks", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (grades.isEmpty()) {
            Text("No tasks available", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(grades) { grade ->
                    TaskGradeCard(grade = grade)
                }
            }
        }
    }
}

@Composable
private fun TaskGradeCard(grade: Grade) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Task #${grade.taskId}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Grade: ${grade.value.toString() ?: "Not graded"}",
                style = MaterialTheme.typography.bodySmall
            )
            grade.comments?.let { comments ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Comments: $comments",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}