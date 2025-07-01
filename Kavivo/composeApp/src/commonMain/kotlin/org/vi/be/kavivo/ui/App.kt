package org.vi.be.kavivo.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.vi.be.kavivo.domain.comments.AddComment
import org.vi.be.kavivo.ui.expenses.ExpensesScreen
import org.vi.be.kavivo.ui.feed.AddCommentsScreen
import org.vi.be.kavivo.ui.feed.FeedScreen
import org.vi.be.kavivo.ui.home.HomeScreen
import org.vi.be.kavivo.ui.login.LoginScreen
import org.vi.be.kavivo.ui.tasks.AddTaskScreen
import org.vi.be.kavivo.ui.tasks.TasksScreen

// Definición de rutas
object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val TASKS = "tasks"
    const val EXPENSES = "expenses"
    const val FEED = "feed"

    const val ADD_TASKS = "add_tasks"
    const val ADD_COMMENTS = "add_comments"

    /*// Rutas de detalle con parámetros
    const val HOME_DETAIL = "home_detail/{title}"
    const val TAREAS_DETAIL = "tareas_detail/{title}"
    const val TAREAS_FORM = "tareas_form"
    const val GASTOS_LIST = "gastos_list"
    const val MURO_DETAIL = "muro_detail/{title}"

    // Funciones helper para crear rutas con parámetros
    fun homeDetail(title: String) = "home_detail/$title"
    fun tareasDetail(title: String) = "tareas_detail/$title"
    fun muroDetail(title: String) = "muro_detail/$title"*/
}

// Configuración de tabs para la bottom navigation
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, Routes.HOME),
    BottomNavItem("Tareas", Icons.Default.List, Routes.TASKS),
    BottomNavItem("Gastos", Icons.Default.ShoppingCart, Routes.EXPENSES),
    BottomNavItem("Muro", Icons.Default.Person, Routes.FEED)
)

@Composable
@Preview
fun App() {
    MaterialTheme {
        val appViewModel = koinViewModel<AppViewModel>()
        val user by appViewModel.user.collectAsState()
        val navController = rememberNavController()

        if (user != null) {
            MainAppWithBottomNav(navController)
        } else {
            LoginNavigation(navController)
        }
    }
}


@Composable
fun LoginNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
    }
}


@Composable
fun MainAppWithBottomNav(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Solo mostrar bottom nav en las pantallas principales
            if (currentRoute in bottomNavItems.map { it.route }) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Evitar múltiples copias del mismo destino
                                    /*popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }*/
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        MainNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        // Pantallas principales
        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(Routes.TASKS) {
            TasksScreen(navController)
        }

        composable(Routes.EXPENSES) {
            ExpensesScreen(navController)
        }

        composable(Routes.FEED) {
            FeedScreen(navController)
        }

        composable(Routes.ADD_TASKS) {
            AddTaskScreen(navController)
        }

        composable(Routes.ADD_COMMENTS) {
            AddCommentsScreen(navController)
        }
/*
        // Pantallas de detalle
        composable(Routes.HOME_DETAIL) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            HomeDetailScreen(navController, title)
        }

        composable(Routes.TAREAS_DETAIL) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            TareasDetailScreen(navController, title)
        }

        composable(Routes.TAREAS_FORM) {
            TareasFormScreen(navController)
        }

        composable(Routes.GASTOS_LIST) {
            GastosListScreen(navController)
        }

        composable(Routes.MURO_DETAIL) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            MuroDetailScreen(navController, title)
        }*/
    }
}