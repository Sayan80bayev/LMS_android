package com.example.tutorial.presentation.screen.teacher

import android.se.omapi.Session
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
import com.example.tutorial.presentation.viewmodel.TeacherViewModel
import com.example.tutorial.util.SessionManager

@Composable
fun TeacherScreen(navController: NavController) {
    val _teacherId = SessionManager.getUser()?.id
    val teacherId = _teacherId ?: -1
    val context = LocalContext.current
    val viewModel: TeacherViewModel = viewModel(factory = TeacherViewModel.Companion.Factory(context))
    val courses by viewModel.courses.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTeacherCourses(teacherId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("My Courses", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        if (courses.isEmpty()) {
            Text("No courses assigned", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(courses) { course ->
                    CourseCard(course = course, onClick = {
                        navController.navigate("teacher_course_screen/${course.id}")
                    })
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