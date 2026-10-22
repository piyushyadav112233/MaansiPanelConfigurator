package com.maansiauto.panelconfigurator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
    primary = Navy,
    onPrimary = Color(0xFFFFFFFF),
    secondary = Amber,
    background = Background,
    surface = Surface,
    onBackground = Ink,
    onSurface = Ink,
    error = Red
)

@Composable
fun MaansiPanelConfiguratorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}
