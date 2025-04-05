package com.example.tutorial.presentation.screen.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tutorial.data.local.entities.Grade
import com.example.tutorial.data.local.entities.Person
import com.example.tutorial.data.repository.SchoolRepository
import com.example.tutorial.presentation.viewmodel.TeacherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTaskScreen(navController: NavController, courseId: Int, taskId: Int) {
    val context = LocalContext.current
    val viewModel: TeacherViewModel = viewModel(factory = TeacherViewModel.Companion.Factory(context))
    val grades by viewModel.grades.collectAsState()
    val students = remember { SchoolRepository.getInstance(context).courseStudentDao.getStudentsInCourseWithDetails(courseId) }

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var selectedStudent by remember { mutableStateOf<Person?>(null) }
    var score by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadTaskGrades(taskId)
    }

    val filteredStudents = students.filter { student ->
        student.name.contains(searchQuery.text, ignoreCase = true) &&
                !grades.any { it.studentId == student.id }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Grade Task", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search student") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxHeight(0.7f)) {
            items(grades) { grade ->
                val student = students.find { it.id == grade.studentId }
                student?.let {
                    GradeItem(
                        student = it,
                        grade = grade,
                        onClick = {
                            selectedStudent = it
                            score = grade.value.toString()
                            comments = grade.comments ?: ""
                            showSheet = true
                        }
                    )
                }
            }

            items(filteredStudents) { student ->
                StudentItem(
                    student = student,
                    onClick = {
                        selectedStudent = student
                        score = ""
                        comments = ""
                        showSheet = true
                    }
                )
            }
        }
    }

    if (showSheet && selectedStudent != null) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Grade for ${selectedStudent?.name}", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = score,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == '.' }) score = newValue
                    },
                    label = { Text("Score") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = comments,
                    onValueChange = { comments = it },
                    label = { Text("Comments") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        selectedStudent?.let { student ->
                            score.toFloatOrNull()?.let { scoreValue ->
                                viewModel.updateGrade(
                                    studentId = student.id,
                                    taskId = taskId,
                                    value = scoreValue,
                                    comments = comments.ifEmpty { null }
                                )
                            }
                        }
                        showSheet = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Grade")
                }
            }
        }
    }
}

@Composable
private fun StudentItem(student: Person, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = student.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun GradeItem(student: Person, grade: Grade, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(student.name, style = MaterialTheme.typography.bodyMedium)
            Text("Grade: ${grade.value}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}