package com.example.tutorial.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tutorial.data.local.entities.Course
import com.example.tutorial.data.local.entities.Person
import com.example.tutorial.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(private val context: Context) : ViewModel() {

    private val repository = SchoolRepository.getInstance(context)

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private val _teachers = MutableStateFlow<List<Person>>(emptyList())
    val teachers: StateFlow<List<Person>> = _teachers

    private val _students = MutableStateFlow<List<Person>>(emptyList())
    val students: StateFlow<List<Person>> = _students

    private val _courseStudents = MutableStateFlow<List<Int>>(emptyList())
    val courseStudents: StateFlow<List<Int>> = _courseStudents

    fun loadCourses() {
        viewModelScope.launch {
            _courses.value = repository.courseDao.getAllCourses()
        }
    }

    fun loadTeachers() {
        viewModelScope.launch {
            _teachers.value = repository.personDao.getTeachers()
        }
    }

    fun loadStudents() {
        viewModelScope.launch {
            _students.value = repository.personDao.getStudents()
        }
    }

    fun loadStudentsInCourse(courseId: Int) {
        viewModelScope.launch {
            _courseStudents.value = repository.courseStudentDao.getStudentsInCourse(courseId)
        }
    }

    fun createCourse(name: String, teacherId: Int = -1, isActive: Boolean = false) {
        viewModelScope.launch {
            repository.courseDao.createCourse(name, teacherId, isActive)
            loadCourses()
        }
    }

    fun activateCourse(courseId: Int, activate: Boolean) {
        viewModelScope.launch {
            repository.courseDao.activateCourse(courseId, activate)
            loadCourses()
        }
    }

    fun assignTeacherToCourse(courseId: Int, teacherId: Int) {
        viewModelScope.launch {

            val course = repository.courseDao.getCourseById(courseId)
            course?.let {
                val updatedCourse = it.copy(teacherId = teacherId)
                repository.courseDao.updateCourse(updatedCourse)
                loadCourses()
            }
        }
    }

    fun assignStudentToCourse(courseId: Int, studentId: Int) {
        viewModelScope.launch {
            if (!repository.courseStudentDao.isStudentInCourse(courseId, studentId)) {
                repository.courseStudentDao.addStudentToCourse(courseId, studentId)
                loadStudentsInCourse(courseId)
            }
        }
    }

    fun removeStudentFromCourse(courseId: Int, studentId: Int) {
        viewModelScope.launch {
            repository.courseStudentDao.removeStudentFromCourse(courseId, studentId)
            loadStudentsInCourse(courseId)
        }
    }

    fun updateCourse(course: Course) {
        viewModelScope.launch {
            repository.courseDao.updateCourse(course)
            loadCourses()
        }
    }

    fun deleteCourse(courseId: Int) {
        viewModelScope.launch {

            val tasks = repository.taskDao.getTasksByCourse(courseId)
            tasks.forEach { task ->

                repository.gradeDao.getGradesByTask(task.id).forEach { grade ->
                    repository.gradeDao.deleteGrade(grade.id)
                }

                repository.taskDao.deleteTask(task.id)
            }

            repository.courseStudentDao.getStudentsInCourse(courseId).forEach { studentId ->
                repository.courseStudentDao.removeStudentFromCourse(courseId, studentId)
            }

            repository.courseDao.deleteCourse(courseId)
            loadCourses()
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

    companion object{
        class Factory(private val context: Context) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AdminViewModel(context) as T
            }
        }
    }
}