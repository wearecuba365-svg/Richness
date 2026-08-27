package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ModuleEntity
import com.example.data.model.SECTION_ACHIEVEMENTS
import com.example.data.model.SectionAchievementInfo
import com.example.data.model.getSectionProgress
import com.example.data.model.isSectionCompleted
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Full Celebratory Animation Overlay / Dialog for completing all modules in a Section
 */
private data class SectionCelebrationParticle(
    val angle: Double,
    val distance: Double,
    val size: Float,
    val speed: Float,
    val color: Color
)

@Composable
fun SectionAchievementCelebrationDialog(
    section: SectionAchievementInfo,
    modules: List<ModuleEntity> = emptyList(),
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    // Animation States
    val entranceScale = remember { Animatable(0.35f) }
    val sunburstRotation = remember { Animatable(0f) }
    val crestBounce = remember { Animatable(0.5f) }
    val haloExpansion = remember { Animatable(0f) }
    val particlesAnimation = remember { Animatable(0f) }
    var displayedXpCount by remember { mutableIntStateOf(0) }

    // Particle burst definitions
    val particleList = remember {
        List(32) {
            val angle = Random.nextDouble(0.0, 360.0)
            val distance = Random.nextDouble(50.0, 190.0)
            val size = Random.nextDouble(3.5, 9.5)
            val speed = Random.nextDouble(0.7, 1.3)
            val color = when (it % 4) {
                0 -> GoldLight
                1 -> AmberBright
                2 -> Color(0xFFFFF9E6)
                else -> section.accentGold
            }
            SectionCelebrationParticle(angle, distance, size.toFloat(), speed.toFloat(), color)
        }
    }

    LaunchedEffect(section) {
        launch {
            entranceScale.animateTo(
                1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            crestBounce.animateTo(
                1.25f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            crestBounce.animateTo(1.0f, animationSpec = tween(300))
        }
        launch {
            haloExpansion.animateTo(1f, animationSpec = tween(1400, easing = FastOutSlowInEasing))
        }
        launch {
            particlesAnimation.animateTo(1f, animationSpec = tween(2200, easing = FastOutSlowInEasing))
        }
        launch {
            sunburstRotation.animateTo(
                360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(18000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }

        // Count up animated XP
        val step = maxOf(10, section.xpReward / 25)
        var current = 0
        while (current < section.xpReward) {
            current = (current + step).coerceAtMost(section.xpReward)
            displayedXpCount = current
            delay(28)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.90f))
                .clickable { onDismiss() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background Canvas: Sunburst Rays & Ember Particle Burst
            Canvas(
                modifier = Modifier
                    .size(360.dp)
                    .graphicsLayer { alpha = 0.75f }
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2

                // Rotating sunburst rays
                rotate(sunburstRotation.value, center) {
                    val rayCount = 20
                    val angleStep = 360f / rayCount
                    for (i in 0 until rayCount) {
                        val angleRad = Math.toRadians((i * angleStep).toDouble())
                        val end = Offset(
                            center.x + (radius * cos(angleRad)).toFloat(),
                            center.y + (radius * sin(angleRad)).toFloat()
                        )
                        drawLine(
                            brush = Brush.linearGradient(
                                listOf(
                                    section.primaryGold.copy(alpha = 0.55f),
                                    section.accentGold.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            start = center,
                            end = end,
                            strokeWidth = 7f,
                            cap = StrokeCap.Round
                        )
                    }
                }

                // Expanding Halo Rings
                if (haloExpansion.value > 0.05f) {
                    val haloRadius = radius * haloExpansion.value * 0.95f
                    val haloAlpha = (1f - haloExpansion.value).coerceIn(0f, 1f)
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(
                                section.accentGold.copy(alpha = haloAlpha * 0.9f),
                                section.primaryGold.copy(alpha = haloAlpha * 0.4f),
                                Color.Transparent
                            )
                        ),
                        radius = haloRadius,
                        center = center,
                        style = Stroke(width = 9f)
                    )
                }

                // Burst Particles
                if (particlesAnimation.value > 0.05f) {
                    val pProgress = particlesAnimation.value
                    particleList.forEach { p ->
                        val currentDistance = p.distance * pProgress * p.speed
                        val rad = Math.toRadians(p.angle)
                        val pOffset = Offset(
                            center.x + (currentDistance * cos(rad)).toFloat(),
                            center.y + (currentDistance * sin(rad)).toFloat()
                        )
                        val pAlpha = (1f - pProgress).coerceIn(0f, 1f)
                        drawCircle(
                            color = p.color.copy(alpha = pAlpha),
                            radius = p.size * (1f - pProgress * 0.4f),
                            center = pOffset
                        )
                    }
                }
            }

            // Main Celebratory Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .graphicsLayer {
                        scaleX = entranceScale.value
                        scaleY = entranceScale.value
                    }
                    .border(
                        2.dp,
                        Brush.linearGradient(
                            listOf(
                                GoldLight,
                                section.accentGold,
                                GoldDark,
                                GoldLight
                            )
                        ),
                        RoundedCornerShape(26.dp)
                    )
                    .clickable(enabled = false) {}
                    .testTag("section_achievement_celebration_dialog"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF14120E).copy(alpha = 0.98f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- TOP CHIP: SECTION COMPLETE BANNER ---
                    Surface(
                        color = AmberAccent.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight.copy(alpha = 0.8f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "SECTION ${section.romanNumeral} COMPLETE • ${section.pillarName.uppercase()}",
                                color = GoldLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.3.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- CENTER 3D-STYLED SECTION CREST ---
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .graphicsLayer {
                                scaleX = crestBounce.value
                                scaleY = crestBounce.value
                            }
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        section.accentGold,
                                        GoldDark,
                                        DarkCharcoal
                                    )
                                )
                            )
                            .border(
                                width = 3.dp,
                                brush = Brush.sweepGradient(
                                    listOf(
                                        GoldLight,
                                        section.accentGold,
                                        Color(0xFFFFF9E6),
                                        GoldDark,
                                        GoldLight
                                    )
                                ),
                                shape = CircleShape
                            )
                            .shadow(24.dp, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = section.iconVector,
                                contentDescription = section.title,
                                tint = GoldLight,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "SEC ${section.romanNumeral}",
                                color = Color(0xFFFFF9E6),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // --- SECTION TITLE & SUBTITLE ---
                    Text(
                        text = section.title,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        color = GoldLight,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = section.subtitle,
                        fontSize = 11.sp,
                        color = AmberBright,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- XP REWARD CHIP ---
                    Surface(
                        color = GoldDark.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.WorkspacePremium,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "+$displayedXpCount SOVEREIGN XP CREDITED",
                                color = GoldLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // --- COMPLETED VAULTS IN SECTION PILL ROW ---
                    Text(
                        text = "ALL ${section.moduleIds.size} VAULTS SEALED IN SECTION",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        section.moduleIds.forEach { moduleId ->
                            val module = modules.firstOrNull { it.id == moduleId }
                            val modTitle = module?.title?.take(10) ?: "Vault $moduleId"
                            Surface(
                                color = SurfaceElevated,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.8f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text = "V$moduleId",
                                        color = TextPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // --- PHILOSOPHICAL MAXIM CARD ---
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceElevated.copy(alpha = 0.7f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FormatQuote,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "\"${section.quote}\"",
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 11.sp,
                                    color = TextPrimary,
                                    lineHeight = 15.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "— ${section.quoteAuthor}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmberBright
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- ACTION BUTTON ---
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("dismiss_section_achievement_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(GoldLinearGradient)
                                .border(1.dp, Color(0xFFFFF9E6), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Diamond,
                                    contentDescription = null,
                                    tint = RichBlack,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "SEAL ACHIEVEMENT & CONTINUE",
                                    color = RichBlack,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Animated Toast Banner for section completions that slides in from top
 */
@Composable
fun SectionAchievementToastBanner(
    section: SectionAchievementInfo,
    onViewBadge: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkCharcoal,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldLight),
        shadowElevation = 16.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewBadge() }
            .testTag("section_achievement_toast_banner")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(GoldLinearGradient)
                    .border(1.dp, Color(0xFFFFF9E6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = section.iconVector,
                    contentDescription = null,
                    tint = RichBlack,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ACHIEVEMENT UNLOCKED ✨",
                        color = AmberBright,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
                Text(
                    text = "${section.title} (+${section.xpReward} XP)",
                    color = GoldLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Button(
                onClick = onViewBadge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text(
                    text = "VIEW",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Dismiss",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Section Header Card displayed on top of each module section in ModulesPathScreen
 */
@Composable
fun SectionHeaderCard(
    section: SectionAchievementInfo,
    modules: List<ModuleEntity>,
    onBadgeClick: (SectionAchievementInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val (completed, total) = getSectionProgress(section, modules)
    val isCompleted = completed >= total && total > 0

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isCompleted) 1.5.dp else 1.dp,
                brush = if (isCompleted) {
                    Brush.horizontalGradient(
                        listOf(GoldDark, GoldLight, AmberBright, GoldDark)
                    )
                } else {
                    Brush.horizontalGradient(
                        listOf(cardBorderColor, cardBorderColor)
                    )
                },
                shape = RoundedCornerShape(20.dp)
            )
            .testTag("section_header_${section.sectionId}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                if (isDark) SurfaceElevated.copy(alpha = 0.85f) else LightElevated
            } else {
                surfaceColor
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Section Title & Pillar Tag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Roman Numeral / Crest Badge
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCompleted) {
                                    Brush.radialGradient(listOf(section.accentGold, GoldDark))
                                } else {
                                    Brush.radialGradient(listOf(surfaceColor, DarkCharcoal))
                                }
                            )
                            .border(
                                width = if (isCompleted) 2.dp else 1.dp,
                                color = if (isCompleted) GoldLight else cardBorderColor,
                                shape = CircleShape
                            )
                            .clickable { onBadgeClick(section) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isCompleted) section.iconVector else Icons.Filled.Lock,
                                contentDescription = section.title,
                                tint = if (isCompleted) GoldLight else textMutedColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "SEC ${section.romanNumeral}",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) Color(0xFFFFF9E6) else textMutedColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SECTION ${section.romanNumeral} • ${section.pillarName.uppercase()}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) goldAccent else AmberAccent,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = section.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = textColor
                        )
                    }
                }

                // Section Achievement Status Chip
                Surface(
                    color = if (isCompleted) {
                        GoldPrimary.copy(alpha = if (isDark) 0.22f else 0.15f)
                    } else {
                        surfaceColor
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCompleted) goldAccent else cardBorderColor
                    ),
                    modifier = Modifier.clickable { onBadgeClick(section) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Filled.EmojiEvents else Icons.Filled.Lock,
                            contentDescription = null,
                            tint = if (isCompleted) goldAccent else textMutedColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (isCompleted) "MASTERED ✨" else "$completed/$total",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) goldAccent else textMutedColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = section.subtitle,
                fontSize = 11.sp,
                color = textSecColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar
            val progressFraction = (completed.toFloat() / total.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isCompleted) GoldLight else GoldPrimary,
                    trackColor = if (isDark) DarkCharcoal else LightBorder
                )
                Text(
                    text = "${(progressFraction * 100).toInt()}%",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompleted) goldAccent else textMutedColor
                )
            }
        }
    }
}
