package com.example.tutorial.data.local.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tutorial.data.local.AppDatabaseHelper
import com.example.tutorial.data.local.entities.Grade

class GradeDAO(private val db: SQLiteDatabase) {

    fun createGrade(taskId: Int, studentId: Int, value: Float, comments: String?): Long {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_TASK_ID, taskId)
            put(AppDatabaseHelper.COLUMN_STUDENT_ID, studentId)
            put(AppDatabaseHelper.COLUMN_VALUE, value)
            put(AppDatabaseHelper.COLUMN_COMMENTS, comments)
        }
        return db.insert(AppDatabaseHelper.TABLE_GRADES, null, values)
    }

    fun getGradeById(id: Int): Grade? {
        val cursor = db.query(
            AppDatabaseHelper.TABLE_GRADES,
            null,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) cursorToGrade(it) else null
        }
    }

    fun getGradesByTask(taskId: Int): List<Grade> {
        val grades = mutableListOf<Grade>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_GRADES,
            null,
            "${AppDatabaseHelper.COLUMN_TASK_ID} = ?",
            arrayOf(taskId.toString()),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                grades.add(cursorToGrade(it))
            }
        }
        return grades
    }

    fun getGradesByStudent(studentId: Int): List<Grade> {
        val grades = mutableListOf<Grade>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_GRADES,
            null,
            "${AppDatabaseHelper.COLUMN_STUDENT_ID} = ?",
            arrayOf(studentId.toString()),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                grades.add(cursorToGrade(it))
            }
        }
        return grades
    }

    fun getGradeForStudentInTask(studentId: Int, taskId: Int): Grade? {
        val cursor = db.query(
            AppDatabaseHelper.TABLE_GRADES,
            null,
            "${AppDatabaseHelper.COLUMN_STUDENT_ID} = ? AND ${AppDatabaseHelper.COLUMN_TASK_ID} = ?",
            arrayOf(studentId.toString(), taskId.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) cursorToGrade(it) else null
        }
    }

    fun updateGrade(grade: Grade): Int {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_TASK_ID, grade.taskId)
            put(AppDatabaseHelper.COLUMN_STUDENT_ID, grade.studentId)
            put(AppDatabaseHelper.COLUMN_VALUE, grade.value)
            put(AppDatabaseHelper.COLUMN_COMMENTS, grade.comments)
        }
        return db.update(
            AppDatabaseHelper.TABLE_GRADES,
            values,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(grade.id.toString())
        )
    }

    fun deleteGrade(id: Int): Int {
        return db.delete(
            AppDatabaseHelper.TABLE_GRADES,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun cursorToGrade(cursor: Cursor): Grade {
        return Grade(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ID)),
            taskId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_TASK_ID)),
            studentId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_STUDENT_ID)),
            value = cursor.getFloat(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_VALUE)),
            comments = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_COMMENTS))
        )
    }
}