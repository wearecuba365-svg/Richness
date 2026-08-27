package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BadgeEntity
import com.example.data.model.DailyHabitEntity
import com.example.data.model.DailyHabitLogEntity
import com.example.data.model.GivingGoalEntity
import com.example.data.model.GivingLogEntity
import com.example.data.model.MastermindCheckinEntity
import com.example.data.model.ModuleEntity
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WealthGoalEntity
import com.example.data.model.WealthGoalLogEntity
import com.example.data.model.WeeklyDigestAggregator
import com.example.data.model.WeeklyProgressDigest
import com.example.ui.components.BrushedCard
import com.example.ui.components.LocalWindowSizeInfo
import com.example.ui.theme.AmberAccent
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
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

/**
 * Dedicated Weekly Progress Digest Screen:
 * - Plain-language summary sentence at the top
 * - Simple breakdown of the numbers underneath (Journal, Habits, Streaks, XP, Goals, Badges)
 * - Past Digests history view to scroll back through previous weeks
 * - Export and share as high-resolution luxury image card
 */
@Composable
fun WeeklyDigestScreen(
    userProfile: UserProfileEntity?,
    notebookEntries: List<NotebookEntryEntity>,
    allHabitLogs: List<DailyHabitLogEntity>,
    habits: List<DailyHabitEntity>,
    wealthGoal: WealthGoalEntity?,
    wealthGoalLogs: List<WealthGoalLogEntity>,
    givingGoal: GivingGoalEntity?,
    givingLogs: List<GivingLogEntity>,
    badges: List<BadgeEntity>,
    modules: List<ModuleEntity>,
    mastermindCheckins: List<MastermindCheckinEntity>,
    onShareDigestImage: (WeeklyProgressDigest) -> Unit,
    onBack: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val windowInfo = LocalWindowSizeInfo.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    // Selected week offset (0 = current week, 1 = last week, etc.)
    var selectedWeekOffset by remember { mutableIntStateOf(0) }

    // Pre-calculate past 8 weeks of digests
    val pastDigests = remember(
        userProfile, notebookEntries, allHabitLogs, habits,
        wealthGoal, wealthGoalLogs, givingGoal, givingLogs, badges, modules, mastermindCheckins
    ) {
        WeeklyDigestAggregator.compilePastDigests(
            count = 8,
            userProfile = userProfile,
            notebookEntries = notebookEntries,
            allHabitLogs = allHabitLogs,
            habits = habits,
            wealthGoal = wealthGoal,
            wealthGoalLogs = wealthGoalLogs,
            givingGoal = givingGoal,
            givingLogs = givingLogs,
            badges = badges,
            modules = modules,
            mastermindCheckins = mastermindCheckins
        )
    }

    val currentDigest = pastDigests.getOrElse(selectedWeekOffset) { pastDigests.first() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("weekly_digest_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // 1. Top Header Bar with Back and Share Image Action
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(surfaceColor)
                                .border(1.dp, cardBorderColor, CircleShape)
                                .testTag("weekly_digest_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = goldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "WEEKLY PROGRESS DIGEST",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = textColor,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Text(
                                text = "Sovereign Transmutation & Habit Summary",
                                fontSize = 11.sp,
                                color = textSecColor
                            )
                        }
                    }

                    // Share Image Button
                    Button(
                        onClick = { onShareDigestImage(currentDigest) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = goldAccent,
                            contentColor = RichBlack
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("header_share_digest_image_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Text(text = "SHARE IMAGE", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 2. Week Selector / Past Digests History Carousel
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = null,
                                tint = goldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "PAST DIGESTS HISTORY",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = textMutedColor,
                                letterSpacing = 0.8.sp
                            )
                        }

                        // Navigation arrows
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = {
                                    if (selectedWeekOffset < pastDigests.lastIndex) {
                                        selectedWeekOffset++
                                    }
                                },
                                enabled = selectedWeekOffset < pastDigests.lastIndex,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ChevronLeft,
                                    contentDescription = "Previous Week",
                                    tint = if (selectedWeekOffset < pastDigests.lastIndex) goldAccent else textMutedColor.copy(alpha = 0.4f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (selectedWeekOffset > 0) {
                                        selectedWeekOffset--
                                    }
                                },
                                enabled = selectedWeekOffset > 0,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ChevronRight,
                                    contentDescription = "Next Week",
                                    tint = if (selectedWeekOffset > 0) goldAccent else textMutedColor.copy(alpha = 0.4f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(pastDigests) { index, digest ->
                            val isSelected = selectedWeekOffset == index
                            val label = when (index) {
                                0 -> "This Week"
                                1 -> "Last Week"
                                else -> "${index}w Ago"
                            }

                            Surface(
                                color = if (isSelected) goldAccent else surfaceColor,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) goldAccent else cardBorderColor
                                ),
                                modifier = Modifier
                                    .clickable { selectedWeekOffset = index }
                                    .testTag("week_selector_tab_$index")
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSelected) RichBlack else textColor,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Text(
                                        text = digest.formattedDateRange.split("–").firstOrNull()?.trim() ?: "",
                                        color = if (isSelected) RichBlack.copy(alpha = 0.7f) else textMutedColor,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Hero Plain-Language Summary Card
            item {
                BrushedCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = goldAccent.copy(alpha = if (isDark) 0.2f else 0.12f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(0.8.dp, goldAccent.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = "👑", fontSize = 11.sp)
                                    Text(
                                        text = currentDigest.performanceTierTag.uppercase(Locale.US),
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = goldAccent,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Text(
                                text = currentDigest.formattedDateRange,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = goldAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Plain-Language Summary Headline
                        Text(
                            text = currentDigest.headlineSummary,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = textColor,
                            lineHeight = 23.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentDigest.subHeadline,
                            fontSize = 12.sp,
                            color = textSecColor,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // 4. Breakdown of the Numbers Underneath
            if (windowInfo.isTabletOrFoldable) {
                // Adaptive 2-column layout for tablets
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            JournalBreakdownCard(currentDigest)
                            HabitsBreakdownCard(currentDigest)
                            StreakBreakdownCard(currentDigest)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            XpBreakdownCard(currentDigest)
                            GoalsBreakdownCard(currentDigest)
                            BadgesBreakdownCard(currentDigest)
                        }
                    }
                }
            } else {
                // Mobile stacked layout
                item { JournalBreakdownCard(currentDigest) }
                item { HabitsBreakdownCard(currentDigest) }
                item { GoalsBreakdownCard(currentDigest) }
                item { XpBreakdownCard(currentDigest) }
                item { StreakBreakdownCard(currentDigest) }
                item { BadgesBreakdownCard(currentDigest) }
            }

            // 5. Napoleon Hill Weekly Law Card
            item {
                BrushedCard {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📜", fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
                        Column {
                            Text(
                                text = "\"Riches do not respond to wishful thinking. They respond only to definite plans, backed by definite desires, through constant PERSISTENCE.\"",
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic,
                                fontSize = 12.5.sp,
                                color = textColor,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Napoleon Hill • Think and Grow Rich",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = goldAccent
                            )
                        }
                    }
                }
            }

            // 6. Bottom Export & Share Button
            item {
                Button(
                    onClick = { onShareDigestImage(currentDigest) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = goldAccent,
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("bottom_share_digest_image_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text(
                            text = "EXPORT & SHARE DIGEST IMAGE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(36.dp)) }
        }
    }
}

// -------------------------------------------------------------
// Component Breakdown Cards
// -------------------------------------------------------------

@Composable
private fun JournalBreakdownCard(digest: WeeklyProgressDigest) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark

    BrushedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "📖", fontSize = 16.sp)
                    Text(
                        text = "JOURNAL & CODEX ENTRIES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = "${digest.journalEntriesCount} Entries",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DigestSubMetric(
                    label = "Decisions",
                    value = "${digest.decisionCount}",
                    modifier = Modifier.weight(1f)
                )
                DigestSubMetric(
                    label = "Fear Reframes",
                    value = "${digest.fearReframeCount}",
                    modifier = Modifier.weight(1f)
                )
                DigestSubMetric(
                    label = "Reflections",
                    value = "${(digest.journalEntriesCount - digest.decisionCount - digest.fearReframeCount).coerceAtLeast(0)}",
                    modifier = Modifier.weight(1f)
                )
            }

            if (digest.journalHighlights.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Recent: ${digest.journalHighlights.joinToString(" • ")}",
                    fontSize = 10.sp,
                    color = textMutedColor,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun HabitsBreakdownCard(digest: WeeklyProgressDigest) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark

    BrushedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "⚡", fontSize = 16.sp)
                    Text(
                        text = "DAILY HABITS & RITUALS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = "${digest.habitsCompletedCount} Done",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (digest.distinctHabitDays >= 5) SuccessGreen else goldAccent
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weekly Consistency: ${digest.distinctHabitDays}/7 Days",
                    fontSize = 10.5.sp,
                    color = textSecColor
                )
                Text(
                    text = "${(digest.habitCompletionRate * 100).toInt()}% Rate",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { digest.habitCompletionRate },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = goldAccent,
                trackColor = goldAccent.copy(alpha = 0.2f),
            )
        }
    }
}

