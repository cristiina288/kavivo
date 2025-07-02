package org.vi.be.kavivo.ui.groups

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import org.koin.compose.viewmodel.koinViewModel
import org.vi.be.kavivo.ui.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroupScreen(
    navController: NavController
    //onLoginSuccess: () -> Unit,
    //onRegisterSuccess: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Grupos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
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
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun Content(
    navController: NavController,
    modifier: Modifier
    //onLoginSuccess: () -> Unit,
    //onRegisterSuccess: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Crear grupo", "Unirse a un grupo")

    //val navigator = LocalNavigator.currentOrThrow
    val groupsViewModel = koinViewModel<GroupsViewModel>()

    val status by groupsViewModel.userStatus.collectAsState()
    var codeIsVisible by remember { mutableStateOf(false) }
    var joinedSuccessfullyVisible by remember { mutableStateOf(false) }
    val group by groupsViewModel.group.collectAsState()
    var groupName by remember { mutableStateOf(group?.title) }

    if (status) {
        navController.navigate(Routes.HOME)
    }

    if (group != null
        && group?.id?.isNotEmpty() == true
    ) {
        if (groupsViewModel.groupStatus.collectAsState().value) {
            codeIsVisible = true
        } else if (groupsViewModel.groupJoinedStatus.collectAsState().value) {
            joinedSuccessfullyVisible = true
        }
    }

    if (codeIsVisible) {
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
                    kotlinx.coroutines.delay(5000)
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
        }

    } else if (joinedSuccessfullyVisible) {
        Column(modifier = modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Te has unido al grupo $groupName.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crea o únete a un grupo para poder hacer una gestión compartida.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Pestañas para Login/Register
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Content based on selected tab
            when (selectedTabIndex) {
                0 -> AddNewGroupComponentContent(groupsViewModel)
                1 -> JoinToGroupComponentContent(groupsViewModel)
            }
        }
    }
}


@Composable
private fun JoinToGroupComponentContent(
    groupsViewModel: GroupsViewModel
) {
    var code by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (!groupsViewModel.groupJoinedStatus.collectAsState().value) {
        isLoading = false
        errorMessage = "Ha habido un error al unirte al grupo. Vuelve a intentarlo."
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "El dueño del grupo debe facilitarte el código para poder unirte.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Name Field
        OutlinedTextField(
            value = code,
            onValueChange = {
                code = it
                errorMessage = null
            },
            label = { Text("Código") },
            placeholder = { Text("El código del grupo") },
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
        /*
                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Contraseña") },
                    placeholder = { Text("Ingresa tu contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Lock else Icons.Default.Lock,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = errorMessage != null
                )

                // Error message
                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }*/

        Spacer(modifier = Modifier.height(8.dp))

        // Login Button
        Button(
            onClick = {
                if (code.isNotBlank()) { //&& password.isNotBlank()) {
                    isLoading = true

                    groupsViewModel.joinToGroup(code)


                    /*loginViewModel.loginUser(
                        userEmail = email,
                        userPassword = password,
                    )*/
                    // Aquí iría la lógica de login con Firebase
                    // Por ahora simulamos el login
                    /*loginViewModel.performLogin(email, password) { success, error ->
                        isLoading = false
                        if (success) {
                            onLoginSuccess()
                        } else {
                            errorMessage = error
                        }
                    }*/
                } else {
                    errorMessage = "Por favor completa todos los campos"
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
                    Text("Uniendote al grupo...")
                }
            } else {
                Text(
                    text = "Unirme al grupo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Forgot Password
        /*TextButton(
            onClick = { *//* Implementar recuperar contraseña *//* },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Olvidaste tu contraseña?")
        }*/
    }
}

@Composable
private fun AddNewGroupComponentContent(
    groupsViewModel: GroupsViewModel
) {
    var groupName by remember { mutableStateOf("") }
    //var password by remember { mutableStateOf("") }
    //var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (!groupsViewModel.groupStatus.collectAsState().value) {
        isLoading = false
        errorMessage = "Ha habido un error al crear el grupo. Vuelve a intentarlo."
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = "Introduce un nombre al grupo y créalo. Posteriormente te facilitaremos un código para que lo puedas compartir y se puedan unir a tu grupo.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Name Field
        OutlinedTextField(
            value = groupName,
            onValueChange = {
                groupName = it
                errorMessage = null
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

        // Password Field
        /*OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("Contraseña") },
            placeholder = { Text("Mínimo 6 caracteres") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Lock else Icons.Default.Lock,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = errorMessage != null
        )*/

        Spacer(modifier = Modifier.height(8.dp))

        // Register Button
        Button(
            onClick = {
                when {
                    groupName.isBlank() -> {//|| password.isBlank() -> {
                        errorMessage = "Por favor completa todos los campos"
                    }
                    /*password.length < 6 -> {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    }*/
                    else -> {
                        isLoading = true

                        groupsViewModel.addGroup(groupName)
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
                    Text("Creando grupo...")
                }
            } else {
                Text(
                    text = "Crear grupo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}