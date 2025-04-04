package com.example.tutorial.data.local.dao

import android.content.ContentValues
import com.example.tutorial.data.local.entities.Person
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.tutorial.data.local.AppDatabaseHelper
import com.example.tutorial.data.local.entities.Role

class PersonDAO(private val db: SQLiteDatabase) {

    fun createPerson(person: Person): Long {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_EMAIL, person.email)
            put(AppDatabaseHelper.COLUMN_NAME, person.name)
            put(AppDatabaseHelper.COLUMN_PASSWORD, person.password)
            put(AppDatabaseHelper.COLUMN_ROLE, person.role.name)
        }
        return db.insert(AppDatabaseHelper.TABLE_PERSONS, null, values)
    }

    fun getPersonById(id: Int): Person? {
        val cursor = db.query(
            AppDatabaseHelper.TABLE_PERSONS,
            null,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) cursorToPerson(it) else null
        }
    }

    fun getPersonByEmail(email: String): Person? {
        val cursor = db.query(
            AppDatabaseHelper.TABLE_PERSONS,
            null,
            "${AppDatabaseHelper.COLUMN_EMAIL} = ?",
            arrayOf(email),
            null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) cursorToPerson(it) else null
        }
    }

    fun getAllPersons(): List<Person> {
        val persons = mutableListOf<Person>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_PERSONS,
            null, null, null, null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                persons.add(cursorToPerson(it))
            }
        }
        return persons
    }

    fun updatePerson(person: Person): Int {
        val values = ContentValues().apply {
            put(AppDatabaseHelper.COLUMN_EMAIL, person.email)
            put(AppDatabaseHelper.COLUMN_NAME, person.name)
            put(AppDatabaseHelper.COLUMN_PASSWORD, person.password)
            put(AppDatabaseHelper.COLUMN_ROLE, person.role.name)
        }
        return db.update(
            AppDatabaseHelper.TABLE_PERSONS,
            values,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(person.id.toString())
        )
    }

    fun deletePerson(id: Int): Int {
        return db.delete(
            AppDatabaseHelper.TABLE_PERSONS,
            "${AppDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun getTeachers(): List<Person> {
        val teachers = mutableListOf<Person>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_PERSONS,
            null,
            "${AppDatabaseHelper.COLUMN_ROLE} = ?",
            arrayOf(Role.TEACHER.name),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                teachers.add(cursorToPerson(it))
            }
        }
        return teachers
    }

    fun getStudents(): List<Person> {
        val students = mutableListOf<Person>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_PERSONS,
            null,
            "${AppDatabaseHelper.COLUMN_ROLE} = ?",
            arrayOf(Role.STUDENT.name),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                students.add(cursorToPerson(it))
            }
        }
        return students
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