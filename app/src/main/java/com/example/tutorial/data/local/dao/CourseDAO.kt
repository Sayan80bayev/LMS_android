package com.example.tutorial.data.local.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tutorial.data.local.AppDatabaseHelper
import com.example.tutorial.data.local.entities.Course

class CourseDAO(private val db: SQLiteDatabase) {

    fun createCourse(name: String, teacherId: Int, isActive: Boolean = false): Long {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_NAME, name)
            put(AppDatabaseHelper.COLUMN_TEACHER_ID, teacherId)
            put(AppDatabaseHelper.COLUMN_IS_ACTIVE, if (isActive) 1 else 0)
        }
        return db.insert(AppDatabaseHelper.TABLE_COURSES, null, values)
    }

    fun getCourseById(id: Int): Course? {
        val cursor = db.query(
            AppDatabaseHelper.TABLE_COURSES,
            null,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) cursorToCourse(it) else null
        }
    }

    fun getAllCourses(): List<Course> {
        val courses = mutableListOf<Course>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_COURSES,
            null, null, null, null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                courses.add(cursorToCourse(it))
            }
        }
        return courses
    }

    fun getActiveCourses(): List<Course> {
        val courses = mutableListOf<Course>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_COURSES,
            null,
            "${AppDatabaseHelper.COLUMN_IS_ACTIVE} = 1",
            null, null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                courses.add(cursorToCourse(it))
            }
        }
        return courses
    }

    fun getCoursesByTeacher(teacherId: Int): List<Course> {
        val courses = mutableListOf<Course>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_COURSES,
            null,
            "${AppDatabaseHelper.COLUMN_TEACHER_ID} = ?",
            arrayOf(teacherId.toString()),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                courses.add(cursorToCourse(it))
            }
        }
        return courses
    }

    fun updateCourse(course: Course): Int {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_NAME, course.name)
            put(AppDatabaseHelper.COLUMN_TEACHER_ID, course.teacherId)
            put(AppDatabaseHelper.COLUMN_IS_ACTIVE, if (course.isActive) 1 else 0)
        }
        return db.update(
            AppDatabaseHelper.TABLE_COURSES,
            values,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(course.id.toString())
        )
    }

    fun deleteCourse(id: Int): Int {
        return db.delete(
            AppDatabaseHelper.TABLE_COURSES,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun activateCourse(courseId: Int, isActive: Boolean): Int {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_IS_ACTIVE, if (isActive) 1 else 0)
        }
        return db.update(
            AppDatabaseHelper.TABLE_COURSES,
            values,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(courseId.toString())
        )
    }

    private fun cursorToCourse(cursor: Cursor): Course {
        return Course(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_NAME)),
            teacherId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_TEACHER_ID)),
            isActive = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_IS_ACTIVE)) == 1
        )
    }
}