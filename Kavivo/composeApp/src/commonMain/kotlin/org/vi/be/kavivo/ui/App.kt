package org.vi.be.kavivo.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.vi.be.kavivo.domain.users.models.UserModel
import org.vi.be.kavivo.ui.expenses.ExpensesScreen
import org.vi.be.kavivo.ui.feed.AddCommentsScreen
import org.vi.be.kavivo.ui.feed.FeedScreen
import org.vi.be.kavivo.ui.groups.AddGroupScreen
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
    const val ADD_GROUPS = "add_groups"

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

        var startDestinationRoute = if (user != null) {
            Routes.HOME
        } else {
            Routes.LOGIN
        }

        MainAppWithBottomNav(navController, user, appViewModel, startDestinationRoute)

    }
}

/*
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
}*/


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppWithBottomNav(
    navController: NavHostController,
    user: UserModel?,
    viewModel: AppViewModel,
    startDestinationRoute: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var titleCurrentRoute by remember { mutableStateOf(buildTitle(currentRoute)) }

    // Estado para controlar el drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                userName = user?.name?.replaceFirstChar { it.uppercase() } ?: "Usuario",
                viewModel = viewModel,
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                // Solo mostrar top bar en las pantallas principales
                if (currentRoute in bottomNavItems.map { it.route }) {
                    CenterAlignedTopAppBar(
                        title = { Text(titleCurrentRoute, textAlign = TextAlign.Center) },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Abrir menú"
                                )
                            }
                        },
                        actions = {
                            if (currentRoute == Routes.FEED) {
                                IconButton(onClick = {
                                    navController.navigate(Routes.ADD_COMMENTS)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Agregar comentario"
                                    )
                                }
                            }
                        }
                    )
                }
            },
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
                                    titleCurrentRoute = buildTitle(item.route)
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
                modifier = Modifier.padding(innerPadding),
                startDestinationRoute = startDestinationRoute
            )
        }
    }
}

private fun buildTitle(currentRoute: String?) : String {
    return when (currentRoute) {
        "tasks" -> "TAREAS"
        "feed" -> "MURO"
        "expenses" -> "GASTOS"
        else -> "KAVIVO"
    }
}


@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestinationRoute: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestinationRoute,
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

        composable(Routes.ADD_GROUPS) {
            AddGroupScreen(navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController)
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


@Composable
fun DrawerContent(
    navController: NavHostController,
    userName: String,
    viewModel: AppViewModel,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            // Header del drawer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            // Items del menú
            /*DrawerMenuItem(
                title = "Mis grupos",
                icon = Icons.Default.Add,
                route = "add_groups"
            )*/
            NavigationDrawerItem(
                badge = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                onCloseDrawer()
                                navController.navigate(Routes.ADD_GROUPS)
                            }
                    )
                },
                label = {
                    Text(
                        "Mis grupos",
                        fontWeight = FontWeight.Bold
                    )
                },
                selected = false,//currentRoute == item.route,
                onClick = {
                    /*if (item.route != null) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                        }
                    } else {
                        // Manejar acciones especiales (como cerrar sesión)
                        item.action?.invoke()
                    }
                    onCloseDrawer()*/
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            var groups = viewModel.user.collectAsState().value?.groups
            var drawerItems = mutableListOf<DrawerMenuItem>()

            groups?.forEach {
                drawerItems.add(
                    DrawerMenuItem (
                        title = it.title
                    )
                )
            }

            Column (
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                groups?.forEach { group ->
                    var groupByDefault = if (viewModel.user.value?.groupByDefault == group.id) {
                        true
                    } else {
                        false
                    }

                    var selectedGroup = if (viewModel.groupSelectedId.collectAsState().value == group.id) {
                        true
                    } else {
                        false
                    }

                    NavigationDrawerItem(
                        icon = {
                            if (groupByDefault) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null
                                )
                            }

                        },
                        label = {
                            Text(group.title)
                        },
                        selected = selectedGroup,//currentRoute == item.route,
                        onClick = {
                            viewModel.saveGroupSelectedId(group.id)
                            /*if (item.route != null) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                }
                            } else {
                                // Manejar acciones especiales (como cerrar sesión)
                                item.action?.invoke()
                            }
                            onCloseDrawer()*/
                        },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = CircleShape
                            )

                    )
                }
            }



            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = null
                    )
                },
                label = {
                    Text("Cerrar Sesión")
                },
                selected = false,//currentRoute == item.route,
                onClick = {
                    onCloseDrawer()
                    viewModel.logOut()
                    /*if (item.route != null) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                        }
                    } else {
                        // Manejar acciones especiales (como cerrar sesión)
                        item.action?.invoke()
                    }
                    onCloseDrawer()*/
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Item de configuración al final
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                },
                label = {
                    Text("Configuración")
                },
                selected = false,
                onClick = {
                    // Navegar a configuración
                    onCloseDrawer()
                }
            )
        }
    }
}

// Data class para los items del drawer
data class DrawerMenuItem(
    val title: String,
   /* val icon: ImageVector,
    val route: String? = null,
    val action: (() -> Unit)? = null*/
)
/*

// Lista de items del drawer (puedes personalizarla según tus necesidades)
val drawerMenuItems = listOf(
    DrawerMenuItem(
        title = "Mis grupos",
        icon = Icons.Default.Add,
        route = "add_groups"
    ),
    DrawerMenuItem(
        title = "Perfil",
        icon = Icons.Default.Person,
        route = "profile"
    ),
    DrawerMenuItem(
        title = "Favoritos",
        icon = Icons.Default.Favorite,
        route = "favorites"
    ),*/
/*
    DrawerMenuItem(
        title = "Historial",
        icon = Icons.Default.History,
        route = "history"
    ),
    DrawerMenuItem(
        title = "Ayuda",
        icon = Icons.Default.Help,
        route = "help"
    ),*//*

    DrawerMenuItem(
        title = "Cerrar Sesión",
        icon = Icons.Default.ExitToApp,
        action = {
            // Aquí puedes manejar el cierre de sesión
            println("Cerrando sesión...")
        }
    )
)*/
