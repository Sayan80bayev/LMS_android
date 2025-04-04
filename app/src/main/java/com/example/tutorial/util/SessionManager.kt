package com.example.tutorial.util

import com.example.tutorial.data.local.entities.Person

object SessionManager {
    private var currentUser: Person? = null

    fun login(user: Person) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }

    fun getUser(): Person? = currentUser

    fun isLoggedIn(): Boolean = currentUser != null
}