@Composable
private fun GoalsBreakdownCard(digest: WeeklyProgressDigest) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark

    BrushedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "🎯", fontSize = 16.sp)
                    Text(
                        text = "GOALS & TRANSMUTATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = if (digest.wealthContributedAmount > 0) "+$${String.format(Locale.US, "%,.0f", digest.wealthContributedAmount)}" else "${digest.wealthContributionsCount} Logs",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DigestSubMetric(
                    label = "Wealth Inflows",
                    value = "${digest.wealthContributionsCount}",
                    modifier = Modifier.weight(1f)
                )
                DigestSubMetric(
                    label = "Mastermind Check-in",
                    value = if (digest.mastermindCheckinSubmitted) "Inscribed ✓" else "Pending",
                    modifier = Modifier.weight(1f)
                )
                DigestSubMetric(
                    label = "Giving / Service",
                    value = "${digest.givingLogsCount}",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun XpBreakdownCard(digest: WeeklyProgressDigest) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark

    BrushedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "✨", fontSize = 16.sp)
                    Text(
                        text = "XP & TIER PROGRESS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = "+${digest.xpEarnedThisWeek} XP",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Rank Standing: ${digest.currentTier} Tier • Consistent actions compound your sovereignty.",
                fontSize = 10.5.sp,
                color = textSecColor
            )
        }
    }
}

