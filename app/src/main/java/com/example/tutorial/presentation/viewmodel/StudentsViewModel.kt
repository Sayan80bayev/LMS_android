package com.example.tutorial.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tutorial.data.local.entities.Course
import com.example.tutorial.data.local.entities.Grade
import com.example.tutorial.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentsViewModel(private val context: Context) : ViewModel() {
    private val repository = SchoolRepository.getInstance(context)

    // State flows
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private val _grades = MutableStateFlow<List<Grade>>(emptyList())
    val grades: StateFlow<List<Grade>> = _grades

    // Load student's courses
    fun loadStudentCourses(studentId: Int) {
        viewModelScope.launch {
            val courseIds = repository.courseStudentDao.getCoursesForStudent(studentId)
            _courses.value = courseIds.mapNotNull { repository.courseDao.getCourseById(it) }
        }
    }

    // Load grades for a student in a course
    fun loadStudentGrades(studentId: Int, courseId: Int) {
        viewModelScope.launch {
            // Get all tasks for the specific course
            val tasks = repository.taskDao.getTasksByCourse(courseId)
            // Get grades only for these tasks and this student
            _grades.value = tasks.mapNotNull { task ->
                repository.gradeDao.getGradeForStudentInTask(studentId, task.id)
            }
        }
    }

    companion object {
        class Factory(private val context: Context) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StudentsViewModel(context) as T
            }
        }
    }
}