package com.example.tutorial.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutorial.data.local.entities.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    // StateFlow to hold the list of courses
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    // Function to load all courses from the repository
    fun loadCourses() {
        viewModelScope.launch {
            // TODO: Fetch courses from repository and assign to _courses
        }
    }

    fun createCourse(name: String) {
        viewModelScope.launch {
            // TODO: Add logic to create a new course
            // Update _courses after creation
        }
    }

    fun activateCourse(courseId: Int, activate: Boolean) {
        viewModelScope.launch {
            // TODO: Update course activation state
            // Refresh _courses
        }
    }

    fun assignTeacherToCourse(courseId: Int, teacherId: Int) {
        viewModelScope.launch {
            // TODO: Assign teacher to course
        }
    }

    fun assignStudentToCourse(courseId: Int, studentId: Int) {
        viewModelScope.launch {
            // TODO: Assign student to course
        }
    }
}