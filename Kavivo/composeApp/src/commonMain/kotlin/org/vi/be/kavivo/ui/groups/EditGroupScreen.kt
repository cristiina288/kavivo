package org.vi.be.kavivo.ui.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.vi.be.kavivo.domain.groups.models.GroupModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGroupScreen(
    navController: NavController,
    groupSelected: GroupModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar grupo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("shouldReloadGroups", true)

                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Content(
            navController,
            modifier = Modifier.padding(innerPadding),
            groupSelected
        )
    }
}


@Composable
private fun Content(
    navController: NavController,
    modifier: Modifier,
    groupSelected: GroupModel
) {
    val groupsViewModel = koinViewModel<GroupsViewModel>()

    groupsViewModel.getGroupForEdit(groupSelected)

    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "El codigo a compartir es el siguiente:",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Card clickeable para copiar
        val codeText = groupsViewModel.group.value?.id ?: ""
        val clipboardManager = LocalClipboardManager.current
        var showCopiedMessage by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clickable {
                    if (codeText.isNotEmpty()) {
                        clipboardManager.setText(AnnotatedString(codeText))
                        showCopiedMessage = true
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = codeText,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (showCopiedMessage) Icons.Default.Check else Icons.Default.Send,
                    contentDescription = "Copiar código",
                    tint = if (showCopiedMessage) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Auto-resetear el mensaje después de 2 segundos
        LaunchedEffect(showCopiedMessage) {
            if (showCopiedMessage) {
                delay(5000)
                showCopiedMessage = false
            }
        }

        // Texto de ayuda
        Text(
            text = if (showCopiedMessage) "¡Código copiado! Enviaselo a quien quieras que se una a tu grupo" else "Toca para copiar",
            fontSize = 12.sp,
            color = if (showCopiedMessage)
                MaterialTheme.colorScheme.tertiary
            else
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        InfoGroup(groupsViewModel)
    }
}


@Composable
private fun InfoGroup(
    groupsViewModel: GroupsViewModel,
) {
    var groupName = groupsViewModel.group.collectAsState().value?.title ?: ""
    var editGroupName by remember { mutableStateOf(groupName) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    errorMessage = groupsViewModel.errorMessage.collectAsState().value

    if (!groupsViewModel.groupStatus.collectAsState().value) {
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

       /* Text(
            text = "Introduce un nombre al grupo y créalo. Posteriormente te facilitaremos un código para que lo puedas compartir y se puedan unir a tu grupo.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )*/

        // Name Field
        OutlinedTextField(
            value = editGroupName,
            onValueChange = {
                editGroupName = it
                groupsViewModel.addErrorMessage(null)
            },
            label = { Text("Nombre") },
            placeholder = { Text("Tu nombre") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage != null
        )

        errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Save Button
        Button(
            onClick = {
                when {
                    editGroupName.isBlank() -> {//|| password.isBlank() -> {
                        groupsViewModel.addErrorMessage("Por favor completa todos los campos")
                    }
                    /*password.length < 6 -> {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    }*/
                    else -> {
                        isLoading = true

                        groupsViewModel.editName(editGroupName)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading
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
                    Text("Guardando grupo...")
                }
            } else {
                Text(
                    text = "Guardar grupo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}