package com.example.funtodo.View.Componentes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.funtodo.viewmodel.TarefaViewModel

@Composable
fun Lista(viewModel: TarefaViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val tarefas by viewModel.tarefas.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(tarefas) { tarefa ->
            Card(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tarefa.emoji,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.background
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = tarefa.titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.background
                    )

                    IconButton(onClick = { viewModel.deletarTarefa(tarefa) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Excluir tarefa",
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }
    }
}
