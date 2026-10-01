package com.pes.studentorganiser.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pes.studentorganiser.components.BottomBar
import com.pes.studentorganiser.data.Assignment
import com.pes.studentorganiser.data.SampleData
import com.pes.studentorganiser.screens.AddAssignmentScreen
import com.pes.studentorganiser.screens.AssignmentsScreen
import com.pes.studentorganiser.screens.DashboardScreen
import com.pes.studentorganiser.screens.ProfileScreen
import com.pes.studentorganiser.screens.SettingsScreen

@Composable
fun StudentOrganiserApp(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Shared assignment list: every screen sees the same data
    val assignments = remember { SampleData.assignments.toMutableStateList() }

    val toggleComplete: (Assignment) -> Unit = { assignment ->
        val index = assignments.indexOfFirst { it.id == assignment.id }
        if (index != -1) {
            assignments[index] = assignment.copy(isCompleted = !assignment.isCompleted)
        }
    }

    val goToAddAssignment: () -> Unit = {
        navController.navigate(Screen.AddAssignment.route)
    }

    Scaffold(
        // Each screen has its own Scaffold/TopAppBar, so this outer one adds no insets
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // Hide the bottom bar on the Add Assignment screen
            if (currentRoute != Screen.AddAssignment.route) {
                BottomBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    studentName = SampleData.studentName,
                    courses = SampleData.courses,
                    assignments = assignments,
                    onToggleComplete = toggleComplete,
                    onAddClick = goToAddAssignment
                )
            }
            composable(Screen.Assignments.route) {
                AssignmentsScreen(
                    assignments = assignments,
                    onToggleComplete = toggleComplete,
                    onAddClick = goToAddAssignment
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    studentName = SampleData.studentName,
                    studentId = SampleData.studentId,
                    major = SampleData.major
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = onDarkModeChange
                )
            }
            composable(Screen.AddAssignment.route) {
                AddAssignmentScreen(
                    onSave = { newAssignment ->
                        val nextId = (assignments.maxOfOrNull { it.id } ?: 0) + 1
                        assignments.add(newAssignment.copy(id = nextId))
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}