package com.example.tutorial.data.local.entities

data class Course(
    val id: Int,
    val name: String,
    val teacherId: Int,
    val isActive: Boolean = false // Admin can activate/deactivate
)