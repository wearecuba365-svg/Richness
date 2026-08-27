package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardEntry
import com.example.data.model.LeaderboardMember
import com.example.data.model.LeaderboardMetric
import com.example.data.model.LeaderboardTimeframe
import com.example.data.model.ModuleEntity
import com.example.data.model.SovereignCommunityPeers
import com.example.data.model.UserProfileEntity
import com.example.ui.components.BrushedCard
import com.example.ui.components.LocalWindowSizeInfo
import com.example.ui.components.TierBadgeChip
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.BrushedCardGradient
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightAlabaster
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightCardSurface
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightIvory
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LeaderboardScreen(
    userProfile: UserProfileEntity?,
    modules: List<ModuleEntity>,
    selectedMetric: LeaderboardMetric,
    selectedTimeframe: LeaderboardTimeframe,
    onSelectMetric: (LeaderboardMetric) -> Unit,
    onSelectTimeframe: (LeaderboardTimeframe) -> Unit,
    onToggleOptIn: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val windowInfo = LocalWindowSizeInfo.current
    val haptic = LocalHapticFeedback.current
    val listState = rememberLazyListState()

    var showPrivacyDialog by remember { mutableStateOf(false) }

    val isOptedIn = userProfile?.isLeaderboardOptedIn ?: true
    val userDisplayName = if (!userProfile?.name.isNullOrBlank()) userProfile!!.name else "Sovereign Initiate"
    val completedModulesCount = modules.count { it.isCompleted }

    // Assemble current user's profile member representation
    val userMember = remember(userProfile, completedModulesCount, isOptedIn) {
        val xp = userProfile?.xpTotal ?: 0
        val streak = userProfile?.currentStreak ?: 1
        val bestStreak = userProfile?.bestStreak ?: streak
        val weeklyXp = if (xp > 0) ((xp * 0.42f).toInt() + streak * 40).coerceIn(60, xp) else 0
        val weeklyStreak = streak.coerceIn(1, 7)
        val weeklyModules = completedModulesCount.coerceIn(0, 3)

        LeaderboardMember(
            id = "current_user",
            displayName = userDisplayName,
            avatarInitial = if (userDisplayName.length >= 2) userDisplayName.take(2).uppercase() else "SI",
            avatarColorHex = "#D4AF37",
            tierTitle = userProfile?.tierName ?: "Novice",
            isCurrentUser = true,
            xpAllTime = xp,
            xpThisWeek = weeklyXp,
            streakDaysAllTime = maxOf(streak, bestStreak),
            streakDaysThisWeek = weeklyStreak,
            modulesCompletedAllTime = completedModulesCount,
            modulesCompletedThisWeek = weeklyModules,
            motto = if (!userProfile?.definiteChiefAim.isNullOrBlank()) {
                userProfile!!.definiteChiefAim.take(65) + if (userProfile.definiteChiefAim.length > 65) "..." else ""
            } else {
                "Transmuting desire into sovereign tangible empire."
            },
            isOptedIn = isOptedIn
        )
    }

    // Build ranked leaderboard list based on active metric & timeframe
    val (rankedEntries, userEntry) = remember(
        userMember,
        SovereignCommunityPeers.peers,
        selectedMetric,
        selectedTimeframe,
        isOptedIn
    ) {
        val allPeers = SovereignCommunityPeers.peers.map { it.copy(isCurrentUser = false) }
        val candidateMembers = if (isOptedIn) {
            allPeers + userMember
        } else {
            allPeers
        }

        fun getMetricValue(member: LeaderboardMember): Int {
            return when (selectedMetric) {
                LeaderboardMetric.XP -> if (selectedTimeframe == LeaderboardTimeframe.THIS_WEEK) member.xpThisWeek else member.xpAllTime
                LeaderboardMetric.STREAK -> if (selectedTimeframe == LeaderboardTimeframe.THIS_WEEK) member.streakDaysThisWeek else member.streakDaysAllTime
                LeaderboardMetric.MODULES -> if (selectedTimeframe == LeaderboardTimeframe.THIS_WEEK) member.modulesCompletedThisWeek else member.modulesCompletedAllTime
            }
        }

        fun formatMetric(value: Int): String {
            val numFormat = NumberFormat.getIntegerInstance(Locale.US)
            return when (selectedMetric) {
                LeaderboardMetric.XP -> "${numFormat.format(value)} XP"
                LeaderboardMetric.STREAK -> "$value Days"
                LeaderboardMetric.MODULES -> "$value / 13 Vaults"
            }
        }

        // Sort descending by selected metric, then secondary by all-time XP
        val sortedMembers = candidateMembers.sortedWith(
            compareByDescending<LeaderboardMember> { getMetricValue(it) }
                .thenByDescending { it.xpAllTime }
        )

        val entries = sortedMembers.mapIndexed { index, member ->
            val v = getMetricValue(member)
            LeaderboardEntry(
                rank = index + 1,
                member = member,
                metricValue = v,
                formattedMetricValue = formatMetric(v),
                isCurrentUser = member.isCurrentUser
            )
        }

        // Find user entry or calculate hypothetical standing if opted out
        val currentEntry = if (isOptedIn) {
            entries.firstOrNull { it.isCurrentUser }
        } else {
            val userVal = getMetricValue(userMember)
            val hypotheticalRank = sortedMembers.count { getMetricValue(it) > userVal } + 1
            LeaderboardEntry(
                rank = hypotheticalRank,
                member = userMember,
                metricValue = userVal,
                formattedMetricValue = formatMetric(userVal),
                isCurrentUser = true
            )
        }

        Pair(entries, currentEntry)
    }

    val topThree = remember(rankedEntries) { rankedEntries.take(3) }
    val remainingEntries = remember(rankedEntries) { rankedEntries.drop(3) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .testTag("leaderboard_scroll_column"),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 110.dp // Room for sticky "Your Rank" card
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item(key = "leaderboard_header") {
                LeaderboardHeader(
                    isDark = isDark,
                    isOptedIn = isOptedIn,
                    onNavigateBack = onNavigateBack,
                    onOpenPrivacyDialog = { showPrivacyDialog = true }
                )
            }

            // Metric & Timeframe Selector Controls
            item(key = "leaderboard_controls") {
                LeaderboardControls(
                    isDark = isDark,
                    selectedMetric = selectedMetric,
                    selectedTimeframe = selectedTimeframe,
                    onSelectMetric = { metric ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSelectMetric(metric)
                    },
                    onSelectTimeframe = { timeframe ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSelectTimeframe(timeframe)
                    }
                )
            }

            // Top 3 Podium Cards
            if (topThree.isNotEmpty()) {
                item(key = "leaderboard_podium") {
                    LeaderboardPodiumSection(
                        topThree = topThree,
                        selectedMetric = selectedMetric,
                        isDark = isDark
                    )
                }
            }

            // Guild Ranks Header (Ranks 4+)
            if (remainingEntries.isNotEmpty()) {
                item(key = "leaderboard_roster_title") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.MilitaryTech,
                                contentDescription = null,
                                tint = if (isDark) GoldLight else GoldDark,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "GLOBAL GUILD STANDINGS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = if (isDark) TextSecondary else LightTextSecondary
                            )
                        }

                        Text(
                            text = "${rankedEntries.size} Sovereign Initiates",
                            fontSize = 11.sp,
                            color = if (isDark) TextMuted else LightTextMuted
                        )
                    }
                }

                // Ranked List Items (4 and beyond)
                itemsIndexed(
                    items = remainingEntries,
                    key = { _, entry -> "entry_${entry.member.id}_${entry.rank}" }
                ) { _, entry ->
                    LeaderboardRankRow(
                        entry = entry,
                        selectedMetric = selectedMetric,
                        isDark = isDark
                    )
                }
            }
        }

        // Pinned / Sticky "Your Rank" Row at the Bottom
        if (userEntry != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                StickyUserRankBar(
                    userEntry = userEntry,
                    isOptedIn = isOptedIn,
                    selectedMetric = selectedMetric,
                    isDark = isDark,
                    onOpenPrivacyDialog = { showPrivacyDialog = true }
                )
            }
        }
    }

    // Privacy & Opt-Out Modal Dialog
    if (showPrivacyDialog) {
        LeaderboardPrivacyDialog(
            isOptedIn = isOptedIn,
            isDark = isDark,
            onToggleOptIn = {
                onToggleOptIn()
            },
            onDismiss = { showPrivacyDialog = false }
        )
    }
}

