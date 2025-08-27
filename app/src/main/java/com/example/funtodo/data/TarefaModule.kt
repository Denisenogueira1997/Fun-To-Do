package com.example.funtodo.di

import android.content.Context
import androidx.room.Room
import com.example.funtodo.data.TarefaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.funtodo.data.TarefaDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TarefaDatabase {
        return Room.databaseBuilder(
            appContext, TarefaDatabase::class.java, "tarefa_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTarefaDao(db: TarefaDatabase): TarefaDao {
        return db.tarefaDao()
    }
}
