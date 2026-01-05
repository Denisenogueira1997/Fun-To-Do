package com.example.funtodo.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TarefaDao {
    @Insert
    suspend fun inserir(tarefa: Tarefa)

    @Delete
    suspend fun delete(tarefa: Tarefa)

    @Query("SELECT * FROM tarefas ORDER BY id DESC")
    fun listarTodas(): Flow<List<Tarefa>>

    @Query("SELECT * FROM tarefas WHERE sorteada = 1 LIMIT 1")
    suspend fun obterTarefaSorteada(): Tarefa?

    @Query("UPDATE tarefas SET sorteada = 0")
    suspend fun limparTarefaSorteada()

    @Query("UPDATE tarefas SET sorteada = 1 WHERE id = :id")
    suspend fun marcarComoSorteada(id: Int)


}
