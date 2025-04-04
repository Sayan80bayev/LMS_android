package com.example.tutorial.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutorial.data.local.entities.Course
import com.example.tutorial.data.local.entities.Person
import com.example.tutorial.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(private val context: Context) : ViewModel() {

    private val repository = SchoolRepository.getInstance(context)

    // State flows for courses
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    // State flows for teachers
    private val _teachers = MutableStateFlow<List<Person>>(emptyList())
    val teachers: StateFlow<List<Person>> = _teachers

    // State flows for students
    private val _students = MutableStateFlow<List<Person>>(emptyList())
    val students: StateFlow<List<Person>> = _students

    // State flows for course students
    private val _courseStudents = MutableStateFlow<List<Int>>(emptyList())
    val courseStudents: StateFlow<List<Int>> = _courseStudents

    // Function to load all courses from the repository
    fun loadCourses() {
        viewModelScope.launch {
            _courses.value = repository.courseDao.getAllCourses()
        }
    }

    // Function to load all teachers
    fun loadTeachers() {
        viewModelScope.launch {
            _teachers.value = repository.personDao.getTeachers()
        }
    }

    // Function to load all students
    fun loadStudents() {
        viewModelScope.launch {
            _students.value = repository.personDao.getStudents()
        }
    }

    // Function to load students in a specific course
    fun loadStudentsInCourse(courseId: Int) {
        viewModelScope.launch {
            _courseStudents.value = repository.courseStudentDao.getStudentsInCourse(courseId)
        }
    }

    fun createCourse(name: String, teacherId: Int = -1, isActive: Boolean = false) {
        viewModelScope.launch {
            repository.courseDao.createCourse(name, teacherId, isActive)
            loadCourses() // Refresh the courses list
        }
    }

    fun activateCourse(courseId: Int, activate: Boolean) {
        viewModelScope.launch {
            repository.courseDao.activateCourse(courseId, activate)
            loadCourses() // Refresh the courses list
        }
    }

    fun assignTeacherToCourse(courseId: Int, teacherId: Int) {
        viewModelScope.launch {
            // First get the course to update
            val course = repository.courseDao.getCourseById(courseId)
            course?.let {
                val updatedCourse = it.copy(teacherId = teacherId)
                repository.courseDao.updateCourse(updatedCourse)
                loadCourses() // Refresh the courses list
            }
        }
    }

    fun assignStudentToCourse(courseId: Int, studentId: Int) {
        viewModelScope.launch {
            if (!repository.courseStudentDao.isStudentInCourse(courseId, studentId)) {
                repository.courseStudentDao.addStudentToCourse(courseId, studentId)
                loadStudentsInCourse(courseId) // Refresh the students list for this course
            }
        }
    }

    fun removeStudentFromCourse(courseId: Int, studentId: Int) {
        viewModelScope.launch {
            repository.courseStudentDao.removeStudentFromCourse(courseId, studentId)
            loadStudentsInCourse(courseId) // Refresh the students list for this course
        }
    }

    fun updateCourse(course: Course) {
        viewModelScope.launch {
            repository.courseDao.updateCourse(course)
            loadCourses() // Refresh the courses list
        }
    }

    fun deleteCourse(courseId: Int) {
        viewModelScope.launch {
            // First delete all related data (tasks, grades, course_students)
            val tasks = repository.taskDao.getTasksByCourse(courseId)
            tasks.forEach { task ->
                // Delete grades for this task
                repository.gradeDao.getGradesByTask(task.id).forEach { grade ->
                    repository.gradeDao.deleteGrade(grade.id)
                }
                // Delete the task
                repository.taskDao.deleteTask(task.id)
            }

            // Delete course_students entries
            repository.courseStudentDao.getStudentsInCourse(courseId).forEach { studentId ->
                repository.courseStudentDao.removeStudentFromCourse(courseId, studentId)
            }

            // Finally delete the course
            repository.courseDao.deleteCourse(courseId)
            loadCourses() // Refresh the courses list
        }
    }

    fun getActiveCourses(): List<Course> {
        return repository.courseDao.getActiveCourses()
    }

    fun getCoursesByTeacher(teacherId: Int): List<Course> {
        return repository.courseDao.getCoursesByTeacher(teacherId)
    }

    fun getCoursesForStudent(studentId: Int): List<Course> {
        val courseIds = repository.courseStudentDao.getCoursesForStudent(studentId)
        return courseIds.mapNotNull { repository.courseDao.getCourseById(it) }
    }
}