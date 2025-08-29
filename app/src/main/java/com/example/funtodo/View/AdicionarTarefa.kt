package com.example.funtodo.View

import android.R
import android.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InsertEmoticon
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.funtodo.View.Componentes.BottomNavBar
import com.example.funtodo.viewmodel.TarefaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarTarefa(
    navController: NavController, viewModel: TarefaViewModel = hiltViewModel()
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var emoji by rememberSaveable { mutableStateOf("") }
    var mostrarPicker by remember { mutableStateOf(false) }
    var mostrarMensagem by remember { mutableStateOf(false) }
    var mostrarErro by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val themedContext = ContextThemeWrapper(context, R.style.Theme_Material_Light)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Adicionar Tarefa",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary)
            )
        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .background(MaterialTheme.colorScheme.onPrimary)
                    ) {
                        if (emoji.isNotEmpty()) {
                            Text(
                                text = emoji,
                                fontSize = 28.sp,
                                modifier = Modifier.padding(end = 8.dp),
                                color = MaterialTheme.colorScheme.background
                            )
                        }

                        TextField(
                            value = titulo,
                            onValueChange = { titulo = it },
                            placeholder = {
                                Text(
                                    "Título da tarefa", color = MaterialTheme.colorScheme.background
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.background,
                                unfocusedTextColor = MaterialTheme.colorScheme.background,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        IconButton(onClick = { mostrarPicker = !mostrarPicker }) {
                            Icon(
                                imageVector = Icons.Default.InsertEmoticon,
                                contentDescription = "Selecionar Emoji",
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (titulo.isNotBlank()) {
                            viewModel.salvarTarefa(titulo, emoji)
                            titulo = ""
                            emoji = ""
                            mostrarPicker = false
                            keyboardController?.hide()
                            mostrarMensagem = true
                            scope.launch {
                                delay(2000)
                                mostrarMensagem = false
                            }
                        } else {
                            mostrarErro = true
                            scope.launch {
                                delay(2000)
                                mostrarErro = false
                            }
                        }

                    }, shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Salvar")
                }
            }

            if (mostrarPicker) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .align(Alignment.BottomCenter)
                        .background(Color.White)
                ) {
                    AndroidView(
                        factory = {
                            EmojiPickerView(themedContext).apply {
                                setOnEmojiPickedListener { e ->
                                    emoji = e.emoji
                                    mostrarPicker = false
                                }
                            }
                        }, modifier = Modifier.fillMaxSize()
                    )
                }
            }

            if (mostrarMensagem) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tarefa salva 🤠",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 20.sp
                    )
                }
            }
            if (mostrarErro) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Digite um título para a tarefa ❌",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
