package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Window Size Classes following Material 3 Adaptive standards.
 */
enum class WindowSize {
    COMPACT,   // Phones (< 600dp)
    MEDIUM,    // Foldables unfolded, small tablets (600dp - 839dp)
    EXPANDED   // Tablets, desktop, widescreen (>= 840dp)
}

data class WindowSizeInfo(
    val widthSize: WindowSize,
    val heightSize: WindowSize,
    val screenWidthDp: Dp,
    val screenHeightDp: Dp,
    val isLandscape: Boolean,
    val isSmallPhone: Boolean,       // < 390dp (e.g. 360dp compact phones)
    val isLargePhone: Boolean,       // 390dp - 599dp
    val isTablet: Boolean,           // 600dp - 1023dp
    val isDesktop: Boolean,          // >= 1024dp
    val isTabletOrFoldable: Boolean
)

val LocalWindowSizeInfo = compositionLocalOf {
    WindowSizeInfo(
        widthSize = WindowSize.COMPACT,
        heightSize = WindowSize.COMPACT,
        screenWidthDp = 360.dp,
        screenHeightDp = 640.dp,
        isLandscape = false,
        isSmallPhone = true,
        isLargePhone = false,
        isTablet = false,
        isDesktop = false,
        isTabletOrFoldable = false
    )
}

@Composable
fun rememberWindowSizeInfo(): WindowSizeInfo {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp

    val widthSize = when {
        configuration.screenWidthDp < 600 -> WindowSize.COMPACT
        configuration.screenWidthDp < 840 -> WindowSize.MEDIUM
        else -> WindowSize.EXPANDED
    }

    val heightSize = when {
        configuration.screenHeightDp < 480 -> WindowSize.COMPACT
        configuration.screenHeightDp < 900 -> WindowSize.MEDIUM
        else -> WindowSize.EXPANDED
    }

    val isSmallPhone = configuration.screenWidthDp < 390
    val isLargePhone = configuration.screenWidthDp in 390..599
    val isTablet = configuration.screenWidthDp in 600..1023
    val isDesktop = configuration.screenWidthDp >= 1024
    val isTabletOrFoldable = widthSize != WindowSize.COMPACT || (isLandscape && screenWidthDp >= 600.dp)

    return remember(configuration.screenWidthDp, configuration.screenHeightDp, isLandscape) {
        WindowSizeInfo(
            widthSize = widthSize,
            heightSize = heightSize,
            screenWidthDp = screenWidthDp,
            screenHeightDp = screenHeightDp,
            isLandscape = isLandscape,
            isSmallPhone = isSmallPhone,
            isLargePhone = isLargePhone,
            isTablet = isTablet,
            isDesktop = isDesktop,
            isTabletOrFoldable = isTabletOrFoldable
        )
    }
}

/**
 * Wraps content in a centered container with a maximum width on large screens
 * to avoid excessive stretching and maintain high-fidelity luxury typography and readability.
 */
@Composable
fun ResponsiveContentContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 860.dp,
    horizontalPadding: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = maxWidth)
                .padding(horizontal = horizontalPadding)
        ) {
            content()
        }
    }
}

/**
 * Adaptive Two-Pane Layout:
 * - On Compact screens (< 600dp): Renders vertically (single-column) with [firstPane] followed by [secondPane].
 * - On Medium & Expanded screens (>= 600dp): Renders side-by-side with proportional width weighting.
 */
@Composable
fun AdaptiveTwoPaneLayout(
    modifier: Modifier = Modifier,
    firstPaneWeight: Float = 1f,
    secondPaneWeight: Float = 1f,
    spacing: Dp = 18.dp,
    firstPane: @Composable () -> Unit,
    secondPane: @Composable () -> Unit
) {
    val windowInfo = LocalWindowSizeInfo.current

    if (windowInfo.isTabletOrFoldable) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .weight(firstPaneWeight)
                    .fillMaxHeight()
            ) {
                firstPane()
            }
            Box(
                modifier = Modifier
                    .weight(secondPaneWeight)
                    .fillMaxHeight()
            ) {
                secondPane()
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            firstPane()
            secondPane()
        }
    }
}

/**
 * Tap-Friendly Tooltip helper for touch devices.
 * Supports tap-and-hold (long press) and optional info icon tap so mobile users
 * can easily discover and read tooltip explanations.
 */
@Composable
fun TouchTooltipBox(
    tooltipTitle: String,
    tooltipContent: String,
    modifier: Modifier = Modifier,
    showInfoIcon: Boolean = false,
    content: @Composable () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val isDark = LocalIsDarkTheme.current

    Row(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onLongPress = { showDialog = true }
            )
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
        if (showInfoIcon) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { showDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Info: $tooltipTitle",
                    tint = if (isDark) GoldLight else GoldPrimary,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .widthIn(max = 380.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, if (isDark) DarkBorder else LightBorder, RoundedCornerShape(16.dp)),
                color = if (isDark) DarkCharcoal else LightElevated,
                shadowElevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tooltipTitle,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) GoldLight else GoldPrimary
                        )
                    }
                    Text(
                        text = tooltipContent,
                        fontSize = 12.sp,
                        color = if (isDark) TextSecondary else LightTextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}
