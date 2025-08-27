package com.example.funtodo

import TelaSorteio
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.funtodo.View.AdicionarTarefa
import com.example.funtodo.View.Componentes.NavigationBarItems
import com.example.funtodo.View.ListaTarefas
import com.example.funtodo.ui.theme.CustomColor
import com.example.funtodo.viewmodel.TarefaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CustomColor {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = NavigationBarItems.Sorteio.rota
    ) {
        composable(NavigationBarItems.Sorteio.rota) {
            val viewModel: TarefaViewModel = hiltViewModel()
            TelaSorteio(navController = navController, viewModel = viewModel)
        }
        composable(NavigationBarItems.Criar.rota) {
            val viewModel: TarefaViewModel = hiltViewModel()
            AdicionarTarefa(navController = navController, viewModel = viewModel)
        }
        composable(NavigationBarItems.Lista.rota) {
            val viewModel: TarefaViewModel = hiltViewModel()
            ListaTarefas(navController = navController, viewModel = viewModel)
        }
    }
}

