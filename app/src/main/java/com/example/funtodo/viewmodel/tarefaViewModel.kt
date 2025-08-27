package com.example.funtodo.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.funtodo.data.Tarefa
import com.example.funtodo.data.TarefaDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TarefaViewModel @Inject constructor(private val dao: TarefaDao) : ViewModel() {

    val tarefas: Flow<List<Tarefa>> = dao.listarTodas()

    private val _tarefaSorteada = MutableStateFlow<Tarefa?>(null)
    val tarefaSorteada: StateFlow<Tarefa?> = _tarefaSorteada

    fun sortearTarefa(tarefas: List<Tarefa>, tarefasConcluidas: List<Int>) {
        val pendentes = tarefas.filter { !tarefasConcluidas.contains(it.id) }
        if (pendentes.isNotEmpty()) {
            _tarefaSorteada.value = pendentes.random()
        } else {
            _tarefaSorteada.value = null
        }
    }

    fun limparTarefaSorteada() {
        _tarefaSorteada.value = null
    }

    fun salvarTarefa(titulo: String, emoji: String) {
        val novaTarefa = Tarefa(titulo = titulo, emoji = emoji)
        viewModelScope.launch {
            dao.inserir(novaTarefa)
        }
    }

    fun deletarTarefa(tarefa: Tarefa) {
        viewModelScope.launch {
            dao.delete(tarefa)
        }
    }

}
