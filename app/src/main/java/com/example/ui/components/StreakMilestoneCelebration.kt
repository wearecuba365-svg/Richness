package com.example.ui.components

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
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
 * High-craft metadata definitions for Think and Grow Rich habit streak milestones
 */
data class StreakMilestoneInfo(
    val days: Int,
    val title: String,
    val subtitle: String,
    val badgeId: String,
    val badgeTitle: String,
    val xpReward: Int,
    val quote: String,
    val author: String = "Napoleon Hill",
    val description: String,
    val iconVector: ImageVector,
    val levelName: String
)

val STREAK_MILESTONES = listOf(
    StreakMilestoneInfo(
        days = 3,
        title = "3-Day Discipline Spark",
        subtitle = "The Habit Loop Ignition",
        badgeId = "badge_streak_3",
        badgeTitle = "3-Day Discipline Spark",
        xpReward = 150,
        quote = "Definiteness of purpose is the starting point of all achievement. Weak desires bring weak results.",
        description = "You have maintained an unbroken 3-day chain of daily rituals. The initial subconscious resistance has been overcome.",
        iconVector = Icons.Filled.Whatshot,
        levelName = "Spark"
    ),
    StreakMilestoneInfo(
        days = 7,
        title = "7-Day Sovereign Flame",
        subtitle = "One Week of Unbroken Mastery",
        badgeId = "badge_flame_7",
        badgeTitle = "7-Day Sovereign Flame",
        xpReward = 250,
        quote = "Patience, persistence and perspiration make an unbeatable combination for success.",
        description = "One full calendar week of relentless discipline. Daily autosuggestion and ritual execution have formed a steady flame.",
        iconVector = Icons.Filled.LocalFireDepartment,
        levelName = "Flame"
    ),
    StreakMilestoneInfo(
        days = 14,
        title = "14-Day Fortress of Habit",
        subtitle = "Fortified Subconscious Discipline",
        badgeId = "badge_fortress_14",
        badgeTitle = "14-Day Fortress of Habit",
        xpReward = 350,
        quote = "Persistence is to the character of man what carbon is to steel.",
        description = "Two consecutive weeks unbroken. Your habits are transitioning from conscious effort into permanent sovereign identity.",
        iconVector = Icons.Filled.Shield,
        levelName = "Fortress"
    ),
    StreakMilestoneInfo(
        days = 30,
        title = "30-Day Transmutation Ironclad",
        subtitle = "Subconscious Indomitable Power",
        badgeId = "badge_streak_30",
        badgeTitle = "30-Day Transmutation Ironclad",
        xpReward = 500,
        quote = "Whatever the mind can conceive and believe, the mind can achieve.",
        description = "30 uninterrupted days of sovereign mastery! The Think and Grow Rich philosophy is now deeply transmuted into your character.",
        iconVector = Icons.Filled.WorkspacePremium,
        levelName = "Ironclad"
    )
)

fun getMilestoneForDays(days: Int): StreakMilestoneInfo? {
    return STREAK_MILESTONES.firstOrNull { it.days == days }
}

/**
 * Interactive Visual Streak Counter with connected milestone stepper & celebration trigger
 */
