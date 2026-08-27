package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.UserProfileEntity

/**
 * User Tiers in Richlogy Wealth Protocol
 */
enum class UserTier(
    val title: String,
    val rankLevel: Int,
    val xpThreshold: Int,
    val nextXpThreshold: Int,
    val auraDescription: String
) {
    NOVICE(
        title = "Novice",
        rankLevel = 1,
        xpThreshold = 0,
        nextXpThreshold = 500,
        auraDescription = "Subtle Burnished Bronze • Awoken Initiate"
    ),
    BUILDER(
        title = "Builder",
        rankLevel = 2,
        xpThreshold = 500,
        nextXpThreshold = 1500,
        auraDescription = "Polished Warm Gold • Disciplined Practitioner"
    ),
    ARCHITECT(
        title = "Architect",
        rankLevel = 3,
        xpThreshold = 1500,
        nextXpThreshold = 3500,
        auraDescription = "Radiant Luminous Gold • Mindset Strategist"
    ),
    SOVEREIGN(
        title = "Sovereign",
        rankLevel = 4,
        xpThreshold = 3500,
        nextXpThreshold = 7000,
        auraDescription = "Imperial 24K Sovereign Gold • Master of Wealth"
    ),
    LEGACY(
        title = "Legacy",
        rankLevel = 5,
        xpThreshold = 7000,
        nextXpThreshold = 10000,
        auraDescription = "Celestial Apex Gold • Transmuted Dynasty"
    );

    companion object {
        fun fromString(name: String?): UserTier {
            return when (name?.trim()?.lowercase()) {
                "legacy" -> LEGACY
                "sovereign" -> SOVEREIGN
                "architect" -> ARCHITECT
                "builder" -> BUILDER
                else -> NOVICE
            }
        }
    }
}

/**
 * Comprehensive Dynamic Gold Theme & Glow Configuration
 */
data class TierGoldTheme(
    val tier: UserTier,
    val rankLevel: Int,
    val glowAlpha: Float,
    val haloIntensity: Float,
    val goldPrimary: Color,
    val goldLight: Color,
    val goldDark: Color,
    val goldAura: Color,
    val goldGlow: Color,
    val darkBorder: Color,
    val goldLinearGradient: Brush,
    val goldMetallicGradient: Brush,
    val goldAuraGradient: Brush,
    val emberParticleCount: Int,
    val emberSpeedMultiplier: Float,
    val shimmerDurationMs: Int,
    val crestBorderWidth: Dp,
    val badgeBorderWidth: Dp,
    val lusterBloomAlpha: Float
)

/**
 * Master function to dynamically resolve metallic gold accent intensity based on tier.
 * Higher tiers produce richer saturation, higher radiance, and luminous bloom.
 */
