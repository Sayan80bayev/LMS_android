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
import com.example.tutorial.data.repository.SchoolRepository
import com.example.tutorial.presentation.screen.admin.AdminScreen
import com.example.tutorial.presentation.screen.admin.CourseAdminScreen
import com.example.tutorial.presentation.screen.auth.LoginScreen
import com.example.tutorial.presentation.screen.auth.RegisterScreen
import com.example.tutorial.presentation.screen.student.StudentCourseScreen
import com.example.tutorial.presentation.screen.student.StudentScreen
import com.example.tutorial.presentation.screen.teacher.TeacherCourseScreen
import com.example.tutorial.presentation.screen.teacher.TeacherScreen
import com.example.tutorial.presentation.screen.teacher.TeacherTaskScreen

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

    val navController = rememberNavController()

    Scaffold { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
        ) {
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.align(Alignment.Center)
            ) {

                composable("login") {
                    LoginScreen(navController)
                }

                composable("register") {
                    RegisterScreen(navController = navController)
                }

                composable("admin_screen") {
                    AdminScreen(navController)
                }
                composable(
                    route = "course_admin/{courseId}",
                    arguments = listOf(navArgument("courseId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt("courseId") ?: -1
                    CourseAdminScreen(courseId, navController)
                }
                composable("teacher_screen") {
                    TeacherScreen(navController)
                }
                composable("teacher_course_screen/{courseId}",
                    arguments = listOf(navArgument("courseId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt("courseId") ?: -1
                    TeacherCourseScreen(navController, courseId)
                }
                composable("teacher_task_screen/{courseId}/{taskId}",
                    arguments = listOf(navArgument("courseId") { type = NavType.IntType },
                        navArgument("taskId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt("courseId") ?: -1
                    val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
                    TeacherTaskScreen(navController, courseId, taskId)
                }
                composable("student_screen"){
                    StudentScreen(navController)
                }
                composable(
                    "student_course_screen/{courseId}",
                    arguments = listOf(navArgument("courseId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val courseId = backStackEntry.arguments?.getInt("courseId") ?: -1
                    StudentCourseScreen(navController, courseId)
                }
            }
        }
    }
}