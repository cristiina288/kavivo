package org.vi.be.kavivo.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.vi.be.kavivo.domain.tasks.models.TaskModel

object TaskScreen : Screen {
    @Composable
    override fun Content() {
        TasksScreen()
    }
}


@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun TasksScreen(
    onTaskClick: (TaskModel) -> Unit = {}
) {
    val navigator = LocalNavigator.currentOrThrow
    val tasksViewModel = koinViewModel<TaskViewModel>()

    val tasks by tasksViewModel.tasks.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Tareas") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.push(AddTaskScreen)
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
            Box(modifier = Modifier
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
                itemsIndexed(tasks ?: emptyList()) { index, task ->
                    TaskItem(task = task, onClick = { onTaskClick(task) }, tasksViewModel)
                }
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

    checked = task.isDone

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
            }

            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = !checked

                    task.isDone = checked

                    viewModel.isChecked(task)
                }
            )
        }
    }
}
