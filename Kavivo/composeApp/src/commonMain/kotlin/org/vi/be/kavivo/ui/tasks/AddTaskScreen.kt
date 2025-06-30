package org.vi.be.kavivo.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import org.vi.be.kavivo.domain.tasks.models.TaskModel


object AddTaskScreen : Screen {
    @Composable
    override fun Content() {
        AddTaskContent()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTaskContent() {
    val navigator = LocalNavigator.currentOrThrow
    val tasksViewModel = koinViewModel<TaskViewModel>()

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaLimite by remember { mutableStateOf("") }
    var usuarioAsignado by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    val status by tasksViewModel.addTaskStatus.collectAsState()

    if (status) {
        navigator.pop()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nueva Tarea",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() } ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (titulo.isNotBlank()) {
                                isLoading = true
                                val newTask = TaskModel(
                                    title = titulo,
                                    description = descripcion,
                                    isDone = false,
                                    dueDate = 0L,
                                    createdAt = 0,
                                    updatedAt = null,
                                    assignedUserId = usuarioAsignado
                                )

                                tasksViewModel.saveTask(newTask)                            }
                        },
                        enabled = titulo.isNotBlank() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Guardar tarea"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Título (obligatorio)
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título *") },
                placeholder = { Text("Ingresa el título de la tarea") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = titulo.isBlank(),
                supportingText = {
                    if (titulo.isBlank()) {
                        Text(
                            text = "El título es obligatorio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // Campo Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                placeholder = { Text("Describe los detalles de la tarea") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Campo Fecha Límite
            OutlinedTextField(
                value = fechaLimite,
                onValueChange = { fechaLimite = it },
                label = { Text("Fecha Límite") },
                placeholder = { Text("DD/MM/AAAA") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Usuario Asignado
            OutlinedTextField(
                value = usuarioAsignado,
                onValueChange = { usuarioAsignado = it },
                label = { Text("Usuario Asignado") },
                placeholder = { Text("Nombre del usuario responsable") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de guardar alternativo (por si prefieres un botón grande)
            Button(
                onClick = {
                    if (titulo.isNotBlank()) {
                        isLoading = true

                        val newTask = TaskModel(
                            title = titulo,
                            description = descripcion,
                            isDone = false,
                            dueDate = 0L,
                            createdAt = 0,
                            updatedAt = null,
                            assignedUserId = usuarioAsignado
                        )

                        tasksViewModel.saveTask(newTask)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = titulo.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text("Guardando...")
                    }
                } else {
                    Text(
                        text = "Guardar Tarea",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/*
// En tu Composable principal donde llamas a AddTaskScreen:
AddTaskScreen(
    onNavigateBack = { navController.popBackStack() },
    onSaveTask = { titulo, descripcion, fechaLimite, usuarioAsignado ->
        val newTask = Task(
            titulo = titulo,
            descripcion = descripcion,
            fechaLimite = fechaLimite,
            usuarioAsignado = usuarioAsignado
        )

        viewModel.saveTask(newTask) { success ->
            if (success) {
                navController.popBackStack()
            }
        }
    }
)
*/