// -----------------------------------------------------------------------------
// HEADER SECTION
// -----------------------------------------------------------------------------

@Composable
private fun LeaderboardHeader(
    isDark: Boolean,
    isOptedIn: Boolean,
    onNavigateBack: () -> Unit,
    onOpenPrivacyDialog: () -> Unit
) {
    val tierTheme = LocalTierGoldTheme.current

    BrushedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_header_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isDark) SurfaceElevated else LightElevated)
                            .border(0.8.dp, if (isDark) DarkBorder else LightBorder, CircleShape)
                            .testTag("leaderboard_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isDark) GoldLight else GoldDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = if (isDark) GoldLight else GoldDark,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SOVEREIGN LEADERBOARD",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                letterSpacing = 1.sp,
                                color = if (isDark) GoldLight else GoldDark
                            )
                        }
                        Text(
                            text = "Guild Transmutation & Mastery Standings",
                            fontSize = 11.sp,
                            color = if (isDark) TextSecondary else LightTextSecondary
                        )
                    }
                }

                // Privacy Status Pill
                Surface(
                    color = if (isOptedIn) {
                        SuccessGreen.copy(alpha = if (isDark) 0.18f else 0.12f)
                    } else {
                        AmberAccent.copy(alpha = if (isDark) 0.20f else 0.14f)
                    },
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 0.8.dp,
                        color = if (isOptedIn) SuccessGreen.copy(alpha = 0.4f) else AmberAccent.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .clickable { onOpenPrivacyDialog() }
                        .testTag("leaderboard_privacy_status_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isOptedIn) Icons.Filled.Public else Icons.Filled.PublicOff,
                            contentDescription = null,
                            tint = if (isOptedIn) SuccessGreen else AmberAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isOptedIn) "PUBLIC" else "PRIVATE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = if (isOptedIn) SuccessGreen else AmberAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Guild Context Subtext
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isDark) DarkCharcoal.copy(alpha = 0.7f) else LightCardSurface)
                    .border(0.6.dp, if (isDark) DarkBorder else LightBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = if (isDark) AmberBright else GoldDark,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Napoleon Hill's 11th Principle: The Master Mind alliance multiplies energy. Measure your daily discipline alongside sovereign peers worldwide.",
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = if (isDark) TextSecondary else LightTextSecondary
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// CONTROLS: METRICS TABS & TIMEFRAME TOGGLE
// -----------------------------------------------------------------------------

@Composable
private fun LeaderboardControls(
    isDark: Boolean,
    selectedMetric: LeaderboardMetric,
    selectedTimeframe: LeaderboardTimeframe,
    onSelectMetric: (LeaderboardMetric) -> Unit,
    onSelectTimeframe: (LeaderboardTimeframe) -> Unit
) {
    val tierTheme = LocalTierGoldTheme.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Timeframe Pill Selector (This Week vs All-Time)
        Surface(
            color = if (isDark) DarkCharcoal else LightIvory,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LeaderboardTimeframe.values().forEach { timeframe ->
                    val isSelected = selectedTimeframe == timeframe
                    val bgBrush = if (isSelected) {
                        if (isDark) GoldLinearGradient else Brush.horizontalGradient(listOf(GoldDark, GoldPrimary))
                    } else {
                        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(bgBrush)
                            .clickable { onSelectTimeframe(timeframe) }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (timeframe == LeaderboardTimeframe.THIS_WEEK) Icons.Filled.TrendingUp else Icons.Filled.WorkspacePremium,
                                contentDescription = null,
                                tint = if (isSelected) {
                                    if (isDark) RichBlack else Color.White
                                } else {
                                    if (isDark) TextSecondary else LightTextSecondary
                                },
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = timeframe.title.uppercase(),
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                letterSpacing = 0.6.sp,
                                color = if (isSelected) {
                                    if (isDark) RichBlack else Color.White
                                } else {
                                    if (isDark) TextSecondary else LightTextSecondary
                                }
                            )
                        }
                    }
                }
            }
        }

        // Metric Selector Tabs (XP Total, Streak Length, Vaults Completed)
        Surface(
            color = if (isDark) DarkCharcoal else LightIvory,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            TabRow(
                selectedTabIndex = selectedMetric.ordinal,
                containerColor = Color.Transparent,
                contentColor = if (isDark) GoldLight else GoldDark,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedMetric.ordinal]),
                        height = 3.dp,
                        color = if (isDark) GoldLight else GoldDark
                    )
                },
                divider = {}
            ) {
                LeaderboardMetric.values().forEach { metric ->
                    val isSelected = selectedMetric == metric
                    val icon = when (metric) {
                        LeaderboardMetric.XP -> Icons.Filled.AutoAwesome
                        LeaderboardMetric.STREAK -> Icons.Filled.Whatshot
                        LeaderboardMetric.MODULES -> Icons.AutoMirrored.Filled.MenuBook
                    }

                    Tab(
                        selected = isSelected,
                        onClick = { onSelectMetric(metric) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) (if (isDark) GoldLight else GoldDark) else (if (isDark) TextMuted else LightTextMuted),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = metric.shortLabel,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) (if (isDark) GoldLight else GoldDark) else (if (isDark) TextMuted else LightTextMuted)
                                )
                            }
                        },
                        modifier = Modifier.testTag("leaderboard_tab_${metric.name.lowercase()}")
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// PODIUM SECTION (TOP 3 RANKS)
// -----------------------------------------------------------------------------

