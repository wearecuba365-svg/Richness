package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldPrimary
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
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LeaderboardPreviewWidget(
    userProfile: UserProfileEntity?,
    modules: List<ModuleEntity>,
    onOpenLeaderboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val isOptedIn = userProfile?.isLeaderboardOptedIn ?: true
    val userDisplayName = if (!userProfile?.name.isNullOrBlank()) userProfile!!.name else "Sovereign Initiate"
    val completedCount = modules.count { it.isCompleted }

    // Prepare current user member
    val userMember = remember(userProfile, completedCount, isOptedIn) {
        val xp = userProfile?.xpTotal ?: 0
        val streak = userProfile?.currentStreak ?: 1
        val weeklyXp = if (xp > 0) ((xp * 0.42f).toInt() + streak * 40).coerceIn(60, xp) else 0

        LeaderboardMember(
            id = "current_user",
            displayName = userDisplayName,
            avatarInitial = if (userDisplayName.length >= 2) userDisplayName.take(2).uppercase() else "SI",
            avatarColorHex = "#D4AF37",
            tierTitle = userProfile?.tierName ?: "Novice",
            isCurrentUser = true,
            xpAllTime = xp,
            xpThisWeek = weeklyXp,
            streakDaysAllTime = maxOf(streak, userProfile?.bestStreak ?: streak),
            streakDaysThisWeek = streak.coerceIn(1, 7),
            modulesCompletedAllTime = completedCount,
            modulesCompletedThisWeek = completedCount.coerceIn(0, 3),
            isOptedIn = isOptedIn
        )
    }

    // Top 3 peers & user entry
    val (topThree, userEntry) = remember(userMember, isOptedIn) {
        val allPeers = SovereignCommunityPeers.peers.map { it.copy(isCurrentUser = false) }
        val candidateMembers = if (isOptedIn) allPeers + userMember else allPeers
        val sorted = candidateMembers.sortedByDescending { it.xpThisWeek }

        val numFormat = NumberFormat.getIntegerInstance(Locale.US)
        val entries = sorted.mapIndexed { index, m ->
            LeaderboardEntry(
                rank = index + 1,
                member = m,
                metricValue = m.xpThisWeek,
                formattedMetricValue = "${numFormat.format(m.xpThisWeek)} XP",
                isCurrentUser = m.isCurrentUser
            )
        }

        val top3 = entries.take(3)
        val uEntry = if (isOptedIn) {
            entries.firstOrNull { it.isCurrentUser }
        } else {
            val hypotheticalRank = sorted.count { it.xpThisWeek > userMember.xpThisWeek } + 1
            LeaderboardEntry(
                rank = hypotheticalRank,
                member = userMember,
                metricValue = userMember.xpThisWeek,
                formattedMetricValue = "${numFormat.format(userMember.xpThisWeek)} XP",
                isCurrentUser = true
            )
        }

        Pair(top3, uEntry)
    }

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("leaderboard_preview_widget")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(GoldDark.copy(alpha = 0.5f), AmberBright.copy(alpha = 0.3f))
                                )
                            )
                            .border(1.dp, GoldLight.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EmojiEvents,
                            contentDescription = null,
                            tint = if (isDark) GoldLight else GoldDark,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "SOVEREIGN STANDINGS",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            letterSpacing = 0.8.sp,
                            color = if (isDark) GoldLight else GoldDark
                        )
                        Text(
                            text = "Weekly Transmutation Ranks",
                            fontSize = 10.sp,
                            color = if (isDark) TextSecondary else LightTextSecondary
                        )
                    }
                }

                Surface(
                    color = (if (isDark) GoldDark else GoldPrimary).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(0.6.dp, GoldLight.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "THIS WEEK",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) GoldLight else GoldDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Top 3 Mini Leaderboard Rows
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                topThree.forEach { entry ->
                    val rankColor = when (entry.rank) {
                        1 -> GoldLight
                        2 -> Color(0xFFC0C0C0)
                        else -> Color(0xFFCD7F32)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (entry.isCurrentUser) {
                                    (if (isDark) GoldLight else GoldDark).copy(alpha = 0.12f)
                                } else {
                                    if (isDark) DarkCharcoal.copy(alpha = 0.6f) else LightCardSurface
                                }
                            )
                            .border(
                                width = if (entry.isCurrentUser) 1.dp else 0.4.dp,
                                color = if (entry.isCurrentUser) GoldLight else (if (isDark) DarkBorder else LightBorder),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "#${entry.rank}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = rankColor,
                                modifier = Modifier.width(24.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        try {
                                            Color(android.graphics.Color.parseColor(entry.member.avatarColorHex))
                                        } catch (e: Exception) {
                                            GoldDark
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = entry.member.avatarInitial,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = entry.member.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (entry.isCurrentUser) FontWeight.Bold else FontWeight.Medium,
                                color = if (isDark) TextPrimary else LightTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.widthIn(max = 130.dp)
                            )

                            if (entry.isCurrentUser) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    color = GoldLight.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "YOU",
                                        fontSize = 7.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isDark) GoldLight else GoldDark,
                                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = entry.formattedMetricValue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) GoldLight else GoldDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // User's Standing Card (if outside top 3 or privacy active)
            if (userEntry != null && (userEntry.rank > 3 || !isOptedIn)) {
                Surface(
                    color = (if (isDark) SurfaceElevated else LightElevated).copy(alpha = 0.9f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isOptedIn) GoldLight.copy(alpha = 0.6f) else AmberAccent.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isOptedIn) Icons.Filled.MilitaryTech else Icons.Filled.Lock,
                                contentDescription = null,
                                tint = if (isOptedIn) GoldLight else AmberAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isOptedIn) {
                                    "Your Rank: #${userEntry.rank} • ${userDisplayName}"
                                } else {
                                    "Your Standing: Rank #${userEntry.rank} (Private)"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) TextPrimary else LightTextPrimary
                            )
                        }

                        Text(
                            text = userEntry.formattedMetricValue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDark) GoldLight else GoldDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // Expand to Full Leaderboard CTA Button
            Surface(
                color = if (isDark) DarkCharcoal else LightIvory,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(0.8.dp, if (isDark) DarkBorder else LightBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenLeaderboard() }
                    .testTag("view_full_leaderboard_button")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 9.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "View Full Leaderboard Standings",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) GoldLight else GoldDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Leaderboard",
                        tint = if (isDark) GoldLight else GoldDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
