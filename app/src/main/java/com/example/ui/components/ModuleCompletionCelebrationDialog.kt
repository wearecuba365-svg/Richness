package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.graphics.Path
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
import com.example.data.model.ModuleCompletionCelebrationInfo
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.MutedGold
import com.example.ui.theme.NightBlack
import com.example.ui.theme.PureBlack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class CelebrationParticle(
    val angle: Double,
    val distance: Double,
    val size: Float,
    val speed: Float,
    val color: Color
)

/**
 * Module Completion Celebration Dialog
 * Celebrates the mastery and completion reflection of a principle module
 * with rich animations, monk contemplative visual motif, radiant gold rays,
 * and XP celebration.
 */
@Composable
fun ModuleCompletionCelebrationDialog(
    info: ModuleCompletionCelebrationInfo,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val entranceScale = remember { Animatable(0.3f) }
    val sunburstRotation = remember { Animatable(0f) }
    val crestBounce = remember { Animatable(0.5f) }
    val haloExpansion = remember { Animatable(0f) }
    val particlesAnimation = remember { Animatable(0f) }
    var displayedXpCount by remember { mutableIntStateOf(0) }

    val particleList = remember {
        List(28) {
            val angle = Random.nextDouble(0.0, 360.0)
            val distance = Random.nextDouble(40.0, 170.0)
            val size = Random.nextDouble(3.0, 9.0)
            val speed = Random.nextDouble(0.8, 1.3)
            val color = if (Random.nextBoolean()) GoldLight else AmberBright
            CelebrationParticle(angle, distance, size.toFloat(), speed.toFloat(), color)
        }
    }

    LaunchedEffect(info) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
            crestBounce.animateTo(1.0f, animationSpec = tween(280))
        }
        launch {
            haloExpansion.animateTo(1f, animationSpec = tween(1300, easing = FastOutSlowInEasing))
        }
        launch {
            particlesAnimation.animateTo(1f, animationSpec = tween(1900, easing = FastOutSlowInEasing))
        }
        launch {
            sunburstRotation.animateTo(
                360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(15000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }

        // Count up animation to XP
        val targetXp = info.xpEarned
        val stepDelay = (600 / targetXp.coerceAtLeast(1)).coerceIn(10, 80).toLong()
        val stepSize = (targetXp / 25).coerceAtLeast(1)
        for (i in 0..targetXp step stepSize) {
            displayedXpCount = i
            delay(stepDelay)
        }
        displayedXpCount = targetXp
    }

    val infiniteTransition = rememberInfiniteTransition(label = "celebration_glow")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(PureBlack.copy(alpha = 0.88f))
                .clickable(onClick = onDismiss)
                .testTag("module_completion_celebration_dialog"),
            contentAlignment = Alignment.Center
        ) {
            // Main Modal Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .graphicsLayer {
                        scaleX = entranceScale.value
                        scaleY = entranceScale.value
                        alpha = entranceScale.value.coerceIn(0f, 1f)
                    }
                    .clickable(enabled = false) {}
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                GoldLight.copy(alpha = 0.9f),
                                AmberAccent.copy(alpha = 0.5f),
                                GoldDark.copy(alpha = 0.9f)
                            )
                        ),
                        shape = RoundedCornerShape(26.dp)
                    )
                    .shadow(
                        elevation = 32.dp,
                        shape = RoundedCornerShape(26.dp),
                        spotColor = GoldLight.copy(alpha = 0.45f)
                    ),
                shape = RoundedCornerShape(26.dp),
                color = NightBlack
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF1E170A),
                                    NightBlack,
                                    PureBlack
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    // Close Button in Corner
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(36.dp)
                            .background(DarkCharcoal.copy(alpha = 0.7f), CircleShape)
                            .testTag("celebration_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Celebration",
                            tint = MutedGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Monk & Sunburst Emblem
                        Box(
                            modifier = Modifier
                                .size(170.dp)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Rotating Sunburst Canvas
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        alpha = 0.75f * pulseGlow
                                    }
                            ) {
                                val center = Offset(size.width / 2f, size.height / 2f)
                                val radius = size.minDimension / 2f

                                rotate(sunburstRotation.value, pivot = center) {
                                    val rayCount = 18
                                    for (i in 0 until rayCount) {
                                        val angleDeg = i * (360f / rayCount)
                                        val angleRad = Math.toRadians(angleDeg.toDouble())
                                        val endX = center.x + radius * cos(angleRad).toFloat()
                                        val endY = center.y + radius * sin(angleRad).toFloat()

                                        drawLine(
                                            brush = Brush.linearGradient(
                                                listOf(
                                                    GoldLight.copy(alpha = 0.8f),
                                                    AmberAccent.copy(alpha = 0.3f),
                                                    Color.Transparent
                                                ),
                                                start = center,
                                                end = Offset(endX, endY)
                                            ),
                                            start = center,
                                            end = Offset(endX, endY),
                                            strokeWidth = 3f,
                                            cap = StrokeCap.Round
                                        )
                                    }
                                }

                                // Pulsing Aura Rings
                                val haloRadius = (radius * 0.75f) * haloExpansion.value
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        listOf(
                                            GoldLight.copy(alpha = 0.35f * (1f - haloExpansion.value * 0.5f)),
                                            AmberAccent.copy(alpha = 0.15f),
                                            Color.Transparent
                                        ),
                                        center = center,
                                        radius = haloRadius.coerceAtLeast(1f)
                                    ),
                                    center = center,
                                    radius = haloRadius
                                )

                                // Floating Gold Dust Particles
                                val particleProgress = particlesAnimation.value
                                particleList.forEach { particle ->
                                    val distance = particle.distance * particleProgress * particle.speed
                                    val rad = Math.toRadians(particle.angle)
                                    val px = center.x + (distance * cos(rad)).toFloat()
                                    val py = center.y + (distance * sin(rad)).toFloat()
                                    val pAlpha = (1f - (particleProgress * 0.7f)).coerceIn(0f, 1f)

                                    drawCircle(
                                        color = particle.color.copy(alpha = pAlpha),
                                        radius = particle.size * (1f - particleProgress * 0.3f),
                                        center = Offset(px, py)
                                    )
                                }
                            }

                            // Central Monk Medallion with Checkmark
                            Box(
                                modifier = Modifier
                                    .size(92.dp)
                                    .scale(crestBounce.value)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                GoldLight,
                                                AmberAccent,
                                                GoldDark
                                            )
                                        ),
                                        CircleShape
                                    )
                                    .border(2.5.dp, GoldLight, CircleShape)
                                    .shadow(16.dp, CircleShape, spotColor = AmberBright),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    // Monk Contemplation Motif Icon
                                    Icon(
                                        imageVector = Icons.Default.SelfImprovement,
                                        contentDescription = "Monk Mastery",
                                        tint = PureBlack,
                                        modifier = Modifier.size(34.dp)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Completed",
                                            tint = PureBlack,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "SEALED",
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PureBlack,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Category Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = AmberBright.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stars,
                                    contentDescription = null,
                                    tint = GoldLight,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "VAULT ${info.vaultOrder} CONQUERED",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight,
                                    letterSpacing = 1.2.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Module Principle Title
                        Text(
                            text = info.principleName,
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Text(
                            text = info.moduleTitle,
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            color = MutedGold,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // XP and Rewards Box
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = DarkCharcoal.copy(alpha = 0.85f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = AmberBright,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "+$displayedXpCount SOVEREIGN XP",
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AmberBright,
                                        letterSpacing = 1.sp
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = null,
                                        tint = GoldLight.copy(alpha = 0.8f),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Next Principle Vault Unlocked & Available",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = MutedGold,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    val formattedDate = remember(info.completedTimestamp) {
                                        SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())
                                            .format(Date(info.completedTimestamp))
                                    }
                                    Text(
                                        text = "Inscribed to Permanent History: $formattedDate",
                                        fontSize = 10.sp,
                                        color = MutedGold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Monk Inscription Quote
                        Text(
                            text = "“Whatever the mind can conceive and believe, it can achieve.”",
                            fontFamily = FontFamily.Serif,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color = MutedGold.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Continue Button
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("celebration_continue_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                GoldLight,
                                                AmberAccent,
                                                GoldDark
                                            )
                                        ),
                                        RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "CONTINUE JOURNEY",
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PureBlack,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = PureBlack,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
