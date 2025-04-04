package com.example.tutorial.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutorial.data.repository.SchoolRepository
import com.example.tutorial.data.local.entities.Person
import com.example.tutorial.data.local.entities.Role
import com.example.tutorial.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(private val context: Context) : ViewModel() {
    private val repository = SchoolRepository.getInstance(context)
    private val userDao = repository.personDao

    fun register(
        name: String,
        email: String,
        password: String,
        role: Role = Role.STUDENT,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingUser = userDao.getPersonByEmail(email)
                if (existingUser != null) {
                    withContext(Dispatchers.Main) {
                        onError("User already registered")
                    }
                } else {
                    val newUser = Person(0, email, name, password, role)
                    userDao.createPerson(newUser)
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Registration failed")
                }
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getPersonByEmail(email)
            if (user == null || user.password != password) {
                withContext(Dispatchers.Main) {
                    onError("Invalid credentials")
                }
            } else {
                SessionManager.login(user)
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }
}