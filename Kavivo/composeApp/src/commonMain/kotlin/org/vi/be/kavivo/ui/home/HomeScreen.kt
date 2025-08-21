package org.vi.be.kavivo.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import org.vi.be.kavivo.domain.tasks.models.TaskModel
import org.vi.be.kavivo.ui.Routes
import org.vi.be.kavivo.ui.helpers.groupTasksByDate
import kotlin.coroutines.coroutineContext


@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val homeViewModel = koinViewModel<HomeViewModel>()

    val tasks by homeViewModel.tasks.collectAsState(initial = emptyList())

    // Agrupar y ordenar tareas por fecha
    val groupedTasks = remember(tasks) {
        if (!tasks.isNullOrEmpty()) {
            groupTasksByDate(tasks ?: emptyList(), true)
        } else {
            emptyList()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                "Recordatorios",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (groupedTasks.isEmpty() == true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text("No hay tareas para hoy")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    groupedTasks.forEach { (dateInfo, tasksForDate) ->
                        // Header de la fecha
                        item(key = "header_${dateInfo.dateMillis}") {
                            DateHeader(
                                taskCount = tasksForDate.size,
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
                                   // onTaskClick(task)
                                },
                                homeViewModel
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

                    item {
                        TextButton (
                            onClick = {
                                navController.navigate(Routes.TASKS)
                            }
                        ) {
                            Text("Ver todas las tareas")
                        }
                    }
                }
            }
        }
    }
}



@Composable
private fun TaskItem(
    task: TaskModel,
    onClick: () -> Unit,
    viewModel: HomeViewModel
) {
    var checked by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }

    checked = task.isDone

    if (isVisible) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 8.dp),
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
private fun DateHeader(
    taskCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
                text = "Tareas de hoy",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Badge(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = taskCount.toString(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

