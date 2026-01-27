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
class TarefaViewModel @Inject constructor(
    private val dao: TarefaDao
) : ViewModel() {

    val tarefas: Flow<List<Tarefa>> = dao.listarTodas()

    private val _tarefasSorteadas = MutableStateFlow<List<Tarefa>>(emptyList())
    val tarefasSorteadas: StateFlow<List<Tarefa>> = _tarefasSorteadas


    init {
        restaurarTarefaSorteada()
    }

    private fun restaurarTarefaSorteada() {
        viewModelScope.launch {
            _tarefasSorteadas.value = dao.obterTarefasSorteadas()
        }
    }

    fun sortearTarefas(
        tarefas: List<Tarefa>,
        tarefasConcluidas: List<Int>
    ) {
        viewModelScope.launch {
            dao.limparTarefaSorteada()

            val pendentes = tarefas.filter { it.id !in tarefasConcluidas }

            if (pendentes.isNotEmpty()) {
                val sorteadas = pendentes
                    .shuffled()
                    .take(3)

                sorteadas.forEach {
                    dao.marcarComoSorteada(it.id)
                }

                _tarefasSorteadas.value = sorteadas.map {
                    it.copy(sorteada = true)
                }
            }
        }
    }

    fun limparTarefaSorteada() {
        viewModelScope.launch {
            dao.limparTarefaSorteada()
            _tarefasSorteadas.value = emptyList()
        }
    }

    fun salvarTarefa(titulo: String, emoji: String) {
        viewModelScope.launch {
            dao.inserir(Tarefa(titulo = titulo, emoji = emoji))
        }
    }

    fun deletarTarefa(tarefa: Tarefa) {
        viewModelScope.launch {
            dao.delete(tarefa)
        }
    }
}
