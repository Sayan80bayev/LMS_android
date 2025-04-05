package com.example.tutorial.presentation.screen.student

import androidx.compose.foundation.clickable
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
import com.example.tutorial.data.local.entities.Course
import com.example.tutorial.presentation.viewmodel.StudentsViewModel
import com.example.tutorial.util.SessionManager

@Composable
fun StudentScreen(navController: NavController) {
    val _studentId = SessionManager.getUser()?.id
    val studentId = _studentId ?: -1
    val context = LocalContext.current
    val viewModel: StudentsViewModel = viewModel(factory = StudentsViewModel.Companion.Factory(context))
    val courses by viewModel.courses.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStudentCourses(studentId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("My Courses", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        if (courses.isEmpty()) {
            Text("No enrolled courses", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(courses) { course ->
                    CourseCard(
                        course = course,
                        onClick = {
                            navController.navigate("student_course_screen/${course.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CourseCard(course: Course, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(course.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = if (course.isActive) "Active" else "Inactive",
                style = MaterialTheme.typography.bodySmall,
                color = if (course.isActive) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
        }
    }
}