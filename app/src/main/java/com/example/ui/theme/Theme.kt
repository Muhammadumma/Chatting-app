package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = WhatsAppGreenLight,
    onPrimary = Color.Black,
    primaryContainer = WhatsAppDarkOutgoingBubble,
    onPrimaryContainer = WhatsAppGreenContainer,
    secondary = WhatsAppGreen,
    onSecondary = Color.Black,
    background = WhatsAppDarkBackground,
    onBackground = WhatsAppDarkTextPrimary,
    surface = WhatsAppDarkSurface,
    onSurface = WhatsAppDarkTextPrimary,
    surfaceVariant = WhatsAppDarkIncomingBubble,
    onSurfaceVariant = WhatsAppDarkTextSecondary,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = WhatsAppGreen,
    onPrimary = Color.White,
    primaryContainer = WhatsAppGreenContainer,
    onPrimaryContainer = WhatsAppGreenDeep,
    secondary = WhatsAppGreenLight,
    onSecondary = Color.White,
    background = WhatsAppBackground,
    onBackground = WhatsAppTextPrimary,
    surface = WhatsAppSurface,
    onSurface = WhatsAppTextPrimary,
    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = WhatsAppTextSecondary,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve WhatsApp iconic branding
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
