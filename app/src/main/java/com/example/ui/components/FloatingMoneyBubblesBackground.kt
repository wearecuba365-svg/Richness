package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.TierGoldTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Type of wealth / coin symbol rendered inside a floating bubble.
 */
enum class BubbleMoneySymbol {
    DOLLAR_COIN,
    TREASURY_MEDALLION,
    STACKED_COINS,
    DIAMOND_CREST
}

/**
 * Immutable particle state definition for a floating money bubble.
 */
private data class MoneyBubble(
    val id: Int,
    val xRatio: Float,
    val yStartRatio: Float,
    val baseRadiusDp: Float,
    val speedFactor: Float,
    val swayFrequency: Float,
    val swayAmplitudeDp: Float,
    val phaseOffset: Float,
    val symbolType: BubbleMoneySymbol,
    val colorVariant: Int
)

/**
 * Subtle animated background effect displaying floating translucent bubbles
 * containing gold money, coins, and wealth crests drifting gently upward.
 *
 * Runs strictly behind all interactive content with low opacity and high efficiency.
 */
@Composable
fun FloatingMoneyBubblesBackground(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tierTheme: TierGoldTheme = LocalTierGoldTheme.current
) {
    if (!enabled) return

    val isDark = LocalIsDarkTheme.current
    val windowInfo = LocalWindowSizeInfo.current

    // Adaptive bubble density: 8 bubbles on compact phones, 14 on larger screens for optimal 60/120fps performance
    val bubbleCount = if (windowInfo.isSmallPhone || windowInfo.screenWidthDp < 480.dp) 8 else 14

    val bubbles = remember(bubbleCount) {
        val symbols = BubbleMoneySymbol.values()
        List(bubbleCount) { index ->
            // Distribute across horizontal canvas evenly with pseudo-random offsets
            val segmentWidth = 0.90f / bubbleCount.toFloat()
            val xRatio = 0.05f + (index * segmentWidth) + (((index * 37) % 10) * 0.004f)
            val yStart = ((index * 0.271f + 0.13f) % 1.0f)

            // Varied sizes: small (16-20dp), medium (22-26dp), large (28-34dp)
            val sizeCategory = index % 3
            val radius = when (sizeCategory) {
                0 -> 16f + (index % 4) * 1.2f // Small
                1 -> 22f + (index % 3) * 1.5f // Medium
                else -> 28f + (index % 3) * 2.0f // Large
            }

            val speed = 0.85f + ((index * 13) % 7) * 0.08f // Gentle variation in upward speed
            val swayFreq = 1.2f + ((index * 19) % 5) * 0.3f
            val swayAmp = 10f + ((index * 23) % 4) * 4f // 10dp to 22dp sway

            MoneyBubble(
                id = index,
                xRatio = xRatio.coerceIn(0.04f, 0.94f),
                yStartRatio = yStart,
                baseRadiusDp = radius,
                speedFactor = speed,
                swayFrequency = swayFreq,
                swayAmplitudeDp = swayAmp,
                phaseOffset = (index * (1f / bubbleCount.toFloat())),
                symbolType = symbols[index % symbols.size],
                colorVariant = index % 4
            )
        }
    }

    // Infinite master progress clock (22 seconds per full ascension cycle)
    val infiniteTransition = rememberInfiniteTransition(label = "money_bubbles_transition")
    val masterProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bubbles_progress"
    )

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        if (canvasWidth <= 0 || canvasHeight <= 0) return@Canvas

        val baseGlowMultiplier = (tierTheme.glowAlpha * 0.85f).coerceIn(0.35f, 1.0f)
        val themeOpacityMod = if (isDark) 0.85f else 0.55f

        bubbles.forEach { bubble ->
            // Compute current upward progress (0f to 1f)
            val bubbleProgress = (bubble.yStartRatio - (masterProgress * bubble.speedFactor) + 10f) % 1f

            // Total vertical span covers extra padding beyond screen top & bottom for smooth enter/exit
            val totalSpan = canvasHeight + (bubble.baseRadiusDp * 4.dp.toPx())
            val yPos = (bubbleProgress * totalSpan) - (bubble.baseRadiusDp * 2.dp.toPx())

            // Gentle sine-wave horizontal drift
            val swayPx = bubble.swayAmplitudeDp.dp.toPx()
            val swayAngle = (bubbleProgress * 2f * PI.toFloat() * bubble.swayFrequency) + (bubble.id * 1.5f)
            val xPos = (bubble.xRatio * canvasWidth) + (sin(swayAngle) * swayPx)

            // Smooth fade-in near bottom (0.0 to 0.12) and fade-out near top (0.88 to 1.0)
            val verticalFade = when {
                bubbleProgress < 0.12f -> (bubbleProgress / 0.12f).coerceIn(0f, 1f)
                bubbleProgress > 0.88f -> ((1f - bubbleProgress) / 0.12f).coerceIn(0f, 1f)
                else -> 1f
            }

            // Individual bubble alpha modulated by theme and tier
            val bubbleAlpha = (0.24f * baseGlowMultiplier * themeOpacityMod * verticalFade).coerceIn(0.04f, 0.45f)
            val radiusPx = bubble.baseRadiusDp.dp.toPx()

            val primaryColor = when (bubble.colorVariant) {
                0 -> tierTheme.goldLight
                1 -> tierTheme.goldPrimary
                2 -> AmberBright
                else -> GoldChampagne
            }

            val centerOffset = Offset(xPos, yPos)

            // 1. Draw Translucent Bubble Outer Sphere & Glass Reflection
            drawMoneyBubbleSphere(
                center = centerOffset,
                radius = radiusPx,
                primaryColor = primaryColor,
                alpha = bubbleAlpha
            )

            // 2. Draw the Gold Money / Coin / Crest Icon inside the bubble
            drawMoneyCoinInsideBubble(
                center = centerOffset,
                radius = radiusPx * 0.62f,
                symbol = bubble.symbolType,
                primaryColor = primaryColor,
                alpha = bubbleAlpha * 1.25f
            )
        }
    }
}

