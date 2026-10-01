package com.pes.studentorganiser.data

data class Course(
    val name: String,
    val code: String,
    val instructor: String,
    val progress: Float
)

data class Assignment(
    val id: Int,
    val title: String,
    val course: String,
    val dueDate: String,
    val priority: String,
    val isCompleted: Boolean = false
)