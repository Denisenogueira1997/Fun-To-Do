package com.example.funtodo.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val userBaseColor = Color(0xFF934336)

@Composable
fun CustomColor(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) darkColorScheme(
        primary = userBaseColor,
        onPrimary = Color.White,
        secondary = userBaseColor.copy(alpha = 0.8f),
        onSecondary = Color.White,
        tertiary = userBaseColor.copy(alpha = 0.6f),
        onTertiary = Color.White,
        background = Color(0xFF121212),
        onBackground = Color.White,
        surface = Color(0xFF1E1E1E),
        onSurface = Color.White
    ) else lightColorScheme(
        primary = userBaseColor,
        onPrimary = Color.White,
        secondary = userBaseColor.copy(alpha = 0.8f),
        onSecondary = Color.White,
        tertiary = userBaseColor.copy(alpha = 0.6f),
        onTertiary = Color.White,
        background = Color(0xFFFFFFFF),
        onBackground = Color.Black,
        surface = Color(0xFFF5F5F5),
        onSurface = Color.Black
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

