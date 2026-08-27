package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.UserProfileEntity
import com.example.data.model.VisionBoardItemEntity
import com.example.data.model.getStockPresetById
import com.example.data.repository.RichesRepository
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightCard
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Compact Dashboard Preview Widget displaying the Vision Board moodboard strip,
 * current vision streak, and quick launch into full board or 60s ritual.
 */
@Composable
fun VisionBoardDashboardWidget(
    visionItems: List<VisionBoardItemEntity>,
    userProfile: UserProfileEntity?,
    onOpenVisionBoard: () -> Unit,
    onStartContemplation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val todayEpochDay = remember { RichesRepository.getTodayEpochDay() }
    val hasContemplatedToday = (userProfile?.lastVisionBoardViewEpochDay ?: 0L) == todayEpochDay
    val visionStreak = userProfile?.visionBoardStreak ?: 0

    // Take first 4 items for thumbnail collage
    val previewItems = remember(visionItems) { visionItems.take(5) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) DarkCard else LightCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) DarkBorder else LightBorder
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onOpenVisionBoard() }
            .testTag("dashboard_vision_board_widget")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(tierTheme.goldPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = tierTheme.goldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "VISION BOARD",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = if (isDark) tierTheme.goldLight else tierTheme.goldDark
                            )
                            if (visionStreak > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "🔥 ${visionStreak}d",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = tierTheme.goldPrimary
                                )
                            }
                        }
                        Text(
                            text = "${visionItems.size} Goals Inscribed",
                            fontSize = 11.sp,
                            color = if (isDark) TextMuted else LightTextMuted
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "View All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = tierTheme.goldLight
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Vision Board",
                        tint = tierTheme.goldLight,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Thumbnail Strip / Collage
            if (previewItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDark) SurfaceElevated else LightElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = tierTheme.goldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tap to inscribe your first vision goal",
                            fontSize = 12.sp,
                            color = if (isDark) TextSecondary else LightTextSecondary
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    previewItems.forEach { item ->
                        VisionBoardCompactThumbnail(
                            item = item,
                            onClick = onOpenVisionBoard
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Daily Contemplation Quick Trigger
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (hasContemplatedToday) {
                            if (isDark) SurfaceElevated else LightElevated
                        } else {
                            tierTheme.goldPrimary.copy(alpha = 0.12f)
                        }
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (hasContemplatedToday) Icons.Filled.Check else Icons.Filled.SelfImprovement,
                        contentDescription = null,
                        tint = if (hasContemplatedToday) Color(0xFF10B981) else tierTheme.goldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (hasContemplatedToday) "Contemplated Today (+50 XP)" else "Daily 60s Visualization (+50 XP)",
                        fontSize = 11.sp,
                        fontWeight = if (hasContemplatedToday) FontWeight.Medium else FontWeight.Bold,
                        color = if (hasContemplatedToday) (if (isDark) TextSecondary else LightTextSecondary) else (if (isDark) tierTheme.goldLight else tierTheme.goldDark)
                    )
                }

                Button(
                    onClick = onStartContemplation,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasContemplatedToday) {
                            if (isDark) DarkCharcoal else LightCard
                        } else {
                            tierTheme.goldPrimary
                        },
                        contentColor = if (hasContemplatedToday) tierTheme.goldLight else RichBlack
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text(
                        text = if (hasContemplatedToday) "Review" else "Start 60s",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun VisionBoardCompactThumbnail(
    item: VisionBoardItemEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tierTheme = LocalTierGoldTheme.current
    val preset = remember(item.imageUrl) { getStockPresetById(item.imageUrl) }

    Box(
        modifier = modifier
            .size(width = 80.dp, height = 72.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, if (item.isPinned) tierTheme.goldPrimary else DarkBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        if (item.imageUrl.startsWith("http") || item.imageUrl.startsWith("content://") || item.imageUrl.startsWith("file://")) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (preset != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(preset.webImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkCharcoal),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.WorkspacePremium,
                    contentDescription = null,
                    tint = tierTheme.goldPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Gradient Darkener at Bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            RichBlack.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        // Caption label
        Text(
            text = item.title,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(4.dp)
        )

        if (item.isPinned) {
            Icon(
                imageVector = Icons.Filled.PushPin,
                contentDescription = "Pinned",
                tint = tierTheme.goldPrimary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .size(10.dp)
            )
        }
    }
}

/**
 * Daily In-App Nudge Banner encouraging 60s subconscious vision impression
 */
@Composable
fun DailyVisionNudgeBanner(
    userProfile: UserProfileEntity?,
    onStart60sRitual: () -> Unit,
    onDismissNudge: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val todayEpochDay = remember { RichesRepository.getTodayEpochDay() }
    val hasContemplatedToday = (userProfile?.lastVisionBoardViewEpochDay ?: 0L) == todayEpochDay

    if (hasContemplatedToday) return

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) DarkCard else LightCard
        ),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    tierTheme.goldPrimary,
                    tierTheme.goldLight
                )
            )
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_vision_nudge_banner")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(tierTheme.goldPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.SelfImprovement,
                        contentDescription = null,
                        tint = tierTheme.goldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Daily 60-Second Vision Ritual",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) tierTheme.goldLight else tierTheme.goldDark
                    )
                    Text(
                        text = "Imprint your desires on the subconscious mind (+50 XP).",
                        fontSize = 11.sp,
                        color = if (isDark) TextSecondary else LightTextSecondary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onStart60sRitual,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) tierTheme.goldPrimary else tierTheme.goldDark,
                        contentColor = if (isDark) RichBlack else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = "Imprint",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onDismissNudge,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Dismiss",
                        tint = if (isDark) TextMuted else LightTextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
