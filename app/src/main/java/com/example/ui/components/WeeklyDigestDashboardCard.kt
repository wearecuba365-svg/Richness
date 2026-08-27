package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WeeklyProgressDigest
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Top Dashboard Card for the Weekly Progress Digest.
 * Displays automatically on generation days / when active,
 * featuring a plain-language summary, key number breakdown, and dismissibility.
 */
@Composable
fun WeeklyDigestDashboardCard(
    digest: WeeklyProgressDigest,
    onViewFullDigest: () -> Unit,
    onDismiss: () -> Unit,
    onShareImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("weekly_digest_dashboard_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Badge, Date Range & Dismiss Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = goldAccent.copy(alpha = if (isDark) 0.18f else 0.12f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.8.dp, goldAccent.copy(alpha = 0.6f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "👑", fontSize = 11.sp)
                            Text(
                                text = if (digest.isGenerationDay) "YOUR WEEK IN REVIEW" else "WEEKLY PROGRESS DIGEST",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Text(
                        text = digest.formattedDateRange,
                        fontSize = 11.sp,
                        color = textMutedColor
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(surfaceColor)
                        .testTag("dismiss_weekly_digest_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Dismiss",
                        tint = textMutedColor,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Plain-Language Summary Headline
            Text(
                text = digest.headlineSummary,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = textColor,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = digest.subHeadline,
                fontSize = 11.5.sp,
                color = textSecColor,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Key Numbers Breakdown Grid (4 Columns)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DigestMiniMetric(
                    icon = "📖",
                    title = "JOURNAL",
                    value = "${digest.journalEntriesCount}",
                    color = goldAccent,
                    modifier = Modifier.weight(1f)
                )

                DigestMiniMetric(
                    icon = "⚡",
                    title = "HABITS",
                    value = "${digest.habitsCompletedCount}",
                    color = if (digest.distinctHabitDays >= 5) SuccessGreen else goldAccent,
                    modifier = Modifier.weight(1f)
                )

                DigestMiniMetric(
                    icon = "🔥",
                    title = "STREAK",
                    value = "${digest.currentStreak}d",
                    color = AmberBright,
                    modifier = Modifier.weight(1f)
                )

                DigestMiniMetric(
                    icon = "✨",
                    title = "XP",
                    value = "+${digest.xpEarnedThisWeek}",
                    color = goldAccent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Actions: View Full Digest & Export Image
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onViewFullDigest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = goldAccent,
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("view_full_digest_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                        Text(
                            text = "VIEW FULL DIGEST",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                OutlinedButton(
                    onClick = onShareImage,
                    border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                    modifier = Modifier.testTag("share_digest_image_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Share", modifier = Modifier.size(14.dp))
                        Text(text = "SHARE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DigestMiniMetric(
    icon: String,
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder
    val textMutedColor = if (isDark) TextMuted else LightTextMuted

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = surfaceColor,
        border = BorderStroke(0.6.dp, cardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(text = icon, fontSize = 9.sp)
                Text(
                    text = title,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = textMutedColor,
                    letterSpacing = 0.3.sp,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

/**
 * Compact shortcut banner for Weekly Progress Digest to be used anywhere on Dashboard or Notebook.
 */
@Composable
fun WeeklyDigestShortcutBanner(
    onOpenDigest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenDigest() }
            .testTag("weekly_digest_shortcut_banner")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    color = goldAccent.copy(alpha = if (isDark) 0.18f else 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.8.dp, goldAccent.copy(alpha = 0.5f)),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "👑", fontSize = 16.sp)
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "WEEKLY PROGRESS DIGEST",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                            color = textColor,
                            letterSpacing = 0.4.sp
                        )
                    }
                    Text(
                        text = "Review habit consistency, XP velocity & milestone trends",
                        fontSize = 10.5.sp,
                        color = textSecColor
                    )
                }
            }

            Surface(
                color = goldAccent.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(0.6.dp, goldAccent.copy(alpha = 0.6f))
            ) {
                Text(
                    text = "VIEW",
                    color = goldAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

