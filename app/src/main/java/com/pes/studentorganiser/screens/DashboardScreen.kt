package com.pes.studentorganiser.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pes.studentorganiser.components.AppBar
import com.pes.studentorganiser.components.AssignmentCard
import com.pes.studentorganiser.components.CourseInfo
import com.pes.studentorganiser.data.Assignment
import com.pes.studentorganiser.data.Course

@Composable
fun DashboardScreen(
    studentName: String,
    courses: List<Course>,
    assignments: List<Assignment>,
    onToggleComplete: (Assignment) -> Unit,
    onAddClick: () -> Unit
) {
    val pendingAssignments = assignments.filter { !it.isCompleted }

    Scaffold(
        topBar = {
            AppBar(
                title = "Welcome back, $studentName!",
                canNavigateBack = false,
                onBackClick = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add Assignment")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Active Courses", courses.size, Modifier.weight(1f))
                StatCard("Pending Tasks", pendingAssignments.size, Modifier.weight(1f))
            }

            // Active Courses (horizontal)
            SectionTitle("Active Courses")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(courses) { course ->
                    CourseInfo(
                        courseName = course.name,
                        courseCode = course.code,
                        instructor = course.instructor,
                        progress = course.progress
                    )
                }
            }

            // Urgent Deadlines (top 2 pending)
            SectionTitle("Urgent Deadlines")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (pendingAssignments.isEmpty()) {
                    Text("No pending assignments. Tap + to add one.")
                }
                pendingAssignments.take(2).forEach { assignment ->
                    AssignmentCard(
                        title = assignment.title,
                        course = assignment.course,
                        dueDate = assignment.dueDate,
                        priority = assignment.priority,
                        isCompleted = assignment.isCompleted,
                        onToggleComplete = { onToggleComplete(assignment) }
                    )
                }
            }

            // Space so the FAB doesn't cover the last card
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun StatCard(label: String, value: Int, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}