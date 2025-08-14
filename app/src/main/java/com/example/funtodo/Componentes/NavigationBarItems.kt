package com.example.funtodo.Componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

object NavigationBarItems {
    val Sorteio = NavigationItem("sorteio", Icons.Default.Casino, "Sorteio")
    val Criar = NavigationItem("criar", Icons.Default.Add, "Criar")
    val Lista = NavigationItem("lista", Icons.Default.List, "Lista")
}

data class NavigationItem(val rota: String, val icon: ImageVector, val label: String)