fun getTierGoldIntensity(tier: UserTier): TierGoldTheme {
    return when (tier) {
        UserTier.NOVICE -> {
            // Subtle burnished brass/bronze-gold with modest aura
            val primary = Color(0xFFC29B38)
            val light = Color(0xFFE8D4A2)
            val dark = Color(0xFF7A5C1E)
            val glow = Color(0x33C29B38)
            val aura = Color(0x1FC29B38)
            val border = Color(0x38C29B38)
            TierGoldTheme(
                tier = tier,
                rankLevel = 1,
                glowAlpha = 0.25f,
                haloIntensity = 0.20f,
                goldPrimary = primary,
                goldLight = light,
                goldDark = dark,
                goldAura = aura,
                goldGlow = glow,
                darkBorder = border,
                goldLinearGradient = Brush.linearGradient(listOf(light, primary, dark)),
                goldMetallicGradient = Brush.horizontalGradient(listOf(dark, light, primary, dark)),
                goldAuraGradient = Brush.radialGradient(listOf(glow, Color.Transparent)),
                emberParticleCount = 6,
                emberSpeedMultiplier = 0.8f,
                shimmerDurationMs = 3200,
                crestBorderWidth = 1.0.dp,
                badgeBorderWidth = 1.0.dp,
                lusterBloomAlpha = 0.15f
            )
        }

        UserTier.BUILDER -> {
            // Polished warm classic gold
            val primary = Color(0xFFD4AF37)
            val light = Color(0xFFFFE082)
            val dark = Color(0xFF9E781B)
            val glow = Color(0x4DD4AF37)
            val aura = Color(0x2BD4AF37)
            val border = Color(0x4CD4AF37)
            TierGoldTheme(
                tier = tier,
                rankLevel = 2,
                glowAlpha = 0.45f,
                haloIntensity = 0.40f,
                goldPrimary = primary,
                goldLight = light,
                goldDark = dark,
                goldAura = aura,
                goldGlow = glow,
                darkBorder = border,
                goldLinearGradient = Brush.linearGradient(listOf(light, primary, dark)),
                goldMetallicGradient = Brush.horizontalGradient(listOf(dark, light, Color(0xFFE5C158), primary, dark)),
                goldAuraGradient = Brush.radialGradient(listOf(glow, Color.Transparent)),
                emberParticleCount = 12,
                emberSpeedMultiplier = 1.0f,
                shimmerDurationMs = 2600,
                crestBorderWidth = 1.5.dp,
                badgeBorderWidth = 1.0.dp,
                lusterBloomAlpha = 0.35f
            )
        }

        UserTier.ARCHITECT -> {
            // Radiant luminous gold with heightened sheen
            val primary = Color(0xFFE0BB3C)
            val light = Color(0xFFFFF099)
            val dark = Color(0xFFB08620)
            val glow = Color(0x66E0BB3C)
            val aura = Color(0x3DE0BB3C)
            val border = Color(0x60E0BB3C)
            TierGoldTheme(
                tier = tier,
                rankLevel = 3,
                glowAlpha = 0.65f,
                haloIntensity = 0.60f,
                goldPrimary = primary,
                goldLight = light,
                goldDark = dark,
                goldAura = aura,
                goldGlow = glow,
                darkBorder = border,
                goldLinearGradient = Brush.linearGradient(listOf(light, primary, dark, AmberAccent)),
                goldMetallicGradient = Brush.horizontalGradient(listOf(dark, light, Color(0xFFFFD54F), primary, dark)),
                goldAuraGradient = Brush.radialGradient(listOf(glow, Color.Transparent)),
                emberParticleCount = 18,
                emberSpeedMultiplier = 1.2f,
                shimmerDurationMs = 2100,
                crestBorderWidth = 2.0.dp,
                badgeBorderWidth = 1.5.dp,
                lusterBloomAlpha = 0.55f
            )
        }

        UserTier.SOVEREIGN -> {
            // Imperial 24K Sovereign Gold with rich deep luster and gold-fire aura
            val primary = Color(0xFFF5C542)
            val light = Color(0xFFFFF7C2)
            val dark = Color(0xFFC7951B)
            val glow = Color(0x8CF5C542)
            val aura = Color(0x52F5C542)
            val border = Color(0x80F5C542)
            TierGoldTheme(
                tier = tier,
                rankLevel = 4,
                glowAlpha = 0.85f,
                haloIntensity = 0.80f,
                goldPrimary = primary,
                goldLight = light,
                goldDark = dark,
                goldAura = aura,
                goldGlow = glow,
                darkBorder = border,
                goldLinearGradient = Brush.linearGradient(listOf(Color(0xFFFFFFFF), light, primary, dark, AmberDeep)),
                goldMetallicGradient = Brush.horizontalGradient(listOf(dark, light, Color(0xFFFFE082), primary, Color(0xFFFFB300), dark)),
                goldAuraGradient = Brush.radialGradient(listOf(glow, aura, Color.Transparent)),
                emberParticleCount = 26,
                emberSpeedMultiplier = 1.5f,
                shimmerDurationMs = 1700,
                crestBorderWidth = 2.5.dp,
                badgeBorderWidth = 1.8.dp,
                lusterBloomAlpha = 0.75f
            )
        }

        UserTier.LEGACY -> {
            // Apex Celestial Gold with holographic diamond luster and transcendent bloom
            val primary = Color(0xFFFFD700)
            val light = Color(0xFFFFFFFF)
            val dark = Color(0xFFD69A08)
            val glow = Color(0xB3FFD700)
            val aura = Color(0x66FFD700)
            val border = Color(0x99FFD700)
            TierGoldTheme(
                tier = tier,
                rankLevel = 5,
                glowAlpha = 1.0f,
                haloIntensity = 1.0f,
                goldPrimary = primary,
                goldLight = light,
                goldDark = dark,
                goldAura = aura,
                goldGlow = glow,
                darkBorder = border,
                goldLinearGradient = Brush.linearGradient(
                    listOf(Color(0xFFFFFFFF), Color(0xFFFFF9DB), primary, AmberBright, dark)
                ),
                goldMetallicGradient = Brush.horizontalGradient(
                    listOf(dark, primary, light, Color(0xFFFFC107), primary, dark)
                ),
                goldAuraGradient = Brush.radialGradient(listOf(glow, aura, Color.Transparent)),
                emberParticleCount = 36,
                emberSpeedMultiplier = 1.8f,
                shimmerDurationMs = 1300,
                crestBorderWidth = 3.0.dp,
                badgeBorderWidth = 2.0.dp,
                lusterBloomAlpha = 0.95f
            )
        }
    }
}

