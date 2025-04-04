package com.example.tutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tutorial.data.local.entities.Course
import com.example.tutorial.data.local.entities.Person
import com.example.tutorial.data.local.entities.Role
import com.example.tutorial.data.repository.SchoolRepository
import com.example.tutorial.presentation.screen.admin.AdminScreen
import com.example.tutorial.presentation.screen.HomeScreen
import com.example.tutorial.presentation.screen.admin.CourseAdminScreen
import com.example.tutorial.presentation.screen.auth.LoginScreen
import com.example.tutorial.presentation.screen.auth.RegisterScreen

class MainActivity : ComponentActivity() {
    private val repository by lazy { SchoolRepository.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.close()
    }
}

@Composable
fun MyApp() {
    // Инициализируем навигационный контроллер
    val navController = rememberNavController()

    // Scaffold — это контейнер, который помогает управлять основным UI
    Scaffold { paddingValues ->
        // Используем Box для центрирования
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp) // Учитываем отступы
                .fillMaxSize(), // Занимаем все доступное пространство
        ) {
            NavHost(
                navController = navController,
                startDestination = "login", // Устанавливаем экран входа как начальный
                modifier = Modifier.align(Alignment.Center) // Центрируем NavHost
            ) {
                // Навигация для экрана Login
                composable("login") {
                    LoginScreen(navController)
                }
                // Навигация для экрана Register
                composable("register") {
                    RegisterScreen(navController = navController)
                }

                composable("home"){
                    HomeScreen()
                }
                composable("admin_home") {
                    AdminScreen(navController)
                }
                composable(
                    route = "course_admin/{courseId}",
                    arguments = listOf(navArgument("courseId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt("courseId") ?: -1

                    // Заглушка для примера
                    val fakeCourse = Course(
                        id = courseId,
                        name = "Курс #$courseId",
                        teacherId = -1,
                        isActive = false
                    )

                    val allUsers = listOf( // Заглушка для всех юзеров
                        Person(1, "student1@mail.com", "Студент 1", "pass", Role.STUDENT),
                        Person(2, "teacher1@mail.com", "Препод 1", "pass", Role.TEACHER),
                        Person(3, "student2@mail.com", "Студент 2", "pass", Role.STUDENT),
                        Person(3, "student3@mail.com", "Студент 3", "pass", Role.STUDENT)
                    )

                    CourseAdminScreen(course = fakeCourse, allPeople = allUsers)
                }
            }
        }
    }
}