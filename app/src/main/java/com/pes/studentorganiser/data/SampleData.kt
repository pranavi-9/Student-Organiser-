package com.pes.studentorganiser.data

object SampleData {

    // Student info (used on Dashboard greeting + Profile screen)
    const val studentName = "PRANAVI S "
    const val studentId = "PES1UG24CS338"
    const val major = "Computer Science"

    val courses = listOf(
        Course("Intro to Programming", "CS101", "Dr. Sharma", 0.75f),
        Course("Linear Algebra", "MATH201", "Prof. Rao", 0.60f),
        Course("Technical Writing", "ENG102", "Dr. Menon", 0.85f),
        Course("Engineering Physics", "PHY101", "Prof. Iyer", 0.40f)
    )

    val assignments = listOf(
        Assignment(1, "Kotlin Basics Lab", "CS101", "02/10/2026", "High"),
        Assignment(2, "Matrix Operations Worksheet", "MATH201", "03/10/2026", "High"),
        Assignment(3, "Research Essay Draft", "ENG102", "05/10/2026", "Medium"),
        Assignment(4, "Optics Lab Report", "PHY101", "07/10/2026", "Medium"),
        Assignment(5, "Compose UI Mini Project", "CS101", "10/10/2026", "High"),
        Assignment(6, "Eigenvalues Problem Set", "MATH201", "12/10/2026", "Low"),
        Assignment(7, "Peer Review Notes", "ENG102", "14/10/2026", "Low"),
        Assignment(8, "Loops & Functions Quiz", "CS101", "25/09/2026", "Medium", isCompleted = true),
        Assignment(9, "Vectors Assignment", "MATH201", "20/09/2026", "Low", isCompleted = true)
    )
}