@Composable
private fun LeaderboardPodiumSection(
    topThree: List<LeaderboardEntry>,
    selectedMetric: LeaderboardMetric,
    isDark: Boolean
) {
    if (topThree.isEmpty()) return

    // Podium display order: #2 on left, #1 in center (tallest), #3 on right
    val rank1 = topThree.firstOrNull { it.rank == 1 }
    val rank2 = topThree.firstOrNull { it.rank == 2 }
    val rank3 = topThree.firstOrNull { it.rank == 3 }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_podium_section"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // #2 Silver (Left)
            Box(modifier = Modifier.weight(1f)) {
                if (rank2 != null) {
                    PodiumCard(
                        entry = rank2,
                        podiumRank = 2,
                        cardHeight = 180.dp,
                        primaryColor = Color(0xFFC0C0C0), // Silver
                        accentColor = Color(0xFFE0E0E0),
                        isDark = isDark,
                        selectedMetric = selectedMetric
                    )
                } else {
                    Spacer(modifier = Modifier.height(180.dp))
                }
            }

            // #1 Gold (Center, elevated)
            Box(modifier = Modifier.weight(1.15f)) {
                if (rank1 != null) {
                    PodiumCard(
                        entry = rank1,
                        podiumRank = 1,
                        cardHeight = 210.dp,
                        primaryColor = GoldLight,
                        accentColor = AmberBright,
                        isDark = isDark,
                        selectedMetric = selectedMetric
                    )
                }
            }

            // #3 Bronze (Right)
            Box(modifier = Modifier.weight(1f)) {
                if (rank3 != null) {
                    PodiumCard(
                        entry = rank3,
                        podiumRank = 3,
                        cardHeight = 165.dp,
                        primaryColor = Color(0xFFCD7F32), // Bronze
                        accentColor = Color(0xFFE5A65D),
                        isDark = isDark,
                        selectedMetric = selectedMetric
                    )
                } else {
                    Spacer(modifier = Modifier.height(165.dp))
                }
            }
        }
    }
}

