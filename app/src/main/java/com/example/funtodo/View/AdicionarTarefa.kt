package com.example.funtodo.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.funtodo.viewmodel.TarefaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarTarefa(
    viewModel: TarefaViewModel = hiltViewModel(), modifier: Modifier = Modifier
) {
    var titulo by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("") }
    var mostrarPicker by remember { mutableStateOf(false) }
    var mostrarMensagem by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Adicionar Tarefa",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.fillMaxSize()
    ) { padding ->

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(16.dp)
                    .imePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)
                ) {
                    Text(
                        text = emoji,
                        fontSize = 36.sp,
                        modifier = Modifier.padding(end = 16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .background(MaterialTheme.colorScheme.onPrimary)
                        ) {
                            TextField(
                                value = titulo,
                                onValueChange = { titulo = it },
                                placeholder = {
                                    Text(
                                        "Título da tarefa",
                                        color = MaterialTheme.colorScheme.background
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
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (mostrarPicker) {
                    AndroidView(
                        factory = { context ->
                            EmojiPickerView(context).apply {
                                setOnEmojiPickedListener { emojiSelecionado ->
                                    emoji = emojiSelecionado.emoji
                                    mostrarPicker = false
                                }
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
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
                        }
                    }, modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Salvar")
                }
            }


            if (mostrarMensagem) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 100.dp)
                        .background(
                            MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12.dp)
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
        }
    }
}