fun getTierGoldIntensity(tierName: String?): TierGoldTheme =
    getTierGoldIntensity(UserTier.fromString(tierName))

fun UserProfileEntity?.toTierGoldTheme(): TierGoldTheme =
    getTierGoldIntensity(this?.tierName)

/**
 * Composable function to smoothly animate dynamic gold intensity transitions
 * when the user levels up or changes tiers.
 */
@Composable
fun rememberAnimatedTierGoldTheme(userProfile: UserProfileEntity?): TierGoldTheme {
    val targetTier = UserTier.fromString(userProfile?.tierName)
    val target = getTierGoldIntensity(targetTier)

    val animDuration = 600
    val easing = FastOutSlowInEasing

    val animatedPrimary by animateColorAsState(
        targetValue = target.goldPrimary,
        animationSpec = tween(animDuration, easing = easing),
        label = "gold_primary"
    )
    val animatedLight by animateColorAsState(
        targetValue = target.goldLight,
        animationSpec = tween(animDuration, easing = easing),
        label = "gold_light"
    )
    val animatedDark by animateColorAsState(
        targetValue = target.goldDark,
        animationSpec = tween(animDuration, easing = easing),
        label = "gold_dark"
    )
    val animatedAura by animateColorAsState(
        targetValue = target.goldAura,
        animationSpec = tween(animDuration, easing = easing),
        label = "gold_aura"
    )
    val animatedGlow by animateColorAsState(
        targetValue = target.goldGlow,
        animationSpec = tween(animDuration, easing = easing),
        label = "gold_glow"
    )
    val animatedBorder by animateColorAsState(
        targetValue = target.darkBorder,
        animationSpec = tween(animDuration, easing = easing),
        label = "gold_border"
    )
    val animatedGlowAlpha by animateFloatAsState(
        targetValue = target.glowAlpha,
        animationSpec = tween(animDuration, easing = easing),
        label = "glow_alpha"
    )
    val animatedHalo by animateFloatAsState(
        targetValue = target.haloIntensity,
        animationSpec = tween(animDuration, easing = easing),
        label = "halo_intensity"
    )
    val animatedBloom by animateFloatAsState(
        targetValue = target.lusterBloomAlpha,
        animationSpec = tween(animDuration, easing = easing),
        label = "bloom_alpha"
    )

    return TierGoldTheme(
        tier = targetTier,
        rankLevel = target.rankLevel,
        glowAlpha = animatedGlowAlpha,
        haloIntensity = animatedHalo,
        goldPrimary = animatedPrimary,
        goldLight = animatedLight,
        goldDark = animatedDark,
        goldAura = animatedAura,
        goldGlow = animatedGlow,
        darkBorder = animatedBorder,
        goldLinearGradient = Brush.linearGradient(listOf(animatedLight, animatedPrimary, animatedDark)),
        goldMetallicGradient = Brush.horizontalGradient(
            listOf(animatedDark, animatedLight, animatedPrimary, animatedDark)
        ),
        goldAuraGradient = Brush.radialGradient(listOf(animatedGlow, Color.Transparent)),
        emberParticleCount = target.emberParticleCount,
        emberSpeedMultiplier = target.emberSpeedMultiplier,
        shimmerDurationMs = target.shimmerDurationMs,
        crestBorderWidth = target.crestBorderWidth,
        badgeBorderWidth = target.badgeBorderWidth,
        lusterBloomAlpha = animatedBloom
    )
}

/**
 * CompositionLocal for ambient Tier-based metallic gold theme access
 */
val LocalTierGoldTheme = compositionLocalOf<TierGoldTheme> {
    getTierGoldIntensity(UserTier.NOVICE)
}
