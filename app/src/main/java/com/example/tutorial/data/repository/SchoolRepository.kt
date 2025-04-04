package com.example.tutorial.data.repository

import android.content.Context
import com.example.tutorial.data.local.AppDatabaseHelper
import com.example.tutorial.data.local.dao.CourseDAO
import com.example.tutorial.data.local.dao.CourseStudentDAO
import com.example.tutorial.data.local.dao.GradeDAO
import com.example.tutorial.data.local.dao.PersonDAO
import com.example.tutorial.data.local.dao.TaskDAO

class SchoolRepository private constructor(context: Context) {
    private val dbHelper = AppDatabaseHelper(context.applicationContext)
    private val db = dbHelper.writableDatabase

    val personDao = PersonDAO(db)
    val courseDao = CourseDAO(db)
    val taskDao = TaskDAO(db)
    val gradeDao = GradeDAO(db)
    val courseStudentDao = CourseStudentDAO(db)

    companion object {
        @Volatile
        private var INSTANCE: SchoolRepository? = null

        fun getInstance(context: Context): SchoolRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SchoolRepository(context).also { INSTANCE = it }
            }
        }
    }

    fun close() {
        db.close()
        dbHelper.close()
        INSTANCE = null
    }
}