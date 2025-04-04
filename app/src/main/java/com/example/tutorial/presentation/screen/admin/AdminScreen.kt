package com.example.tutorial.presentation.screen.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tutorial.R
import com.example.tutorial.data.local.entities.Course
import com.example.tutorial.presentation.components.AddCourseBottomSheet
import com.example.tutorial.presentation.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: AdminViewModel = viewModel(factory = AdminViewModel.Companion.Factory(context))
    val courses by viewModel.courses.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    // Load courses when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadCourses()
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            AddCourseBottomSheet(
                onConfirm = { courseName, isActive ->
                    viewModel.createCourse(courseName, isActive = isActive)
                    showSheet = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Admin Screen") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    showSheet = true
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add course")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (courses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No courses available")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(courses) { course ->
                        CourseItem(
                            course = course,
                            onCourseClick = {
                                navController.navigate("course_admin/${course.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CourseItem(
    course: Course,
    onCourseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCourseClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(course.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = if (course.isActive) "Active" else "Inactive",
                style = MaterialTheme.typography.bodySmall,
                color = if (course.isActive) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
        }
    }
}
