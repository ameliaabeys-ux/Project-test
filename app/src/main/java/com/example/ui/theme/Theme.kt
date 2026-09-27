package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CoffeeGold,
    secondary = CoffeeAmber,
    tertiary = CoffeeAccent,
    background = CoffeeBg,
    surface = CoffeeBg,
    surfaceVariant = CoffeeCard,
    onPrimary = Color(0xFF180A06),
    onSecondary = Color(0xFF180A06),
    onTertiary = Color(0xFF180A06),
    onBackground = CoffeeTextPrimary,
    onSurface = CoffeeTextPrimary,
    onSurfaceVariant = CoffeeTextMuted
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
