package com.example.tutorial.data.local.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tutorial.data.local.AppDatabaseHelper
import com.example.tutorial.data.local.entities.Person
import com.example.tutorial.data.local.entities.Role

class CourseStudentDAO(private val db: SQLiteDatabase) {

    fun addStudentToCourse(courseId: Int, studentId: Int): Long {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_COURSE_ID, courseId)
            put(AppDatabaseHelper.COLUMN_STUDENT_ID, studentId)
        }
        return db.insert(AppDatabaseHelper.TABLE_COURSE_STUDENTS, null, values)
    }

    fun removeStudentFromCourse(courseId: Int, studentId: Int): Int {
        return db.delete(
            AppDatabaseHelper.TABLE_COURSE_STUDENTS,
            "${AppDatabaseHelper.COLUMN_COURSE_ID} = ? AND ${AppDatabaseHelper.COLUMN_STUDENT_ID} = ?",
            arrayOf(courseId.toString(), studentId.toString())
        )
    }

    fun getStudentsInCourse(courseId: Int): List<Int> {
        val studentIds = mutableListOf<Int>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_COURSE_STUDENTS,
            arrayOf(AppDatabaseHelper.COLUMN_STUDENT_ID),
            "${AppDatabaseHelper.COLUMN_COURSE_ID} = ?",
            arrayOf(courseId.toString()),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                studentIds.add(it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_STUDENT_ID)))
            }
        }
        return studentIds
    }

    // New method to get full student objects for a course
    fun getStudentsInCourseWithDetails(courseId: Int): List<Person> {
        val students = mutableListOf<Person>()
        val query = """
            SELECT p.* FROM ${AppDatabaseHelper.TABLE_PERSONS} p
            INNER JOIN ${AppDatabaseHelper.TABLE_COURSE_STUDENTS} cs 
            ON p.${AppDatabaseHelper.COLUMN_ID} = cs.${AppDatabaseHelper.COLUMN_STUDENT_ID}
            WHERE cs.${AppDatabaseHelper.COLUMN_COURSE_ID} = ?
            AND p.${AppDatabaseHelper.COLUMN_ROLE} = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(courseId.toString(), Role.STUDENT.name))
        cursor.use {
            while (it.moveToNext()) {
                students.add(cursorToPerson(it))
            }
        }
        return students
    }

    fun getCoursesForStudent(studentId: Int): List<Int> {
        val courseIds = mutableListOf<Int>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_COURSE_STUDENTS,
            arrayOf(AppDatabaseHelper.COLUMN_COURSE_ID),
            "${AppDatabaseHelper.COLUMN_STUDENT_ID} = ?",
            arrayOf(studentId.toString()),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                courseIds.add(it.getInt(it.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_COURSE_ID)))
            }
        }
        return courseIds
    }

    fun isStudentInCourse(courseId: Int, studentId: Int): Boolean {
        val cursor = db.query(
            AppDatabaseHelper.TABLE_COURSE_STUDENTS,
            null,
            "${AppDatabaseHelper.COLUMN_COURSE_ID} = ? AND ${AppDatabaseHelper.COLUMN_STUDENT_ID} = ?",
            arrayOf(courseId.toString(), studentId.toString()),
            null, null, null
        )
        return cursor.use {
            it.count > 0
        }
    }

    private fun cursorToPerson(cursor: Cursor): Person {
        return Person(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ID)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_EMAIL)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_NAME)),
            password = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_PASSWORD)),
            role = Role.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ROLE)))
        )
    }
}