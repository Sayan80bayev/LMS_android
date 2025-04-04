package com.example.tutorial.presentation.screen.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tutorial.presentation.components.AddCourseBottomSheet
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavController) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            AddCourseBottomSheet(
                onConfirm = { courseName, isActive ->
                    createCourseStub(courseName, isActive) // ← Заглушка
                    showSheet = false
                }
            )
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    showSheet = true
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить курс")
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Список курсов (заглушка)", style = MaterialTheme.typography.labelLarge)

            Spacer(modifier = Modifier.height(8.dp))

            repeat(3) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("course_admin/$index")
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Курс #$index", style = MaterialTheme.typography.bodySmall)
                        Text("Статус: Активен")
                    }
                }
            }
        }
    }
}

fun createCourseStub(name: String, isActive: Boolean) {
    println("Создан курс (заглушка): Название = $name, Активен = $isActive")
}
