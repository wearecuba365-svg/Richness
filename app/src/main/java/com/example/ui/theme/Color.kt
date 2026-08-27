package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core Luxury Palette: Matte-Black, Deep Charcoal, Sovereign Gold, Amber
val PureBlack = Color(0xFF000000)
val NightBlack = Color(0xFF0F0F0F)
val RichBlack = Color(0xFF0A0A0A) // Rich Black
val DarkCharcoal = Color(0xFF121212) // Deep Charcoal
val BrushedMetal = Color(0xFF1A1A1A) // Brushed Charcoal
val SurfaceElevated = Color(0xFF202020) // Elevated Dark Surface
val SurfaceHighlight = Color(0xFF2A2A2A) // Highlighted Surface

// Metallic Gold Palette
val GoldPrimary = Color(0xFFD4AF37) // Classic Metallic Gold
val GoldLight = Color(0xFFFFE082) // Radiant Light Gold
val GoldDark = Color(0xFF9E781B) // Antique Dark Gold
val GoldChampagne = Color(0xFFF3E5AB) // Champagne Gold
val GoldMetallic = Color(0xFFE5C158) // Brushed Metallic Gold
val GoldAura = Color(0x2BD4AF37) // Translucent Gold Aura
val MutedGold = Color(0xFFC5A059) // Muted Soft Gold

// Amber Accent Palette
val AmberAccent = Color(0xFFFFB300) // Vibrant Amber Accent
val AmberBright = Color(0xFFFFC107) // Bright Glowing Amber
val AmberDeep = Color(0xFFFF8F00) // Deep Burnished Amber
val AmberWarm = Color(0xFFFFD54F) // Warm Honey Amber

// Glows and Translucent Borders
val GoldGlow = Color(0x40D4AF37)
val AmberGlow = Color(0x40FFB300)
val DarkBorder = Color(0x40D4AF37)
val GoldBorder = DarkBorder
val DarkCardBorder = Color(0x33FFFFFF)
val DarkCard = SurfaceElevated

// Light Luxury Palette: Alabaster, Imperial Ivory, Champagne White, Antique Gold
val LightAlabaster = Color(0xFFF7F5F0) // Alabaster background canvas
val LightIvory = Color(0xFFFFFFFF) // Pure Ivory surface
val LightSurface = Color(0xFFFAF8F5) // Soft warm ivory surface
val LightElevated = Color(0xFFF2ECE1) // Elevated light surface
val LightHighlight = Color(0xFFEBE3D3) // Highlight light surface
val LightCardSurface = Color(0xFFFFFFFF) // Crisp bright card surface
val LightCard = LightCardSurface
val LightBorder = Color(0x38B8860B) // Subtle antique gold hairline border
val LightCardBorder = Color(0x1F000000) // Subtle shadow border

// High contrast text for Dark theme
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFE4E4E7)
val TextMuted = Color(0xFFA1A1AA)

// High contrast text for Light theme
val LightTextPrimary = Color(0xFF18181B) // Obsidian Charcoal
val LightTextSecondary = Color(0xFF3F3F46) // Deep Neutral
val LightTextMuted = Color(0xFF71717A) // Muted Stone

val TierNovice = Color(0xFF8E8E93)
val TierBuilder = Color(0xFF4FC3F7)
val TierArchitect = Color(0xFFBA68C8)
val TierSovereign = Color(0xFFD4AF37)
val TierLegacy = Color(0xFFFF6E40)

val SuccessGreen = Color(0xFF4ADE80)
val ErrorRed = Color(0xFFF87171)

// Gradients
val GoldLinearGradient = Brush.linearGradient(
    colors = listOf(GoldLight, GoldPrimary, GoldDark)
)

val GoldMetallicGradient = Brush.horizontalGradient(
    colors = listOf(GoldDark, GoldLight, GoldMetallic, GoldPrimary, GoldDark)
)

val AmberGradient = Brush.linearGradient(
    colors = listOf(AmberBright, AmberAccent, AmberDeep)
)

val DarkSurfaceGradient = Brush.verticalGradient(
    colors = listOf(DarkCharcoal, RichBlack)
)

val LightSurfaceGradient = Brush.verticalGradient(
    colors = listOf(LightSurface, LightAlabaster)
)

val BrushedCardGradient = Brush.linearGradient(
    colors = listOf(BrushedMetal, DarkCharcoal)
)

val LightCardGradient = Brush.linearGradient(
    colors = listOf(LightCardSurface, LightSurface)
)

val GoldGlowGradient = Brush.radialGradient(
    colors = listOf(GoldGlow, Color.Transparent)
)