/**
 * Draws the soft circular glassmorphic bubble with subtle golden rim and highlight.
 */
private fun DrawScope.drawMoneyBubbleSphere(
    center: Offset,
    radius: Float,
    primaryColor: Color,
    alpha: Float
) {
    // Soft radial ambient inner tint
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                primaryColor.copy(alpha = alpha * 0.35f),
                primaryColor.copy(alpha = alpha * 0.12f),
                Color.Transparent
            ),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )

    // Outer delicate golden rim
    drawCircle(
        color = primaryColor.copy(alpha = alpha * 0.70f),
        radius = radius,
        center = center,
        style = Stroke(width = 1.2.dp.toPx())
    )

    // Inner subtle glow border
    drawCircle(
        color = Color.White.copy(alpha = alpha * 0.30f),
        radius = (radius - 1.2.dp.toPx()).coerceAtLeast(1f),
        center = center,
        style = Stroke(width = 0.6.dp.toPx())
    )

    // Glossy top-left bubble highlight (glass refraction arc)
    val highlightOffset = Offset(center.x - radius * 0.35f, center.y - radius * 0.35f)
    val highlightRadius = radius * 0.28f
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = alpha * 0.85f),
                primaryColor.copy(alpha = alpha * 0.35f),
                Color.Transparent
            ),
            center = highlightOffset,
            radius = highlightRadius
        ),
        radius = highlightRadius,
        center = highlightOffset
    )
}

/**
 * Draws the crisp gold coin / money symbol centered cleanly inside the bubble.
 */
private fun DrawScope.drawMoneyCoinInsideBubble(
    center: Offset,
    radius: Float,
    symbol: BubbleMoneySymbol,
    primaryColor: Color,
    alpha: Float
) {
    val coinStroke = (radius * 0.10f).coerceIn(1.dp.toPx(), 2.2.dp.toPx())

    when (symbol) {
        BubbleMoneySymbol.DOLLAR_COIN -> {
            // Gold coin outer disc
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.35f),
                radius = radius,
                center = center
            )
            // Coin edge milling ring
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.90f),
                radius = radius,
                center = center,
                style = Stroke(width = coinStroke)
            )
            // Inner coin beaded ring
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.50f),
                radius = radius * 0.76f,
                center = center,
                style = Stroke(width = coinStroke * 0.7f)
            )
            // Centered Dollar Sign Glyph ($) drawn geometrically with smooth vector lines
            drawDollarSignGlyph(
                center = center,
                height = radius * 1.1f,
                color = primaryColor.copy(alpha = alpha * 0.95f),
                strokeWidth = coinStroke * 1.1f
            )
        }

        BubbleMoneySymbol.TREASURY_MEDALLION -> {
            // Concentric royal coin disc
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.30f),
                radius = radius,
                center = center
            )
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.85f),
                radius = radius,
                center = center,
                style = Stroke(width = coinStroke)
            )
            // Inner 8-point geometric wealth star
            drawWealthStar(
                center = center,
                outerRadius = radius * 0.65f,
                innerRadius = radius * 0.30f,
                color = primaryColor.copy(alpha = alpha * 0.90f)
            )
            // Central core dot
            drawCircle(
                color = Color.White.copy(alpha = alpha * 0.80f),
                radius = radius * 0.16f,
                center = center
            )
        }

        BubbleMoneySymbol.STACKED_COINS -> {
            val offsetShift = radius * 0.28f
            val coinRad = radius * 0.75f

            // Back coin
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.25f),
                radius = coinRad,
                center = Offset(center.x - offsetShift, center.y - offsetShift)
            )
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.65f),
                radius = coinRad,
                center = Offset(center.x - offsetShift, center.y - offsetShift),
                style = Stroke(width = coinStroke * 0.8f)
            )

            // Front coin
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.40f),
                radius = coinRad,
                center = Offset(center.x + offsetShift * 0.6f, center.y + offsetShift * 0.6f)
            )
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.90f),
                radius = coinRad,
                center = Offset(center.x + offsetShift * 0.6f, center.y + offsetShift * 0.6f),
                style = Stroke(width = coinStroke)
            )
            // Dollar glyph on front coin
            drawDollarSignGlyph(
                center = Offset(center.x + offsetShift * 0.6f, center.y + offsetShift * 0.6f),
                height = coinRad * 1.0f,
                color = primaryColor.copy(alpha = alpha * 0.95f),
                strokeWidth = coinStroke * 0.9f
            )
        }

        BubbleMoneySymbol.DIAMOND_CREST -> {
            // Diamond crest coin ring
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.28f),
                radius = radius,
                center = center
            )
            drawCircle(
                color = primaryColor.copy(alpha = alpha * 0.80f),
                radius = radius,
                center = center,
                style = Stroke(width = coinStroke)
            )
            // Faceted diamond silhouette inside coin
            drawDiamondFacetedIcon(
                center = center,
                size = radius * 1.15f,
                color = primaryColor.copy(alpha = alpha * 0.90f),
                strokeWidth = coinStroke
            )
        }
    }
}

