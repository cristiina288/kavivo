package org.vi.be.kavivo.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.vi.be.kavivo.domain.tasks.models.DateInfo
import org.vi.be.kavivo.domain.tasks.models.TaskModel
import org.vi.be.kavivo.ui.Routes
import org.vi.be.kavivo.ui.helpers.formatDateTitle


@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun TasksScreen (
    navController: NavHostController
    //onTaskClick: (TaskModel) -> Unit = {}
) {
    //val navigator = LocalNavigator.currentOrThrow
    val tasksViewModel = koinViewModel<TaskViewModel>()

    val tasks by tasksViewModel.tasks.collectAsState(initial = emptyList())

    // Agrupar y ordenar tareas por fecha
    val groupedTasks = remember(tasks) {
        groupTasksByDate(tasks ?: emptyList())
    }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val shouldReload = savedStateHandle?.get<Boolean>("shouldReloadTasks") == true

    LaunchedEffect(shouldReload) {
        if (shouldReload) {
            tasksViewModel.getAllTasks()
            savedStateHandle?.remove<Boolean>("shouldReloadTasks")
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.ADD_TASKS)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir tarea"
                )
            }
        }
    ) { paddingValues ->
        if (tasks?.isEmpty() == true) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No hay tareas")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                groupedTasks.forEach { (dateInfo, tasksForDate) ->
                    // Header de la fecha
                    item(key = "header_${dateInfo.dateMillis}") {
                        DateHeader(
                            title = dateInfo.title,
                            taskCount = tasksForDate.size,
                            isOverdue = dateInfo.isOverdue
                        )
                    }

                    // Tareas de esa fecha
                    itemsIndexed(
                        items = tasksForDate,
                        key = { _, task -> "task_${task.id}_${dateInfo.dateMillis}" }
                    ) { index, task ->
                        TaskItem(
                            task = task,
                            onClick = {
                                //onTaskClick(task)
                                      },
                            tasksViewModel
                        )

                        if (index < tasksForDate.size - 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }

                    // Espacio entre grupos
                    item(key = "spacer_${dateInfo.dateMillis}") {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                /*itemsIndexed(tasks ?: emptyList()) { index, task ->
                    TaskItem(task = task, onClick = { onTaskClick(task) }, tasksViewModel)
                    Spacer(modifier = Modifier.height(4.dp))
                }*/
            }
        }
    }
}


@Composable
fun TaskItem(
    task: TaskModel,
    onClick: () -> Unit,
    viewModel: TaskViewModel
) {
    var checked by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }

    checked = task.isDone

    if (isVisible) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 20.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = task.title, style = MaterialTheme.typography.titleMedium)

                    if (!task.description.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = task.description ?: "", style = MaterialTheme.typography.bodyMedium)
                    }

                    val assignedUserName = task.assignedUserId?.replaceFirstChar { it.uppercase() }

                    if (!assignedUserName.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Asignada a $assignedUserName",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = !checked

                        task.isDone = checked

                        val today = Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date

                        val taskDate = Instant.fromEpochMilliseconds(task.dueDate!!)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date

                        if (taskDate < today && task.isDone) {
                            isVisible = false
                        }

                        viewModel.isChecked(task)
                    }
                )
            }
        }
    }
}


@Composable
fun DateHeader(
    title: String,
    taskCount: Int,
    isOverdue: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isOverdue -> MaterialTheme.colorScheme.errorContainer
                title == "Hoy" -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.secondaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = when {
                    isOverdue -> MaterialTheme.colorScheme.onErrorContainer
                    title == "Hoy" -> MaterialTheme.colorScheme.onPrimaryContainer
                    else -> MaterialTheme.colorScheme.onSecondaryContainer
                }
            )

            Badge(
                containerColor = when {
                    isOverdue -> MaterialTheme.colorScheme.error
                    title == "Hoy" -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.secondary
                }
            ) {
                Text(
                    text = taskCount.toString(),
                    color = when {
                        isOverdue -> MaterialTheme.colorScheme.onError
                        title == "Hoy" -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onSecondary
                    }
                )
            }
        }
    }
}


// Función para agrupar tareas por fecha
private fun groupTasksByDate(tasks: List<TaskModel>): List<Pair<DateInfo, List<TaskModel>>> {
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    // Filtrar solo tareas con fecha válida y agrupar por fecha
    val tasksWithDate = tasks
        .filter { it.dueDate != null && it.dueDate > 0 }
        .groupBy { task ->
            val taskDate = Instant.fromEpochMilliseconds(task.dueDate!!)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
            taskDate
        }

    // Tareas sin fecha (las ponemos al final)
    val tasksWithoutDate = tasks.filter { it.dueDate == null || it.dueDate <= 0 }

    // Crear lista ordenada de grupos
    val sortedGroups = mutableListOf<Pair<DateInfo, List<TaskModel>>>()

    // Ordenar fechas
    val sortedDates = tasksWithDate.keys.sorted()

    sortedDates.forEach { date ->
        val dateMillis = date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val allTasksForDate = tasksWithDate[date] ?: emptyList()

        // Filtrar tareas: si es anterior a hoy, solo mostrar las no completadas
        val tasksForDate = if (date < today) {
            allTasksForDate.filter { !it.isDone }
        } else {
            allTasksForDate
        }

        // Solo agregar el grupo si tiene tareas después del filtro
        if (tasksForDate.isNotEmpty()) {
            val dateInfo = when {
                date < today -> DateInfo(
                    dateMillis = dateMillis,
                    title = formatDateTitle(date, isOverdue = true),
                    isOverdue = true
                )

                date == today -> DateInfo(
                    dateMillis = dateMillis,
                    title = "Hoy",
                    isOverdue = false
                )

                date == today.plus(DatePeriod(days = 1)) -> DateInfo(
                    dateMillis = dateMillis,
                    title = "Mañana",
                    isOverdue = false
                )

                else -> DateInfo(
                    dateMillis = dateMillis,
                    title = formatDateTitle(date),
                    isOverdue = false
                )
            }

            sortedGroups.add(dateInfo to tasksForDate)
        }
    }

    // Agregar tareas sin fecha al final
    if (tasksWithoutDate.isNotEmpty()) {
        val noDateInfo = DateInfo(
            dateMillis = Long.MAX_VALUE,
            title = "Sin fecha",
            isOverdue = false
        )
        sortedGroups.add(noDateInfo to tasksWithoutDate)
    }

    return sortedGroups
}
