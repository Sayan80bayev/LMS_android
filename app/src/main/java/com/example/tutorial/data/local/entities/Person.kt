package com.example.tutorial.data.local.entities

data class Person(
    val id: Int,
    val email: String,
    val name: String,
    val password: String,
    val role: Role
)