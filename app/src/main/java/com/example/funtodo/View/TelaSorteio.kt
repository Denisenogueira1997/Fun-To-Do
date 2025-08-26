import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.funtodo.data.Tarefa
import com.example.funtodo.viewmodel.TarefaViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaSorteio(viewModel: TarefaViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val tarefas by viewModel.tarefas.collectAsState(initial = emptyList())
    val context = LocalContext.current

    var tarefaSorteada by remember { mutableStateOf<Tarefa?>(null) }
    val tarefasConcluidas = remember { mutableStateListOf<Int>() }
    var mostrarCheck by remember { mutableStateOf(false) }


    if (mostrarCheck) {
        LaunchedEffect(tarefaSorteada) {
            delay(1000)
            tarefaSorteada?.let { tarefasConcluidas.add(it.id) }
            mostrarCheck = false
            tarefaSorteada = null
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
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(padding)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .statusBarsPadding()
        ) {


            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
                    val pendentes = tarefas.filter { !tarefasConcluidas.contains(it.id) }

                    if (pendentes.isEmpty()) {
                        Toast.makeText(context, "Nenhuma tarefa salva ainda!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        tarefaSorteada = pendentes.random()
                    }
                },
                shape = RoundedCornerShape(16.dp),
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
                            .padding(horizontal = 24.dp)
                            .background(MaterialTheme.colorScheme.onPrimary)
                            .padding(16.dp)
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
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
