package com.example.ytclient.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Red = Color(0xFFFF0000)
private val DarkBg = Color(0xFF0F0F0F)
private val DarkSurface = Color(0xFF1A1A1A)

private val DarkColors = darkColorScheme(
    primary = Red,
    secondary = Red,
    background = DarkBg,
    surface = DarkSurface
)

private val LightColors = lightColorScheme(
    primary = Red,
    secondary = Red
)

@Composable
fun YTClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
