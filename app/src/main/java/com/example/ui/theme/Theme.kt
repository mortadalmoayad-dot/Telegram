package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class TelegramExtendedColors(
    val topBarBackground: Color,
    val topBarContent: Color,
    val outgoingBubble: Color,
    val incomingBubble: Color,
    val outgoingText: Color,
    val incomingText: Color,
    val outgoingTime: Color,
    val incomingTime: Color,
    val checkmarkColor: Color,
    val dateChipBackground: Color,
    val fabColor: Color,
    val unreadBadge: Color,
    val mutedBadge: Color,
    val chatWallpaperColor: Color,
    val isDark: Boolean
)

val LocalTelegramColors = staticCompositionLocalOf {
    TelegramExtendedColors(
        topBarBackground = TelegramTopBarLight,
        topBarContent = Color.White,
        outgoingBubble = TelegramOutgoingBubbleLight,
        incomingBubble = TelegramIncomingBubbleLight,
        outgoingText = TelegramOutgoingTextLight,
        incomingText = TelegramIncomingTextLight,
        outgoingTime = TelegramTimeTextLight,
        incomingTime = TelegramSubtextLight,
        checkmarkColor = TelegramCheckmarkBlue,
        dateChipBackground = TelegramDateChipBg,
        fabColor = TelegramLightBlue,
        unreadBadge = TelegramLightBlue,
        mutedBadge = Color(0xFFC4C9CC),
        chatWallpaperColor = Color(0xFFE6ECE0),
        isDark = false
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = TelegramLightBlue,
    onPrimary = Color.White,
    primaryContainer = TelegramNightSurface,
    onPrimaryContainer = Color.White,
    secondary = TelegramCyan,
    background = TelegramNightBg,
    surface = TelegramNightSurface,
    onBackground = Color.White,
    onSurface = Color.White,
    outline = TelegramNightDivider
)

private val LightColorScheme = lightColorScheme(
    primary = TelegramBlue,
    onPrimary = Color.White,
    primaryContainer = TelegramTopBarLight,
    onPrimaryContainer = Color.White,
    secondary = TelegramLightBlue,
    background = TelegramBgLight,
    surface = TelegramSurfaceLight,
    onBackground = Color.Black,
    onSurface = Color.Black,
    outline = TelegramDividerLight
)

@Composable
fun TelegramTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val extendedColors = if (darkTheme) {
        TelegramExtendedColors(
            topBarBackground = TelegramNightTopBar,
            topBarContent = Color.White,
            outgoingBubble = TelegramOutgoingBubbleDark,
            incomingBubble = TelegramIncomingBubbleDark,
            outgoingText = TelegramOutgoingTextDark,
            incomingText = TelegramIncomingTextDark,
            outgoingTime = TelegramTimeTextDark,
            incomingTime = TelegramNightSubtext,
            checkmarkColor = TelegramLightBlue,
            dateChipBackground = Color(0x66000000),
            fabColor = TelegramLightBlue,
            unreadBadge = TelegramLightBlue,
            mutedBadge = Color(0xFF4A5664),
            chatWallpaperColor = TelegramNightBg,
            isDark = true
        )
    } else {
        TelegramExtendedColors(
            topBarBackground = TelegramTopBarLight,
            topBarContent = Color.White,
            outgoingBubble = TelegramOutgoingBubbleLight,
            incomingBubble = TelegramIncomingBubbleLight,
            outgoingText = TelegramOutgoingTextLight,
            incomingText = TelegramIncomingTextLight,
            outgoingTime = TelegramTimeTextLight,
            incomingTime = TelegramSubtextLight,
            checkmarkColor = TelegramCheckmarkBlue,
            dateChipBackground = TelegramDateChipBg,
            fabColor = TelegramLightBlue,
            unreadBadge = TelegramLightBlue,
            mutedBadge = Color(0xFFC4C9CC),
            chatWallpaperColor = Color(0xFFD4E5D4),
            isDark = false
        )
    }

    CompositionLocalProvider(LocalTelegramColors provides extendedColors) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography = Typography,
            content = content
        )
    }
}
