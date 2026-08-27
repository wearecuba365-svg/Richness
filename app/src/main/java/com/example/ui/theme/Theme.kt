package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Supported UI Theme Modes: Dark, Light, and System
 */
enum class AppThemeMode(
    val title: String,
    val subtitle: String
) {
    DARK("Dark", "Sovereign Matte-Black & Gold"),
    LIGHT("Light", "Imperial Ivory & Antique Gold"),
    SYSTEM("System", "Syncs with Device Appearance");

    companion object {
        fun fromString(value: String?): AppThemeMode {
            return when (value?.trim()?.uppercase()) {
                "LIGHT" -> LIGHT
                "SYSTEM" -> SYSTEM
                else -> DARK
            }
        }
    }
}

val LocalAppThemeMode = compositionLocalOf { AppThemeMode.DARK }
val LocalIsDarkTheme = compositionLocalOf { true }

/**
 * Custom Material 3 Luxury Dark Color Scheme
 * - Background & Scrim: Rich Black (#0A0A0A)
 * - Surface: Deep Charcoal (#121212)
 * - Primary & Accents: Metallic Gold variants (#D4AF37, #FFE082, #9E781B, #E5C158)
 * - Secondary: Amber Accent variants (#FFB300, #FFC107, #FF8F00, #FFD54F)
 */
val LuxuryDarkColorScheme: ColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = RichBlack,
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldLight,
    inversePrimary = GoldLight,
    secondary = AmberAccent,
    onSecondary = RichBlack,
    secondaryContainer = SurfaceElevated,
    onSecondaryContainer = AmberBright,
    tertiary = GoldLight,
    onTertiary = RichBlack,
    tertiaryContainer = SurfaceHighlight,
    onTertiaryContainer = AmberWarm,
    background = RichBlack,
    onBackground = TextPrimary,
    surface = DarkCharcoal,
    onSurface = TextPrimary,
    surfaceVariant = BrushedMetal,
    onSurfaceVariant = TextSecondary,
    surfaceTint = GoldPrimary,
    inverseSurface = TextPrimary,
    inverseOnSurface = DarkCharcoal,
    outline = DarkBorder,
    outlineVariant = DarkCardBorder,
    error = ErrorRed,
    onError = RichBlack,
    errorContainer = Color(0xFF3B1212),
    onErrorContainer = Color(0xFFFFB4AB),
    scrim = RichBlack,
    surfaceContainerLowest = RichBlack,
    surfaceContainerLow = DarkCharcoal,
    surfaceContainer = BrushedMetal,
    surfaceContainerHigh = SurfaceElevated,
    surfaceContainerHighest = SurfaceHighlight
)

/**
 * Custom Material 3 Luxury Light Color Scheme
 * - Background: Alabaster Canvas (#F7F5F0)
 * - Surface: Imperial Ivory (#FFFFFF, #FAF8F5)
 * - Primary & Accents: Burnished Antique Gold (#9E781B, #B8860B)
 * - Secondary: Amber Tone (#D97706, #B45309)
 */
val LuxuryLightColorScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF9E781B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFBF4DC),
    onPrimaryContainer = Color(0xFF382A05),
    inversePrimary = GoldLight,
    secondary = Color(0xFFD97706),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = LightElevated,
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = Color(0xFFB8860B),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = LightHighlight,
    onTertiaryContainer = Color(0xFF451A03),
    background = LightAlabaster,
    onBackground = LightTextPrimary,
    surface = LightIvory,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCardSurface,
    onSurfaceVariant = LightTextSecondary,
    surfaceTint = GoldPrimary,
    inverseSurface = DarkCharcoal,
    inverseOnSurface = TextPrimary,
    outline = LightBorder,
    outlineVariant = LightCardBorder,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    scrim = Color(0x66000000),
    surfaceContainerLowest = LightIvory,
    surfaceContainerLow = LightSurface,
    surfaceContainer = LightCardSurface,
    surfaceContainerHigh = LightElevated,
    surfaceContainerHighest = LightHighlight
)

@Composable
fun TheRichesProtocolTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> systemInDark
    }

    val activeColorScheme = if (isDark) LuxuryDarkColorScheme else LuxuryLightColorScheme

    CompositionLocalProvider(
        LocalAppThemeMode provides themeMode,
        LocalIsDarkTheme provides isDark
    ) {
        MaterialTheme(
            colorScheme = activeColorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    TheRichesProtocolTheme(themeMode = themeMode, content = content)
}

