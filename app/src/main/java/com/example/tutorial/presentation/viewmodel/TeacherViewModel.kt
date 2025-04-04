package com.example.tutorial.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tutorial.data.local.entities.Course
import com.example.tutorial.data.local.entities.Grade
import com.example.tutorial.data.local.entities.Task
import com.example.tutorial.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeacherViewModel(private val context: Context) : ViewModel() {
    private val repository = SchoolRepository.getInstance(context)

    // State flows
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _grades = MutableStateFlow<List<Grade>>(emptyList())
    val grades: StateFlow<List<Grade>> = _grades

    // Load teacher's courses
    fun loadTeacherCourses(teacherId: Int) {
        viewModelScope.launch {
            _courses.value = repository.courseDao.getCoursesByTeacher(teacherId)
        }
    }

    // Load tasks for a course
    fun loadCourseTasks(courseId: Int) {
        viewModelScope.launch {
            _tasks.value = repository.taskDao.getTasksByCourse(courseId)
        }
    }

    // Create a new task
    fun createTask(name: String, courseId: Int) {
        viewModelScope.launch {
            repository.taskDao.createTask(name, courseId)
            loadCourseTasks(courseId) // Refresh tasks
        }
    }

    // Delete a task
    fun deleteTask(taskId: Int, courseId: Int) {
        viewModelScope.launch {
            // First delete related grades
            repository.gradeDao.getGradesByTask(taskId).forEach { grade ->
                repository.gradeDao.deleteGrade(grade.id)
            }
            // Then delete the task
            repository.taskDao.deleteTask(taskId)
            loadCourseTasks(courseId) // Refresh tasks
        }
    }

    // Get grades for a task
    fun loadTaskGrades(taskId: Int) {
        viewModelScope.launch {
            _grades.value = repository.gradeDao.getGradesByTask(taskId)
        }
    }

    // Update or create a grade
    fun updateGrade(studentId: Int, taskId: Int, value: Float, comments: String?) {
        viewModelScope.launch {
            val existingGrade = repository.gradeDao.getGradeForStudentInTask(studentId, taskId)
            if (existingGrade != null) {
                // Update existing grade
                repository.gradeDao.updateGrade(existingGrade.copy(value = value, comments = comments))
            } else {
                // Create new grade
                repository.gradeDao.createGrade(taskId, studentId, value, comments)
            }
            loadTaskGrades(taskId) // Refresh grades
        }
    }
    companion object {
        class Factory(private val context: Context) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TeacherViewModel(context) as T
            }
        }
    }
}