@Composable
fun HabitStreakMilestoneBar(
    currentStreak: Int,
    bestStreak: Int,
    todayCompletedHabitsCount: Int,
    totalHabitsCount: Int,
    onMilestoneClick: (StreakMilestoneInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    // Determine the next milestone target
    val nextMilestone = STREAK_MILESTONES.firstOrNull { currentStreak < it.days }
        ?: STREAK_MILESTONES.last()
    
    val daysRemaining = (nextMilestone.days - currentStreak).coerceAtLeast(0)
    val previousMilestoneDays = STREAK_MILESTONES.lastOrNull { currentStreak >= it.days }?.days ?: 0

    // Calculate progress fraction towards the next milestone
    val progressRange = (nextMilestone.days - previousMilestoneDays).coerceAtLeast(1)
    val progressDone = (currentStreak - previousMilestoneDays).coerceAtLeast(0)
    val progressFraction = (progressDone.toFloat() / progressRange.toFloat()).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "milestone_progress"
    )

    // Ambient resting fire glow
    val infiniteTransition = rememberInfiniteTransition(label = "streak_flame_pulse")
    val flameScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_scale"
    )
    val flameGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_glow"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(
                        AmberAccent.copy(alpha = 0.45f),
                        GoldPrimary.copy(alpha = 0.65f),
                        GoldDark.copy(alpha = 0.4f)
                    )
                ),
                RoundedCornerShape(16.dp)
            )
            .testTag("habit_visual_streak_counter"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF18140E).copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // --- TOP ROW: ACTIVE STREAK BADGE & SUMMARY ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Glowing Flame Emblem
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        AmberBright.copy(alpha = flameGlowAlpha),
                                        GoldDark.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(1.dp, AmberBright.copy(alpha = 0.7f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocalFireDepartment,
                            contentDescription = "Habit Streak Flame",
                            tint = AmberBright,
                            modifier = Modifier
                                .size(24.dp)
                                .scale(flameScale)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "$currentStreak-DAY STREAK",
                                color = GoldLight,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            if (currentStreak >= 30) {
                                Surface(
                                    color = GoldPrimary.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(4.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary)
                                ) {
                                    Text(
                                        text = "IRONCLAD",
                                        color = GoldLight,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = if (daysRemaining == 0) "All 4 Milestones Mastered! 🔥"
                            else "$daysRemaining ${if (daysRemaining == 1) "day" else "days"} until ${nextMilestone.title} (+${nextMilestone.xpReward} XP)",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                // Best Streak Pill
                Surface(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldDark.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "RECORD",
                            color = TextMuted,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "$bestStreak Days",
                            color = GoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- 4-MILESTONE CONNECTED STEPPER TRACK ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                STREAK_MILESTONES.forEachIndexed { index, milestone ->
                    val isUnlocked = currentStreak >= milestone.days
                    val isCurrentTarget = nextMilestone.days == milestone.days && !isUnlocked

                    MilestoneNode(
                        milestone = milestone,
                        isUnlocked = isUnlocked,
                        isCurrentTarget = isCurrentTarget,
                        currentStreak = currentStreak,
                        onClick = { onMilestoneClick(milestone) },
                        modifier = Modifier.weight(1f)
                    )

                    // Connecting progress bar between nodes
                    if (index < STREAK_MILESTONES.size - 1) {
                        val nextMilestoneItem = STREAK_MILESTONES[index + 1]
                        val isSegmentComplete = currentStreak >= nextMilestoneItem.days
                        val isSegmentActive = currentStreak >= milestone.days && currentStreak < nextMilestoneItem.days
                        val segmentProgress = if (isSegmentComplete) 1f
                        else if (isSegmentActive) {
                            val segSpan = (nextMilestoneItem.days - milestone.days).coerceAtLeast(1)
                            ((currentStreak - milestone.days).toFloat() / segSpan.toFloat()).coerceIn(0f, 1f)
                        } else 0f

                        Box(
                            modifier = Modifier
                                .weight(0.7f)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.White.copy(alpha = 0.1f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(segmentProgress)
                                    .height(3.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(GoldDark, AmberBright)
                                        )
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Helper hint footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tap milestone to view perks & celebrate",
                    color = TextMuted,
                    fontSize = 9.sp
                )
                Text(
                    text = "Today: $todayCompletedHabitsCount/$totalHabitsCount habits completed",
                    color = if (todayCompletedHabitsCount >= totalHabitsCount && totalHabitsCount > 0) Color(0xFF81C784) else AmberBright,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun MilestoneNode(
    milestone: StreakMilestoneInfo,
    isUnlocked: Boolean,
    isCurrentTarget: Boolean,
    currentStreak: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "target_pulse_${milestone.days}")
    val targetPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "target_node_scale"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .scale(if (isCurrentTarget) targetPulse else 1f)
                .clip(CircleShape)
                .background(
                    when {
                        isUnlocked -> Brush.radialGradient(listOf(GoldLight, GoldDark))
                        isCurrentTarget -> Brush.radialGradient(listOf(AmberAccent.copy(alpha = 0.35f), DarkCharcoal))
                        else -> Brush.radialGradient(listOf(DarkCharcoal, Color(0xFF100E0A)))
                    }
                )
                .border(
                    1.dp,
                    when {
                        isUnlocked -> GoldLight
                        isCurrentTarget -> AmberBright
                        else -> Color.White.copy(alpha = 0.15f)
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isUnlocked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = RichBlack,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Icon(
                    imageVector = milestone.iconVector,
                    contentDescription = null,
                    tint = if (isCurrentTarget) AmberBright else TextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${milestone.days}D",
            color = if (isUnlocked) GoldLight else if (isCurrentTarget) AmberBright else TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "+${milestone.xpReward} XP",
            color = if (isUnlocked) GoldPrimary else TextMuted,
            fontSize = 8.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Full Celebratory Animation Overlay / Dialog for 3, 7, 14, and 30-Day Milestones
 */
@Composable
fun StreakMilestoneCelebrationDialog(
    milestone: StreakMilestoneInfo,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    // Animation States
    val entranceScale = remember { Animatable(0.4f) }
    val sunburstRotation = remember { Animatable(0f) }
    val badgeBounce = remember { Animatable(0.6f) }
    val haloExpansion = remember { Animatable(0f) }
    val particlesAnimation = remember { Animatable(0f) }
    var displayedDayCount by remember { mutableIntStateOf(0) }

    // Floating Particles data
    val particleList = remember {
        List(24) {
            val angle = Random.nextDouble(0.0, 360.0)
            val distance = Random.nextDouble(40.0, 160.0)
            val size = Random.nextDouble(3.0, 8.0)
            val speed = Random.nextDouble(0.8, 1.2)
            val color = if (Random.nextBoolean()) GoldLight else AmberBright
            ParticleData(angle, distance, size.toFloat(), speed.toFloat(), color)
        }
    }

    LaunchedEffect(milestone) {
        // Coordinated animation sequence
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
            badgeBounce.animateTo(
                1.2f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            badgeBounce.animateTo(1.0f, animationSpec = tween(300))
        }
        launch {
            haloExpansion.animateTo(1f, animationSpec = tween(1200, easing = FastOutSlowInEasing))
        }
        launch {
            particlesAnimation.animateTo(1f, animationSpec = tween(1800, easing = FastOutSlowInEasing))
        }
        launch {
            sunburstRotation.animateTo(
                360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(16000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }

        // Count up animation to milestone.days
        val stepDelay = (600 / milestone.days.coerceAtLeast(1)).coerceIn(20, 150).toLong()
        for (i in 1..milestone.days) {
            displayedDayCount = i
            delay(stepDelay)
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
                .background(Color.Black.copy(alpha = 0.88f))
                .clickable { onDismiss() }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background Canvas: Sunburst Rays & Ember Particle Burst
            Canvas(
                modifier = Modifier
                    .size(340.dp)
                    .graphicsLayer { alpha = 0.65f }
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2

                // Rotating sunburst rays
                rotate(sunburstRotation.value, center) {
                    val rayCount = 16
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
                                    GoldPrimary.copy(alpha = 0.45f),
                                    AmberBright.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            ),
                            start = center,
                            end = end,
                            strokeWidth = 6f,
                            cap = StrokeCap.Round
                        )
                    }
                }

                // Expanding Halo Ring
                if (haloExpansion.value > 0.05f) {
                    val haloRadius = radius * haloExpansion.value * 0.9f
                    val haloAlpha = (1f - haloExpansion.value).coerceIn(0f, 1f)
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(
                                AmberBright.copy(alpha = haloAlpha * 0.8f),
                                GoldPrimary.copy(alpha = haloAlpha * 0.4f),
                                Color.Transparent
                            )
                        ),
                        radius = haloRadius,
                        center = center,
                        style = Stroke(width = 8f)
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
                            radius = p.size * (1f - pProgress * 0.5f),
                            center = pOffset
                        )
                    }
                }
            }

            // Main Modal Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .graphicsLayer {
                        scaleX = entranceScale.value
                        scaleY = entranceScale.value
                    }
                    .border(
                        2.dp,
                        Brush.linearGradient(
                            listOf(
                                GoldLight,
                                AmberBright,
                                GoldDark,
                                GoldLight
                            )
                        ),
                        RoundedCornerShape(24.dp)
                    )
                    .clickable(enabled = false) {}
                    .testTag("streak_milestone_celebration_dialog"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF14120E).copy(alpha = 0.98f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- TOP CHIP: SOVEREIGN MILESTONE REACHED ---
                    Surface(
                        color = AmberAccent.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberBright.copy(alpha = 0.7f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "SOVEREIGN MILESTONE UNLOCKED",
                                color = GoldLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.4.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- CENTER ICON CREST WITH COUNT ROLLING ---
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .graphicsLayer {
                                scaleX = badgeBounce.value
                                scaleY = badgeBounce.value
                            }
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        GoldLight,
                                        GoldDark,
                                        Color(0xFF281E0F)
                                    )
                                )
                            )
                            .border(2.dp, AmberBright, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = milestone.iconVector,
                                contentDescription = milestone.title,
                                tint = RichBlack,
                                modifier = Modifier.size(34.dp)
                            )
                            Text(
                                text = "$displayedDayCount DAYS",
                                color = RichBlack,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // --- TITLE & SUBTITLE ---
                    Text(
                        text = milestone.title,
                        color = GoldLight,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = milestone.subtitle,
                        color = AmberBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- XP REWARD CHIP ---
                    Surface(
                        color = GoldPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "+${milestone.xpReward} XP CREDITED & BADGE UNLOCKED",
                                color = GoldLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // --- PHILOSOPHICAL QUOTE ---
                    Surface(
                        color = SurfaceElevated.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "\"${milestone.quote}\"",
                                color = Color(0xFFEDE4D3),
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "— ${milestone.author}",
                                color = GoldPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- ACTION BUTTON: CLAIM & SEAL ---
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("claim_streak_milestone_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = RichBlack
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = RichBlack,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SEAL MILESTONE & CONTINUE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class ParticleData(
    val angle: Double,
    val distance: Double,
    val size: Float,
    val speed: Float,
    val color: Color
)
