package com.example.funtodo

import TelaSorteio
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.funtodo.Componentes.BottomNavBar
import com.example.funtodo.Componentes.NavigationBarItems
import com.example.funtodo.View.AdicionarTarefa
import com.example.funtodo.View.MainScreen
import com.example.funtodo.ui.theme.CustomColor
import com.example.funtodo.viewmodel.TarefaViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CustomColor {
                val navController = rememberNavController()
                val viewModel: TarefaViewModel = hiltViewModel()

                Scaffold(
                    bottomBar = {
                        BottomNavBar(navController = navController)
                    }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavigationBarItems.Sorteio.rota,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(NavigationBarItems.Sorteio.rota) {
                            TelaSorteio(viewModel = viewModel)
                        }
                        composable(NavigationBarItems.Criar.rota) {
                            AdicionarTarefa(viewModel = viewModel)
                        }
                        composable(NavigationBarItems.Lista.rota) {
                            MainScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}



