import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.funtodo.View.Componentes.BottomNavBar
import com.example.funtodo.viewmodel.TarefaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaSorteio(
    navController: NavController,
    viewModel: TarefaViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val tarefas by viewModel.tarefas.collectAsState(initial = emptyList())
    val tarefasSorteadas by viewModel.tarefasSorteadas.collectAsState()
    val scope = rememberCoroutineScope()

    val tarefasConcluidas = remember { mutableStateListOf<Int>() }
    var mensagem by remember { mutableStateOf<String?>(null) }
    val checksConcluidos = remember { mutableStateMapOf<Int, Boolean>() }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tela de Sorteio",
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(innerPadding)


        ) {

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
                    val pendentes = tarefas.filter { !tarefasConcluidas.contains(it.id) }
                    if (pendentes.isEmpty()) {
                        mensagem = "Nenhuma tarefa salva ainda!😔 "
                        scope.launch {
                            delay(2000)
                            mensagem = null
                        }
                    } else {
                        viewModel.sortearTarefas(tarefas, tarefasConcluidas)
                    }
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Sortear as Tarefas", color = MaterialTheme.colorScheme.onPrimary)
            }
            if (tarefasSorteadas.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .zIndex(1f)
                        .background(
                            MaterialTheme.colorScheme.onPrimary,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            "Tarefas Sorteadas 🎯",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.background
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        tarefasSorteadas.forEach { tarefa ->

                            if (!tarefasConcluidas.contains(tarefa.id)) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.onPrimary,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .height(60.dp)
                                        .padding(horizontal = 12.dp)
                                ) {

                                    Text(
                                        tarefa.emoji,
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = MaterialTheme.colorScheme.background
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        tarefa.titulo,
                                        modifier = Modifier.weight(1f),
                                        color = MaterialTheme.colorScheme.background
                                    )

                                    Checkbox(
                                        checked = checksConcluidos[tarefa.id] == true,
                                        onCheckedChange = { checked ->
                                            if (checked) {
                                                checksConcluidos[tarefa.id] = true
                                                tarefasConcluidas.add(tarefa.id)

                                                mensagem = "Tarefa concluída! 🤠"
                                                scope.launch {
                                                    delay(1500)
                                                    mensagem = null
                                                }

                                            }
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            LaunchedEffect(tarefasConcluidas.size, tarefasSorteadas.size) {
                if (
                    tarefasSorteadas.isNotEmpty() &&
                    tarefasConcluidas.containsAll(tarefasSorteadas.map { it.id })
                ) {
                    viewModel.limparTarefaSorteada()
                    checksConcluidos.clear()
                }
            }



            mensagem?.let { msg ->
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }

        }
    }
}

