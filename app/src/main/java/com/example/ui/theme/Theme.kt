package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NeoLightColorScheme = lightColorScheme(
    primary = GankColors.GankYellow,
    onPrimary = GankColors.Ink,
    secondary = GankColors.NeonBlue,
    onSecondary = GankColors.Ink,
    background = GankColors.Paper,
    onBackground = GankColors.Ink,
    surface = GankColors.White,
    onSurface = GankColors.Ink,
    error = GankColors.Danger,
    onError = GankColors.White
)

private val NeoDarkColorScheme = darkColorScheme(
    primary = GankColors.GankYellow,
    onPrimary = GankColors.Ink,
    secondary = GankColors.NeonBlue,
    onSecondary = GankColors.Ink,
    background = GankColors.Ink,
    onBackground = GankColors.Paper,
    surface = GankColors.Ink,
    onSurface = GankColors.White,
    error = GankColors.Danger,
    onError = GankColors.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NeoDarkColorScheme else NeoLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
