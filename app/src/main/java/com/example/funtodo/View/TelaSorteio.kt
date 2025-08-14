import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.funtodo.data.Tarefa
import com.example.funtodo.viewmodel.TarefaViewModel
import kotlinx.coroutines.delay

@Composable
fun TelaSorteio(viewModel: TarefaViewModel = hiltViewModel()) {
    val tarefas by viewModel.tarefas.collectAsState(initial = emptyList())
    val context = LocalContext.current

    var tarefaSorteada by remember { mutableStateOf<Tarefa?>(null) }
    val tarefasConcluidas = remember { mutableStateListOf<Int>() }
    var mostrarCheck by remember { mutableStateOf(false) }
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    if (mostrarCheck) {
        LaunchedEffect(tarefaSorteada) {
            delay(1000)
            tarefaSorteada?.let { tarefasConcluidas.add(it.id) }
            mostrarCheck = false
            tarefaSorteada = null
        }
    }
    Scaffold(
        contentWindowInsets = WindowInsets(0), topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp + statusBarPadding)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sorteador de Tarefas",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {


            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
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
                Text("Sortear Tarefa", color = MaterialTheme.colorScheme.tertiary)
            }

            tarefaSorteada?.let { tarefa ->
                if (!tarefasConcluidas.contains(tarefa.id)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(y = (-80).dp)
                            .padding(horizontal = 24.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Text(
                            tarefa.emoji,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            tarefa.titulo,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.tertiary
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