@Composable
private fun StreakBreakdownCard(digest: WeeklyProgressDigest) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark

    BrushedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "🔥", fontSize = 16.sp)
                    Text(
                        text = "SOVEREIGN STREAK",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = "${digest.currentStreak} Days",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberBright
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "All-Time Best Streak: ${digest.bestStreak} Days • Keep the chain unbroken.",
                fontSize = 10.5.sp,
                color = textSecColor
            )
        }
    }
}

@Composable
private fun BadgesBreakdownCard(digest: WeeklyProgressDigest) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark

    BrushedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "👑", fontSize = 16.sp)
                    Text(
                        text = "BADGES & ACCOLADES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = if (digest.badgesUnlockedThisWeek.isNotEmpty()) "${digest.badgesUnlockedThisWeek.size} New" else "Active",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (digest.badgesUnlockedThisWeek.isNotEmpty()) {
                Text(
                    text = "Unlocked: " + digest.badgesUnlockedThisWeek.joinToString(", ") { it.title },
                    fontSize = 10.5.sp,
                    color = textSecColor
                )
            } else {
                Text(
                    text = "Continuing progress toward next tier badges and mastery codex.",
                    fontSize = 10.5.sp,
                    color = textSecColor
                )
            }
        }
    }
}

@Composable
private fun DigestSubMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val textColor = if (isDark) TextPrimary else LightTextPrimary

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = surfaceColor,
        border = BorderStroke(0.6.dp, cardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 8.sp,
                color = textMutedColor,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1
            )
        }
    }
}
