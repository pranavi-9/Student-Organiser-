package com.pes.studentorganiser.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pes.studentorganiser.data.SampleData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * State-hoisted form: it does NOT own the field values.
 * The parent (AddAssignmentScreen) passes values in and receives changes via callbacks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentForm(
    title: String,
    course: String,
    dueDate: String,
    priority: String,
    description: String,
    reminderEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onCourseChange: (String) -> Unit,
    onDueDateChange: (String) -> Unit,
    onPriorityChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onReminderChange: (Boolean) -> Unit,
    isSaveEnabled: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    courseOptions: List<String> = SampleData.courses.map { it.code }
) {
    // Purely visual UI state (menu open / dialog open), fine to keep locally
    var courseMenuExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Assignment Title (required)
        item {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Assignment Title *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 2. Course dropdown (required)
        item {
            Box {
                OutlinedTextField(
                    value = course,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Course *") },
                    placeholder = { Text("Select a course") },
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Choose course")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                // Invisible layer on top so tapping anywhere on the field opens the menu
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { courseMenuExpanded = true }
                )
                DropdownMenu(
                    expanded = courseMenuExpanded,
                    onDismissRequest = { courseMenuExpanded = false }
                ) {
                    courseOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onCourseChange(option)
                                courseMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // 3. Due Date with calendar icon (required)
        item {
            OutlinedTextField(
                value = dueDate,
                onValueChange = onDueDateChange,
                label = { Text("Due Date *") },
                placeholder = { Text("DD/MM/YYYY") },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Pick a date")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 4. Priority radio buttons
        item {
            Column {
                Text(
                    text = "Priority Level",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Low", "Medium", "High").forEach { option ->
                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = priority == option,
                                    onClick = { onPriorityChange(option) },
                                    role = Role.RadioButton
                                )
                                .padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = priority == option, onClick = null)
                            Text(text = option, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
            }
        }

        // 5. Description (multi-line)
        item {
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Description / Notes") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 6. Reminder checkbox
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onReminderChange(!reminderEnabled) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = reminderEnabled, onCheckedChange = onReminderChange)
                Text("Enable Reminder Notification")
            }
        }

        // Action buttons
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = onSave,
                        enabled = isSaveEnabled,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Assignment")
                    }
                }
                if (!isSaveEnabled) {
                    Text(
                        text = "Fill in Title, Course and Due Date to enable Save",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }

    // Calendar dialog opened by the Due Date icon
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDueDateChange(formatDate(millis))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}