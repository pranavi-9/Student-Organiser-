package com.pes.studentorganiser.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pes.studentorganiser.components.AppBar
import com.pes.studentorganiser.components.AssignmentForm
import com.pes.studentorganiser.data.Assignment

@Composable
fun AddAssignmentScreen(
    onSave: (Assignment) -> Unit,
    onBack: () -> Unit
) {
    // Form state lives HERE and is hoisted into AssignmentForm
    var title by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    var description by remember { mutableStateOf("") }
    var reminderEnabled by remember { mutableStateOf(false) }

    // Validation: Save only when required fields are filled
    val isSaveEnabled = title.isNotBlank() && course.isNotBlank() && dueDate.isNotBlank()

    Scaffold(
        topBar = {
            AppBar(
                title = "Add New Assignment",
                canNavigateBack = true,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        AssignmentForm(
            title = title,
            course = course,
            dueDate = dueDate,
            priority = priority,
            description = description,
            reminderEnabled = reminderEnabled,
            onTitleChange = { title = it },
            onCourseChange = { course = it },
            onDueDateChange = { dueDate = it },
            onPriorityChange = { priority = it },
            onDescriptionChange = { description = it },
            onReminderChange = { reminderEnabled = it },
            isSaveEnabled = isSaveEnabled,
            onSave = {
                onSave(
                    Assignment(
                        id = 0, // the real id is assigned in StudentOrganiserApp
                        title = title.trim(),
                        course = course,
                        dueDate = dueDate.trim(),
                        priority = priority
                    )
                )
            },
            onCancel = onBack,
            modifier = Modifier.padding(innerPadding)
        )
    }
}