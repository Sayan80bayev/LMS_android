package com.example.tutorial.data.local.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tutorial.data.local.AppDatabaseHelper
import com.example.tutorial.data.local.entities.Task

class TaskDAO(private val db: SQLiteDatabase) {

    fun createTask(name: String, courseId: Int): Long {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_NAME, name)
            put(AppDatabaseHelper.COLUMN_COURSE_ID, courseId)
        }
        return db.insert(AppDatabaseHelper.TABLE_TASKS, null, values)
    }

    fun getTaskById(id: Int): Task? {
        val cursor = db.query(
            AppDatabaseHelper.TABLE_TASKS,
            null,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) cursorToTask(it) else null
        }
    }

    fun getTasksByCourse(courseId: Int): List<Task> {
        val tasks = mutableListOf<Task>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_TASKS,
            null,
            "${AppDatabaseHelper.COLUMN_COURSE_ID} = ?",
            arrayOf(courseId.toString()),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                tasks.add(cursorToTask(it))
            }
        }
        return tasks
    }

    fun updateTask(task: Task): Int {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_NAME, task.name)
            put(AppDatabaseHelper.COLUMN_COURSE_ID, task.courseId)
        }
        return db.update(
            AppDatabaseHelper.TABLE_TASKS,
            values,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(task.id.toString())
        )
    }

    fun deleteTask(id: Int): Int {
        return db.delete(
            AppDatabaseHelper.TABLE_TASKS,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun cursorToTask(cursor: Cursor): Task {
        return Task(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_ID)),
            courseId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_COURSE_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COLUMN_NAME))
        )
    }
}