/**
 * Draws a clean vector Dollar Sign ($) consisting of the curved S-path and vertical spine.
 */
private fun DrawScope.drawDollarSignGlyph(
    center: Offset,
    height: Float,
    color: Color,
    strokeWidth: Float
) {
    val halfH = height * 0.5f
    val halfW = height * 0.28f

    // 1. Vertical center line spine
    drawLine(
        color = color,
        start = Offset(center.x, center.y - halfH * 1.15f),
        end = Offset(center.x, center.y + halfH * 1.15f),
        strokeWidth = strokeWidth * 0.85f,
        cap = StrokeCap.Round
    )

    // 2. Smooth S curve path
    val sPath = Path().apply {
        // Top right curve of S
        moveTo(center.x + halfW * 0.85f, center.y - halfH * 0.55f)
        cubicTo(
            center.x + halfW * 0.85f, center.y - halfH * 0.95f,
            center.x - halfW * 0.85f, center.y - halfH * 0.95f,
            center.x - halfW * 0.85f, center.y - halfH * 0.45f
        )
        // Middle cross
        cubicTo(
            center.x - halfW * 0.85f, center.y - halfH * 0.05f,
            center.x + halfW * 0.85f, center.y + halfH * 0.05f,
            center.x + halfW * 0.85f, center.y + halfH * 0.45f
        )
        // Bottom loop
        cubicTo(
            center.x + halfW * 0.85f, center.y + halfH * 0.95f,
            center.x - halfW * 0.85f, center.y + halfH * 0.95f,
            center.x - halfW * 0.85f, center.y + halfH * 0.55f
        )
    }

    drawPath(
        path = sPath,
        color = color,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
}

/**
 * Draws a radiant 8-point wealth star.
 */
private fun DrawScope.drawWealthStar(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    color: Color
) {
    val path = Path()
    val numPoints = 8
    val angleStep = (PI.toFloat() * 2f) / (numPoints * 2f)

    for (i in 0 until numPoints * 2) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        val angle = i * angleStep - (PI.toFloat() / 2f)
        val x = center.x + cos(angle) * r
        val y = center.y + sin(angle) * r
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()

    drawPath(
        path = path,
        color = color
    )
}

/**
 * Draws a faceted luxury diamond icon inside the coin.
 */
private fun DrawScope.drawDiamondFacetedIcon(
    center: Offset,
    size: Float,
    color: Color,
    strokeWidth: Float
) {
    val halfW = size * 0.48f
    val topY = center.y - size * 0.35f
    val girdleY = center.y - size * 0.08f
    val bottomY = center.y + size * 0.45f
    val tableHalfW = halfW * 0.55f

    val outlinePath = Path().apply {
        moveTo(center.x - tableHalfW, topY)
        lineTo(center.x + tableHalfW, topY)
        lineTo(center.x + halfW, girdleY)
        lineTo(center.x, bottomY)
        lineTo(center.x - halfW, girdleY)
        close()
    }

    // Outer contour
    drawPath(
        path = outlinePath,
        color = color,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )

    // Inner facets
    drawLine(
        color = color.copy(alpha = color.alpha * 0.7f),
        start = Offset(center.x - halfW, girdleY),
        end = Offset(center.x + halfW, girdleY),
        strokeWidth = strokeWidth * 0.7f
    )
    drawLine(
        color = color.copy(alpha = color.alpha * 0.7f),
        start = Offset(center.x - tableHalfW, topY),
        end = Offset(center.x, bottomY),
        strokeWidth = strokeWidth * 0.7f
    )
    drawLine(
        color = color.copy(alpha = color.alpha * 0.7f),
        start = Offset(center.x + tableHalfW, topY),
        end = Offset(center.x, bottomY),
        strokeWidth = strokeWidth * 0.7f
    )
}
