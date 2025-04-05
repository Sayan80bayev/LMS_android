package com.example.tutorial.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "school_management.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_PERSONS = "persons"
        const val TABLE_COURSES = "courses"
        const val TABLE_TASKS = "tasks"
        const val TABLE_GRADES = "grades"
        const val TABLE_COURSE_STUDENTS = "course_students"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"

        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROLE = "role"

        const val COLUMN_TEACHER_ID = "teacher_id"
        const val COLUMN_IS_ACTIVE = "is_active"

        const val COLUMN_COURSE_ID = "course_id"

        const val COLUMN_TASK_ID = "task_id"
        const val COLUMN_STUDENT_ID = "student_id"
        const val COLUMN_VALUE = "value"
        const val COLUMN_COMMENTS = "comments"

        private const val CREATE_TABLE_PERSONS = """
            CREATE TABLE $TABLE_PERSONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_ROLE TEXT NOT NULL
            )
        """

        private const val CREATE_TABLE_COURSES = """
            CREATE TABLE $TABLE_COURSES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_TEACHER_ID INTEGER NOT NULL,
                $COLUMN_IS_ACTIVE INTEGER DEFAULT 0,
                FOREIGN KEY ($COLUMN_TEACHER_ID) REFERENCES $TABLE_PERSONS($COLUMN_ID)
            )
        """

        private const val CREATE_TABLE_TASKS = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_COURSE_ID INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_COURSE_ID) REFERENCES $TABLE_COURSES($COLUMN_ID)
            )
        """

        private const val CREATE_TABLE_GRADES = """
            CREATE TABLE $TABLE_GRADES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TASK_ID INTEGER NOT NULL,
                $COLUMN_STUDENT_ID INTEGER NOT NULL,
                $COLUMN_VALUE REAL NOT NULL,
                $COLUMN_COMMENTS TEXT,
                FOREIGN KEY ($COLUMN_TASK_ID) REFERENCES $TABLE_TASKS($COLUMN_ID),
                FOREIGN KEY ($COLUMN_STUDENT_ID) REFERENCES $TABLE_PERSONS($COLUMN_ID)
            )
        """

        private const val CREATE_TABLE_COURSE_STUDENTS = """
            CREATE TABLE $TABLE_COURSE_STUDENTS (
                $COLUMN_COURSE_ID INTEGER NOT NULL,
                $COLUMN_STUDENT_ID INTEGER NOT NULL,
                PRIMARY KEY ($COLUMN_COURSE_ID, $COLUMN_STUDENT_ID),
                FOREIGN KEY ($COLUMN_COURSE_ID) REFERENCES $TABLE_COURSES($COLUMN_ID),
                FOREIGN KEY ($COLUMN_STUDENT_ID) REFERENCES $TABLE_PERSONS($COLUMN_ID)
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PERSONS)
        db.execSQL(CREATE_TABLE_COURSES)
        db.execSQL(CREATE_TABLE_TASKS)
        db.execSQL(CREATE_TABLE_GRADES)
        db.execSQL(CREATE_TABLE_COURSE_STUDENTS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COURSE_STUDENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GRADES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COURSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PERSONS")
        onCreate(db)
    }
}