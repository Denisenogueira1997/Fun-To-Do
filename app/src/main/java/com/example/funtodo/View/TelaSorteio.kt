import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    val tarefaSorteada by viewModel.tarefaSorteada.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val tarefasConcluidas = remember { mutableStateListOf<Int>() }
    var mostrarCheck by remember { mutableStateOf(false) }
    var mensagem by remember { mutableStateOf<String?>(null) }

    if (mostrarCheck) {
        LaunchedEffect(tarefaSorteada) {
            delay(1000)
            tarefaSorteada?.let { tarefasConcluidas.add(it.id) }
            mostrarCheck = false
            viewModel.limparTarefaSorteada()
        }
    }

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
            .consumeWindowInsets(PaddingValues())
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
                        viewModel.sortearTarefa(tarefas, tarefasConcluidas)
                    }
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Sortear Tarefa", color = MaterialTheme.colorScheme.onPrimary)
            }

            tarefaSorteada?.let { tarefa ->
                if (!tarefasConcluidas.contains(tarefa.id)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                            .background(
                                MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(12.dp)
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
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.background
                        )
                        Checkbox(
                            checked = mostrarCheck, onCheckedChange = { checked ->
                                if (checked) {
                                    mostrarCheck = true
                                    mensagem = "Tarefa concluída!🤠"
                                    scope.launch {
                                        delay(2000)
                                        mensagem = null
                                    }
                                }
                            }
                        )
                    }
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
