package org.vi.be.kavivo.ui.feed


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.vi.be.kavivo.domain.comments.models.CommentModel
import org.vi.be.kavivo.ui.Routes
import org.vi.be.kavivo.ui.helpers.formatDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen (
    navController: NavHostController
) {
    val feedViewModel = koinViewModel<FeedViewModel>()

    val comments by feedViewModel.comments.collectAsState(initial = emptyList())

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val shouldReload = savedStateHandle?.get<Boolean>("shouldReloadComments") == true

    LaunchedEffect(shouldReload) {
        if (shouldReload) {
            feedViewModel.getAllComments() // o la función que tengas
            savedStateHandle?.remove<Boolean>("shouldReloadComments")
        }
    }

    Scaffold { paddingValues ->
        if (comments?.isEmpty() == true) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay publicaciones")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                comments?.forEachIndexed { index, comment ->

                    CommentRow(comment)

                    if (index != comments?.lastIndex) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CommentRow(comment: CommentModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar círculo (puede usar un placeholder)
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color = Color.Gray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Aquí puedes poner la imagen del avatar si la tienes,
            // por ejemplo con Coil o painterResource
            // Por ahora un texto placeholder con iniciales:
            Text(
                text = comment.userName.first().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.userName,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatDate(comment.createdAt),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.comment,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}
