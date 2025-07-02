package org.vi.be.kavivo.ui.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.Clock.System
import org.koin.compose.viewmodel.koinViewModel
import org.vi.be.kavivo.domain.tasks.models.TaskModel
import org.vi.be.kavivo.ui.helpers.formatDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController
) {
    //val navigator = LocalNavigator.currentOrThrow
    val tasksViewModel = koinViewModel<TaskViewModel>()

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaLimite by remember { mutableStateOf("") }
    var usuarioAsignado by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    val status by tasksViewModel.addTaskStatus.collectAsState()
    val groupId by tasksViewModel.groupSelectedId.collectAsState()

    if (status) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set("shouldReloadTasks", true)

        navController.popBackStack()
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
                    IconButton(onClick = {
                        navController.popBackStack()
                    } ) {
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
                                    dueDate = selectedDate,
                                    createdAt = 0,
                                    updatedAt = null,
                                    assignedUserId = usuarioAsignado,
                                    groupId = groupId
                                )

                                tasksViewModel.saveTask(newTask)
                            }
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
               /* isError = errorTitle,
                supportingText = {
                    if (errorTitle) {
                        Text(
                            text = "El título es obligatorio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }*/
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Selecciona la fecha límite",
                    style = MaterialTheme.typography.bodyLarge
                )

            // Botón para mostrar el selector
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = if (selectedDate != null) {
                        "Cambiar fecha"
                    } else {
                        "Seleccionar fecha"
                    }
                )
            }

                // Mostrar la fecha seleccionada
                if (selectedDate != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Fecha seleccionada:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = formatDate(selectedDate!!),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                // Botón para limpiar la selección
                if (selectedDate != null) {
                    OutlinedButton(
                        onClick = { selectedDate = null },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Limpiar selección")
                    }
                }
            }

            // Mostrar el DatePickerDialog cuando sea necesario
            if (showDatePicker) {
                DatePickerPersonalized(
                    onDateSelected = { dateMillis ->
                        selectedDate = dateMillis
                    },
                    onDismiss = {
                        showDatePicker = false
                    },
                    initialDate = selectedDate ?: System.now().toEpochMilliseconds()
                )
            }

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
                            dueDate = selectedDate,
                            createdAt = System.now().toEpochMilliseconds(),
                            updatedAt = null,
                            assignedUserId = usuarioAsignado,
                            groupId = groupId
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
