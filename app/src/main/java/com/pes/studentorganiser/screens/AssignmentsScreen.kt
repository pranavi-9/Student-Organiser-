package com.pes.studentorganiser.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.pes.studentorganiser.components.AppBar
import com.pes.studentorganiser.components.AssignmentCard
import com.pes.studentorganiser.data.Assignment

@Composable
fun AssignmentsScreen(
    assignments: List<Assignment>,
    onToggleComplete: (Assignment) -> Unit,
    onAddClick: () -> Unit
) {
    val filters = listOf("All", "Pending", "Completed")
    var selectedFilter by rememberSaveable { mutableStateOf("All") }

    val filteredAssignments = when (selectedFilter) {
        "Pending" -> assignments.filter { !it.isCompleted }
        "Completed" -> assignments.filter { it.isCompleted }
        else -> assignments
    }

    Scaffold(
        topBar = {
            AppBar(title = "My Assignments", canNavigateBack = false, onBackClick = {})
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
        ) {
            // Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                filters.forEach { filter ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            role = Role.RadioButton
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = selectedFilter == filter, onClick = null)
                        Text(text = filter, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }

            if (filteredAssignments.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (selectedFilter == "All") "No assignments yet. Tap + to add one."
                        else "No ${selectedFilter.lowercase()} assignments."
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredAssignments, key = { it.id }) { assignment ->
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
            }
        }
    }
}