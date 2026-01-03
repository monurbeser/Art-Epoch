// File: app/src/main/java/com/artepoch/ui/theme/Theme.kt
package com.artepoch.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme

private val LightColors = lightColorScheme(
    primary = ArtEpochColors.Primary,
    secondary = ArtEpochColors.Secondary,
    background = ArtEpochColors.Background,
    surface = ArtEpochColors.Surface,
    onPrimary = ArtEpochColors.OnPrimary,
    onSecondary = ArtEpochColors.OnSecondary,
    onBackground = ArtEpochColors.OnBackground,
    onSurface = ArtEpochColors.OnSurface
)

private val DarkColors = darkColorScheme(
    primary = ArtEpochColors.PrimaryDark,
    secondary = ArtEpochColors.SecondaryDark,
    background = ArtEpochColors.BackgroundDark,
    surface = ArtEpochColors.SurfaceDark,
    onPrimary = ArtEpochColors.OnPrimaryDark,
    onSecondary = ArtEpochColors.OnSecondaryDark,
    onBackground = ArtEpochColors.OnBackgroundDark,
    onSurface = ArtEpochColors.OnSurfaceDark
)

@Composable
fun ArtEpochTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = ArtEpochTypography,
        content = content
    )
}
