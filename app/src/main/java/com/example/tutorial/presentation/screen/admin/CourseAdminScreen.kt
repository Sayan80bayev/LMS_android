package com.example.tutorial.presentation.screen.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tutorial.presentation.components.SearchableDropdown
import com.example.tutorial.presentation.viewmodel.AdminViewModel

@Composable
fun CourseAdminScreen(
    courseId: Int,
    navController: NavController? = null
) {
    val context = LocalContext.current
    val viewModel: AdminViewModel = viewModel(factory = AdminViewModel.Companion.Factory(context))
    val courses by viewModel.courses.collectAsState()
    val teachers by viewModel.teachers.collectAsState()
    val students by viewModel.students.collectAsState()
    val courseStudents by viewModel.courseStudents.collectAsState()

    // Find the current course
    val course = remember(courses) { courses.find { it.id == courseId } }

    // Local state
    var courseName by remember { mutableStateOf(course?.name ?: "") }
    var isActive by remember { mutableStateOf(course?.isActive ?: false) }
    var selectedTeacherId by remember { mutableStateOf(course?.teacherId ?: -1) }
    val assignedStudents = remember(courseStudents, students) {
        students.filter { student -> courseStudents.contains(student.id) }
    }

    // Load initial data
    LaunchedEffect(Unit) {
        viewModel.loadCourses()
        viewModel.loadTeachers()
        viewModel.loadStudents()
        viewModel.loadStudentsInCourse(courseId)
    }

    // Update local state when course changes
    LaunchedEffect(course) {
        course?.let {
            courseName = it.name
            isActive = it.isActive
            selectedTeacherId = it.teacherId
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Course Management", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = courseName,
            onValueChange = { courseName = it },
            label = { Text("Course name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Course is active")
            Switch(
                checked = isActive,
                onCheckedChange = { active ->
                    isActive = active
                    course?.let {
                        viewModel.activateCourse(it.id, active)
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Teacher selection
        SearchableDropdown(
            label = "Teacher",
            people = teachers,
            selectedPeople = teachers.filter { it.id == selectedTeacherId },
            onSelect = { teacher ->
                selectedTeacherId = teacher.id
                course?.let {
                    viewModel.assignTeacherToCourse(it.id, teacher.id)
                }
            },
            onRemove = {
                selectedTeacherId = -1
                course?.let {
                    viewModel.assignTeacherToCourse(it.id, -1)
                }
            },
            singleSelect = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Students selection
        SearchableDropdown(
            label = "Students",
            people = students,
            selectedPeople = assignedStudents,
            onSelect = { student ->
                course?.let {
                    viewModel.assignStudentToCourse(it.id, student.id)
                }
            },
            onRemove = { student ->
                course?.let {
                    viewModel.removeStudentFromCourse(it.id, student.id)
                }
            },
            singleSelect = false
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                course?.let {
                    val updatedCourse = it.copy(name = courseName)
                    viewModel.updateCourse(updatedCourse)
                    navController?.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save changes")
        }
    }
}