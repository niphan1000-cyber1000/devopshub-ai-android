package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = CyberCyan,
    secondary = NeonPurple,
    tertiary = NeonGreen,
    background = ObsidianBlack,
    surface = DarkGreyCard,
    onPrimary = ObsidianBlack,
    onSecondary = PureWhite,
    onBackground = GhostWhite,
    onSurface = GhostWhite,
    error = HotCoral
  )

private val LightColorScheme = DarkColorScheme // DevOps console app should persist the elegant dark theme

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to true for the dark developer console aesthetic
  dynamicColor: Boolean = false, // Disable dynamic color to enforce our unique cyberpunk branding
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