@Composable
private fun PodiumCard(
    entry: LeaderboardEntry,
    podiumRank: Int,
    cardHeight: androidx.compose.ui.unit.Dp,
    primaryColor: Color,
    accentColor: Color,
    isDark: Boolean,
    selectedMetric: LeaderboardMetric
) {
    val tierTheme = LocalTierGoldTheme.current
    val infiniteTransition = rememberInfiniteTransition(label = "podium_shimmer")
    val crownScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crown_pulse"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) DarkCharcoal else LightIvory
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (entry.isCurrentUser) 2.dp else (if (podiumRank == 1) 1.5.dp else 1.dp),
            color = if (entry.isCurrentUser) GoldLight else primaryColor.copy(alpha = 0.7f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .shadow(
                elevation = if (podiumRank == 1) 10.dp else 5.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = primaryColor.copy(alpha = 0.35f)
            )
            .testTag("podium_rank_$podiumRank")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: Rank Crown / Crest
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .scale(if (podiumRank == 1) crownScale else 1f)
                        .size(if (podiumRank == 1) 32.dp else 26.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(primaryColor, primaryColor.copy(alpha = 0.2f))
                            )
                        )
                        .border(1.dp, primaryColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#$podiumRank",
                        fontSize = if (podiumRank == 1) 13.sp else 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RichBlack
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Avatar with Initial
                Box(
                    modifier = Modifier
                        .size(if (podiumRank == 1) 46.dp else 38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    primaryColor.copy(alpha = 0.9f),
                                    accentColor.copy(alpha = 0.7f)
                                )
                            )
                        )
                        .border(
                            width = if (podiumRank == 1) 2.dp else 1.2.dp,
                            color = primaryColor,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = entry.member.avatarInitial,
                        fontSize = if (podiumRank == 1) 16.sp else 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RichBlack
                    )
                }
            }

            // Middle: Name & Tier
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (entry.isCurrentUser) {
                    Surface(
                        color = GoldLight.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(0.6.dp, GoldLight)
                    ) {
                        Text(
                            text = "YOU",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) GoldLight else GoldDark,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Text(
                    text = entry.member.displayName,
                    fontSize = if (podiumRank == 1) 12.sp else 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) TextPrimary else LightTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = entry.member.tierTitle,
                    fontSize = 9.sp,
                    color = if (isDark) TextMuted else LightTextMuted,
                    maxLines = 1
                )
            }

            // Bottom: Metric Value Badge
            Surface(
                color = primaryColor.copy(alpha = if (isDark) 0.18f else 0.12f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(0.8.dp, primaryColor.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = entry.formattedMetricValue,
                    fontSize = if (podiumRank == 1) 11.sp else 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) primaryColor else GoldDark,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// RANK ROW (RANKS 4+)
// -----------------------------------------------------------------------------

@Composable
private fun LeaderboardRankRow(
    entry: LeaderboardEntry,
    selectedMetric: LeaderboardMetric,
    isDark: Boolean
) {
    val tierTheme = LocalTierGoldTheme.current
    val isUser = entry.isCurrentUser

    val borderStroke = if (isUser) {
        androidx.compose.foundation.BorderStroke(1.2.dp, GoldLight)
    } else {
        androidx.compose.foundation.BorderStroke(0.8.dp, if (isDark) DarkBorder else LightBorder)
    }

    val containerBg = if (isUser) {
        if (isDark) {
            SurfaceElevated.copy(alpha = 0.95f)
        } else {
            LightElevated
        }
    } else {
        if (isDark) DarkCharcoal else LightIvory
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerBg),
        border = borderStroke,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_${entry.rank}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Rank Number + Avatar + Identity
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Rank Number Box
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(if (isDark) DarkBorder.copy(alpha = 0.5f) else LightBorder.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#${entry.rank}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUser) (if (isDark) GoldLight else GoldDark) else (if (isDark) TextSecondary else LightTextSecondary)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            try {
                                Color(android.graphics.Color.parseColor(entry.member.avatarColorHex))
                            } catch (e: Exception) {
                                GoldDark
                            }
                        )
                        .border(
                            width = if (isUser) 1.5.dp else 0.8.dp,
                            color = if (isUser) GoldLight else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = entry.member.avatarInitial,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name, Title & Motto
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = entry.member.displayName,
                            fontSize = 13.5.sp,
                            fontWeight = if (isUser) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isDark) TextPrimary else LightTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (isUser) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = GoldLight.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp),
                                border = androidx.compose.foundation.BorderStroke(0.6.dp, GoldLight)
                            ) {
                                Text(
                                    text = "YOU",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDark) GoldLight else GoldDark,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TierBadgeChip(tier = entry.member.tierTitle)

                        Text(
                            text = entry.member.motto,
                            fontSize = 10.5.sp,
                            color = if (isDark) TextMuted else LightTextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right: Formatted Metric Badge
            Surface(
                color = if (isUser) GoldLight.copy(alpha = if (isDark) 0.20f else 0.12f) else (if (isDark) SurfaceElevated else LightElevated),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 0.8.dp,
                    color = if (isUser) GoldLight else (if (isDark) DarkBorder else LightBorder)
                )
            ) {
                Text(
                    text = entry.formattedMetricValue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUser) (if (isDark) GoldLight else GoldDark) else (if (isDark) TextPrimary else LightTextPrimary),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// PINNED / STICKY "YOUR RANK" BOTTOM ROW
// -----------------------------------------------------------------------------

@Composable
private fun StickyUserRankBar(
    userEntry: LeaderboardEntry,
    isOptedIn: Boolean,
    selectedMetric: LeaderboardMetric,
    isDark: Boolean,
    onOpenPrivacyDialog: () -> Unit
) {
    val tierTheme = LocalTierGoldTheme.current

    Surface(
        color = if (isDark) DarkCharcoal else LightIvory,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.5.dp,
            color = if (isOptedIn) GoldLight else AmberAccent
        ),
        shadowElevation = 14.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("sticky_user_rank_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            (if (isDark) GoldDark else AmberAccent).copy(alpha = if (isDark) 0.25f else 0.12f),
                            Color.Transparent
                        )
                    )
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Position and Name
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isOptedIn) (if (isDark) GoldDark else GoldPrimary) else AmberAccent),
                    contentAlignment = Alignment.Center
                ) {
                    if (isOptedIn) {
                        Text(
                            text = "#${userEntry.rank}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) GoldLight else Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Private",
                            tint = RichBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Your Standing",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = if (isDark) GoldLight else GoldDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        if (!isOptedIn) {
                            Surface(
                                color = AmberAccent.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp),
                                border = androidx.compose.foundation.BorderStroke(0.6.dp, AmberAccent)
                            ) {
                                Text(
                                    text = "PRIVATE MODE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberAccent,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = if (isOptedIn) {
                            "Rank #${userEntry.rank} • ${userEntry.member.displayName}"
                        } else {
                            "Rank #${userEntry.rank} (Hidden from others)"
                        },
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) TextPrimary else LightTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right: Stat & Privacy Settings Button
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = GoldLight.copy(alpha = if (isDark) 0.25f else 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight)
                ) {
                    Text(
                        text = userEntry.formattedMetricValue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) GoldLight else GoldDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onOpenPrivacyDialog,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = "Privacy Settings",
                        tint = if (isDark) TextSecondary else LightTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// PRIVACY & OPT-IN SETTINGS DIALOG
// -----------------------------------------------------------------------------

@Composable
private fun LeaderboardPrivacyDialog(
    isOptedIn: Boolean,
    isDark: Boolean,
    onToggleOptIn: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = if (isDark) DarkCharcoal else LightIvory,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = null,
                    tint = if (isDark) GoldLight else GoldDark,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Leaderboard Privacy",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = if (isDark) TextPrimary else LightTextPrimary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Control how your sovereign profile appears in community and guild rankings.",
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp,
                    color = if (isDark) TextSecondary else LightTextSecondary
                )

                // Toggle Row Card
                Surface(
                    color = if (isDark) SurfaceElevated else LightElevated,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Appear on Leaderboard",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) TextPrimary else LightTextPrimary
                            )
                            Text(
                                text = if (isOptedIn) {
                                    "Your display name, tier, and XP rank are visible to the guild."
                                } else {
                                    "You are hidden from public rankings. You can still view other leaders."
                                },
                                fontSize = 11.sp,
                                color = if (isDark) TextMuted else LightTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Switch(
                            checked = isOptedIn,
                            onCheckedChange = { onToggleOptIn() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = RichBlack,
                                checkedTrackColor = GoldLight,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = if (isDark) DarkBorder else LightBorder
                            ),
                            modifier = Modifier.testTag("leaderboard_privacy_switch")
                        )
                    }
                }

                // Privacy Guarantees Callout
                Surface(
                    color = (if (isDark) DarkBorder else LightBorder).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Zero Financial & Journal Exposure",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) TextPrimary else LightTextPrimary
                            )
                        }
                        Text(
                            text = "Only your chosen public Display Name, Tier, and high-level XP score are shared. Your Private Notebook entries, exact dollar figures, and Definite Chief Aim statements remain 100% confidential.",
                            fontSize = 10.5.sp,
                            lineHeight = 14.sp,
                            color = if (isDark) TextMuted else LightTextMuted
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    )
}
