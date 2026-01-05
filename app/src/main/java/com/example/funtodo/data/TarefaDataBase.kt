package com.example.funtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Tarefa::class], version = 2, exportSchema = false)
abstract class TarefaDatabase : RoomDatabase() {
    abstract fun tarefaDao(): TarefaDao
}
