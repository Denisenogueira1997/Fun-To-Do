package com.example.funtodo.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.funtodo.View.Componentes.BottomNavBar
import com.example.funtodo.View.Componentes.Lista
import com.example.funtodo.viewmodel.TarefaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTarefas(
    navController: NavController,
    viewModel: TarefaViewModel = hiltViewModel(),
    onAdicionarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val listaTarefas by viewModel.tarefas.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Tarefas",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )


                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )


            )

        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .fillMaxSize()

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally


        ) {

            if (listaTarefas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center

                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        Text(
                            text = "Nenhuma tarefa salva 😔",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(
                            text = ("Adicione uma tarefa 🤠"),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            } else {
                Lista(
                    viewModel = viewModel, modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()


                )
            }
        }
    }
}





