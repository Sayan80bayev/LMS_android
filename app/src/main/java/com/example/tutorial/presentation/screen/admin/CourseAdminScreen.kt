package com.example.tutorial.presentation.screen.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tutorial.data.local.entities.Course
import com.example.tutorial.data.local.entities.Person
import com.example.tutorial.data.local.entities.Role
import com.example.tutorial.presentation.components.SearchableDropdown

@Composable
fun CourseAdminScreen(
    course: Course,
    allPeople: List<Person> // список всех пользователей для добавления/удаления
) {
    var courseName by remember { mutableStateOf(course.name) }
    var isActive by remember { mutableStateOf(course.isActive) }
    var selectedTeacherId by remember { mutableStateOf(course.teacherId) }

    // Для примера: текущие студенты курса
    var assignedStudents by remember { mutableStateOf<List<Person>>(emptyList()) }

    val teachers = allPeople.filter { it.role == Role.TEACHER }
    val students = allPeople.filter { it.role == Role.STUDENT }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

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
                onCheckedChange = {
                    isActive = it
                    activateCourseStub(course.id, isActive)
                },
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Teacher
        SearchableDropdown(
            label = "Teacher",
            people = teachers,
            selectedPeople = teachers.filter { it.id == selectedTeacherId },
            onSelect = {
                selectedTeacherId = it.id
                assignTeacherStub(course.id, it.id)
            },
            onRemove = {
                selectedTeacherId = -1
                removeTeacherStub(course.id)
            },
            singleSelect = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Students (multiple selection)
        SearchableDropdown(
            label = "Students",
            people = students,
            selectedPeople = assignedStudents,
            onSelect = {
                assignedStudents = assignedStudents + it // Add student to the list
                assignStudentStub(course.id, it.id)
            },
            onRemove = {
                assignedStudents = assignedStudents.filter { student -> student.id != it.id } // Remove student from the list
                removeStudentStub(course.id, it.id)
            },
            singleSelect = false // Allow multiple selection for students
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                updateCourseNameStub(course.id, courseName)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save changes")
        }
    }
}

fun updateCourseNameStub(courseId: Int, newName: String) {
    println("Changed course name [$courseId] → \"$newName\"")
}

fun activateCourseStub(courseId: Int, isActive: Boolean) {
    println("Course [$courseId] ${if (isActive) "activated" else "deactivated"}")
}

fun assignTeacherStub(courseId: Int, teacherId: Int) {
    println("Teacher [$teacherId] assigned to course [$courseId]")
}

fun removeTeacherStub(courseId: Int) {
    println("Teacher removed from course [$courseId]")
}

fun assignStudentStub(courseId: Int, studentId: Int) {
    println("Student [$studentId] added to course [$courseId]")
}

fun removeStudentStub(courseId: Int, studentId: Int) {
    println("Student [$studentId] removed from course [$courseId]")
}