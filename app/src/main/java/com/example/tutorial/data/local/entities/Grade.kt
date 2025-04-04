package com.example.tutorial.data.local.entities

data class Grade(
    val id: Int,
    val taskId: Int,
    val studentId: Int,
    val value: Float,
    val comments: String?
)