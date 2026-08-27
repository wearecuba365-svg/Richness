package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MastermindCheckinEntity
import com.example.data.model.MastermindGroupEntity
import com.example.data.model.MastermindMemberEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.components.BrushedCard
import com.example.ui.components.TierBadgeChip
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MasterMindCircleScreen(
    userProfile: UserProfileEntity?,
    userGroup: MastermindGroupEntity?,
    allGroups: List<MastermindGroupEntity>,
    groupMembers: List<MastermindMemberEntity>,
    weeklyCheckins: List<MastermindCheckinEntity>,
    allGroupCheckins: List<MastermindCheckinEntity>,
    showJoinDialog: Boolean,
    showCreateDialog: Boolean,
    showWeeklyCheckinDialog: Boolean,
    inviteCodeInput: String,
    onBack: () -> Unit,
    onNavigateToLeaderboard: () -> Unit = {},
    onSetShowJoinDialog: (Boolean) -> Unit,
    onSetShowCreateDialog: (Boolean) -> Unit,
    onSetShowWeeklyCheckinDialog: (Boolean) -> Unit,
    onSetInviteCodeInput: (String) -> Unit,
    onJoinByInviteCode: (String) -> Unit,
    onAutoMatch: () -> Unit,
    onCreateCircle: (name: String, motto: String, tier: String) -> Unit,
    onLeaveCircle: () -> Unit,
    onSubmitWeeklyCheckin: (goal: String, status: String, note: String) -> Unit,
    onToggleReaction: (checkinId: String, reactionType: String) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    var selectedFeedTab by remember { mutableIntStateOf(0) } // 0 = Current Week, 1 = Past Weeks History, 2 = All Circles
    var showLeaveConfirmDialog by remember { mutableStateOf(false) }

    val cal = Calendar.getInstance()
    val currentWeekNumber = cal.get(Calendar.WEEK_OF_YEAR)
    val currentYear = cal.get(Calendar.YEAR)

    val userCheckinThisWeek = weeklyCheckins.find { it.isCurrentUser }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("mastermind_circle_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // --- TOP HEADER ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("circle_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isDark) GoldLight else tierTheme.goldDark
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "MASTERMIND CIRCLE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp,
                            color = if (isDark) GoldPrimary else tierTheme.goldDark
                        )
                        Text(
                            text = "Accountability & Alliance",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = if (isDark) TextPrimary else LightTextPrimary
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        color = if (isDark) SurfaceElevated else LightElevated,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onNavigateToLeaderboard()
                            }
                            .testTag("mastermind_leaderboard_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = "Leaderboard",
                                tint = if (isDark) GoldLight else GoldDark,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Leaderboard",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) GoldLight else GoldDark
                            )
                        }
                    }

                    Surface(
                        color = if (isDark) SurfaceElevated else LightElevated,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
                        modifier = Modifier.clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSetShowJoinDialog(true)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.GroupAdd,
                                contentDescription = "Join / Match",
                                tint = if (isDark) GoldLight else tierTheme.goldDark,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Join / Match",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) GoldLight else tierTheme.goldDark
                            )
                        }
                    }
                }
            }
        }

        // --- GROUP STATUS HERO BANNER ---
        if (userGroup != null) {
            item {
                ActiveGroupHeroCard(
                    group = userGroup,
                    membersCount = groupMembers.size,
                    checkedInCount = weeklyCheckins.size,
                    onCopyInviteCode = { code ->
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Mastermind Invite Code", code)
                        clipboard.setPrimaryClip(clip)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    onOpenLeaveDialog = { showLeaveConfirmDialog = true }
                )
            }

            // --- WEEKLY ACCOUNTABILITY CALL TO ACTION CARD ---
            item {
                WeeklyCheckinPromptCard(
                    userCheckin = userCheckinThisWeek,
                    currentWeek = currentWeekNumber,
                    onOpenCheckinDialog = { onSetShowWeeklyCheckinDialog(true) }
                )
            }

            // --- CIRCLE ROSTER (4-6 MEMBERS) ---
            item {
                CircleRosterSection(
                    group = userGroup,
                    members = groupMembers,
                    weeklyCheckins = weeklyCheckins,
                    onInviteMore = { onSetShowJoinDialog(true) }
                )
            }

            // --- TAB SELECTOR (Current Week vs Past Weeks vs Other Circles) ---
            item {
                FeedTabsSelector(
                    selectedTab = selectedFeedTab,
                    onSelectTab = { selectedFeedTab = it }
                )
            }

            // --- FEED CONTENT ---
            when (selectedFeedTab) {
                0 -> {
                    // Current Week Check-ins Feed
                    if (weeklyCheckins.isEmpty()) {
                        item {
                            EmptyCheckinsState(
                                message = "No check-ins submitted for Week $currentWeekNumber yet. Be the first sovereign to seal your commitment!",
                                onInscribe = { onSetShowWeeklyCheckinDialog(true) }
                            )
                        }
                    } else {
                        items(weeklyCheckins, key = { it.id }) { checkin ->
                            CheckinCard(
                                checkin = checkin,
                                onReact = onToggleReaction
                            )
                        }
                    }
                }
                1 -> {
                    // Past Weeks History Feed
                    val pastCheckins = allGroupCheckins.filter { it.weekNumber != currentWeekNumber || it.year != currentYear }
                    if (pastCheckins.isEmpty()) {
                        item {
                            EmptyCheckinsState(
                                message = "No past weeks recorded in this Circle yet. Keep showing up every week to build permanent historical momentum.",
                                onInscribe = { onSetShowWeeklyCheckinDialog(true) }
                            )
                        }
                    } else {
                        items(pastCheckins, key = { it.id }) { checkin ->
                            CheckinCard(
                                checkin = checkin,
                                onReact = onToggleReaction
                            )
                        }
                    }
                }
                2 -> {
                    // Explore / Switch Active Circles
                    item {
                        Text(
                            text = "AVAILABLE SOVEREIGN CIRCLES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = if (isDark) GoldPrimary else tierTheme.goldDark,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    items(allGroups, key = { it.id }) { group ->
                        CircleDiscoveryCard(
                            group = group,
                            isCurrent = group.id == userGroup.id,
                            onJoin = { onJoinByInviteCode(group.inviteCode) }
                        )
                    }
                }
            }
        } else {
            // Unenrolled State
            item {
                UnenrolledHeroCard(
                    onAutoMatch = onAutoMatch,
                    onJoinCode = { onSetShowJoinDialog(true) },
                    onCreateCircle = { onSetShowCreateDialog(true) }
                )
            }

            item {
                Text(
                    text = "DISCOVER ACTIVE CIRCLES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = if (isDark) GoldPrimary else tierTheme.goldDark,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(allGroups, key = { it.id }) { group ->
                CircleDiscoveryCard(
                    group = group,
                    isCurrent = false,
                    onJoin = { onJoinByInviteCode(group.inviteCode) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(36.dp)) }
    }

    // --- MODALS & DIALOGS ---

    // 1. Submit Weekly Check-In Dialog
    if (showWeeklyCheckinDialog) {
        SubmitWeeklyCheckinDialog(
            userCheckin = userCheckinThisWeek,
            weekNumber = currentWeekNumber,
            onDismiss = { onSetShowWeeklyCheckinDialog(false) },
            onSubmit = onSubmitWeeklyCheckin
        )
    }

    // 2. Join Circle / Auto-Match Dialog
    if (showJoinDialog) {
        JoinOrMatchCircleDialog(
            inviteCode = inviteCodeInput,
            onInviteCodeChange = onSetInviteCodeInput,
            onDismiss = { onSetShowJoinDialog(false) },
            onJoinByCode = onJoinByInviteCode,
            onAutoMatch = onAutoMatch,
            onCreateNew = {
                onSetShowJoinDialog(false)
                onSetShowCreateDialog(true)
            }
        )
    }

    // 3. Create Circle Dialog
    if (showCreateDialog) {
        CreateCircleDialog(
            userTier = userProfile?.tierName ?: "Builder",
            onDismiss = { onSetShowCreateDialog(false) },
            onCreate = onCreateCircle
        )
    }

    // 4. Leave Circle Confirmation Dialog
    if (showLeaveConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveConfirmDialog = false },
            title = {
                Text(
                    text = "Leave Mastermind Circle?",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) GoldLight else tierTheme.goldDark
                )
            },
            text = {
                Text(
                    text = "You will forfeit your current group streak and collective standing in '${userGroup?.name}'. You can rejoin anytime or join a new Circle.",
                    color = if (isDark) TextSecondary else LightTextPrimary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLeaveConfirmDialog = false
                        onLeaveCircle()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                ) {
                    Text("Leave Circle", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveConfirmDialog = false }) {
                    Text("Cancel", color = if (isDark) GoldLight else tierTheme.goldDark)
                }
            },
            containerColor = if (isDark) SurfaceElevated else LightElevated
        )
    }
}

// -------------------------------------------------------------
// COMPONENT SECTIONS
// -------------------------------------------------------------

@Composable
private fun ActiveGroupHeroCard(
    group: MastermindGroupEntity,
    membersCount: Int,
    checkedInCount: Int,
    onCopyInviteCode: (String) -> Unit,
    onOpenLeaveDialog: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    var codeCopied by remember { mutableStateOf(false) }

    BrushedCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(GoldDark, GoldLight))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Group,
                                contentDescription = null,
                                tint = RichBlack,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = group.name,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = if (isDark) GoldLight else tierTheme.goldDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "“${group.motto}”",
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = if (isDark) TextSecondary else LightTextPrimary
                    )
                }

                IconButton(
                    onClick = onOpenLeaveDialog,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "Leave Circle",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Invite Code Bar
            Surface(
                color = if (isDark) DarkCharcoal else LightElevated,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) GoldPrimary.copy(alpha = 0.3f) else LightBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "INVITE CODE: ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Text(
                            text = group.inviteCode,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.5.sp,
                            color = if (isDark) GoldLight else tierTheme.goldDark
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isDark) SurfaceElevated else GoldPrimary.copy(alpha = 0.15f))
                            .clickable {
                                onCopyInviteCode(group.inviteCode)
                                codeCopied = true
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (codeCopied) Icons.Filled.Check else Icons.Filled.ContentCopy,
                            contentDescription = "Copy Code",
                            tint = if (isDark) GoldLight else tierTheme.goldDark,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (codeCopied) "COPIED" else "SHARE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) GoldLight else tierTheme.goldDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Key Motivation Mechanics KPIs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MotivationStatCard(
                    icon = Icons.Filled.Whatshot,
                    value = "${group.groupStreakWeeks} Wks",
                    label = "Group Streak",
                    tint = AmberBright,
                    modifier = Modifier.weight(1f)
                )
                MotivationStatCard(
                    icon = Icons.Filled.AutoAwesome,
                    value = "${group.combinedXpThisWeek} XP",
                    label = "Combined XP",
                    tint = if (isDark) GoldLight else tierTheme.goldDark,
                    modifier = Modifier.weight(1f)
                )
                MotivationStatCard(
                    icon = Icons.Filled.Group,
                    value = "$membersCount/${group.maxMembers}",
                    label = "Sovereigns",
                    tint = AmberAccent,
                    modifier = Modifier.weight(1f)
                )
                val checkinRate = if (membersCount > 0) ((checkedInCount.toFloat() / membersCount) * 100).toInt() else 0
                MotivationStatCard(
                    icon = Icons.Filled.MilitaryTech,
                    value = "$checkinRate%",
                    label = "Participation",
                    tint = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MotivationStatCard(
    icon: ImageVector,
    value: String,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    Surface(
        color = if (isDark) DarkCharcoal else LightElevated,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = if (isDark) TextPrimary else LightTextPrimary,
                maxLines = 1
            )
            Text(
                text = label,
                fontSize = 9.sp,
                color = TextMuted,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun WeeklyCheckinPromptCard(
    userCheckin: MastermindCheckinEntity?,
    currentWeek: Int,
    onOpenCheckinDialog: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val isSealed = userCheckin != null

    BrushedCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSealed) SuccessGreen.copy(alpha = 0.2f)
                                else AmberBright.copy(alpha = 0.2f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSealed) Icons.Filled.CheckCircle else Icons.Filled.Psychology,
                            contentDescription = null,
                            tint = if (isSealed) SuccessGreen else AmberBright,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WEEK $currentWeek ACCOUNTABILITY RITUAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = if (isDark) GoldPrimary else tierTheme.goldDark
                    )
                }

                Surface(
                    color = if (isSealed) SuccessGreen.copy(alpha = 0.15f) else AmberAccent.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSealed) SuccessGreen.copy(alpha = 0.5f) else AmberAccent.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = if (isSealed) "SEALED (+75 XP)" else "PENDING",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSealed) SuccessGreen else AmberBright,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isSealed && userCheckin != null) {
                Text(
                    text = "Goal: “${userCheckin.goalTitle}”",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) TextPrimary else LightTextPrimary
                )
                if (userCheckin.note.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Reflection: ${userCheckin.note}",
                        fontSize = 12.sp,
                        color = if (isDark) TextSecondary else LightTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onOpenCheckinDialog,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Week $currentWeek Check-in", fontSize = 12.sp, color = if (isDark) GoldLight else tierTheme.goldDark)
                }
            } else {
                Text(
                    text = "Declare your chief goal for this week and record whether you achieved your targets. Your Circle relies on your sovereign discipline.",
                    fontSize = 12.sp,
                    color = if (isDark) TextSecondary else LightTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                MastermindGoldButton(
                    text = "Inscribe Weekly Check-In (+75 XP)",
                    icon = Icons.Filled.CheckCircle,
                    onClick = onOpenCheckinDialog,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CircleRosterSection(
    group: MastermindGroupEntity,
    members: List<MastermindMemberEntity>,
    weeklyCheckins: List<MastermindCheckinEntity>,
    onInviteMore: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val checkinMap = weeklyCheckins.associateBy { it.memberId }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CIRCLE COHORT (${members.size}/${group.maxMembers})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = if (isDark) GoldPrimary else tierTheme.goldDark
            )

            if (members.size < group.maxMembers) {
                Text(
                    text = "+ Add Peer",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) AmberAccent else tierTheme.goldDark,
                    modifier = Modifier.clickable { onInviteMore() }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            members.forEach { member ->
                val hasCheckedIn = checkinMap.containsKey(member.id) || (member.isCurrentUser && weeklyCheckins.any { it.isCurrentUser })
                MemberAvatarBadge(member = member, hasCheckedIn = hasCheckedIn)
            }
        }
    }
}

@Composable
private fun MemberAvatarBadge(
    member: MastermindMemberEntity,
    hasCheckedIn: Boolean
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val avatarBgColor = try {
        Color(android.graphics.Color.parseColor(member.avatarColorHex))
    } catch (e: Exception) {
        GoldPrimary
    }

    Surface(
        color = if (isDark) SurfaceElevated else LightElevated,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (member.isCurrentUser) (if (isDark) GoldLight else tierTheme.goldDark) else (if (isDark) DarkBorder else LightBorder)
        ),
        modifier = Modifier.width(108.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(avatarBgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.avatarInitial,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = RichBlack
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = member.displayName,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) TextPrimary else LightTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = member.tierTitle,
                fontSize = 9.sp,
                color = if (isDark) AmberAccent else tierTheme.goldDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                color = if (hasCheckedIn) SuccessGreen.copy(alpha = 0.15f) else TextMuted.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = if (hasCheckedIn) "CHECKED IN" else "PENDING",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (hasCheckedIn) SuccessGreen else TextMuted,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun FeedTabsSelector(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val tabs = listOf("This Week", "History", "All Circles")

    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.Transparent,
        contentColor = if (isDark) GoldLight else tierTheme.goldDark,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                color = if (isDark) GoldLight else tierTheme.goldDark
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onSelectTab(index) },
                text = {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == index) (if (isDark) GoldLight else tierTheme.goldDark) else TextMuted
                    )
                }
            )
        }
    }
}

@Composable
private fun CheckinCard(
    checkin: MastermindCheckinEntity,
    onReact: (checkinId: String, reactionType: String) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val haptic = LocalHapticFeedback.current

    val avatarColor = try {
        Color(android.graphics.Color.parseColor(checkin.memberAvatarColorHex))
    } catch (e: Exception) {
        GoldPrimary
    }

    val (statusLabel, statusColor) = when (checkin.status.uppercase(Locale.US)) {
        "YES" -> "TARGET HIT ⚡" to SuccessGreen
        "PARTIAL" -> "PARTIAL PROGRESS ⏳" to AmberBright
        else -> "OFF TRACK ⚠️" to Color(0xFFEF5350)
    }

    val dateFormatted = try {
        val sdf = SimpleDateFormat("EEE, MMM d", Locale.US)
        sdf.format(Date(checkin.timestamp))
    } catch (e: Exception) {
        "Week ${checkin.weekNumber}"
    }

    BrushedCard {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = checkin.memberAvatarInitial,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = RichBlack
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = checkin.memberDisplayName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) TextPrimary else LightTextPrimary
                            )
                            if (checkin.isCurrentUser) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldPrimary.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("YOU", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                                }
                            }
                        }
                        Text(
                            text = "${checkin.memberTier} • $dateFormatted",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }

                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = checkin.goalTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                color = if (isDark) TextPrimary else LightTextPrimary
            )

            if (checkin.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = checkin.note,
                    fontSize = 12.sp,
                    color = if (isDark) TextSecondary else LightTextPrimary,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = if (isDark) DarkBorder else LightBorder, thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Lightweight Reactions Bar (Fire 🔥, Clap 👏, Diamond 💎)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReactionChip(
                    emoji = "🔥",
                    count = checkin.fireCount,
                    isSelected = checkin.userReactedFire,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onReact(checkin.id, "fire")
                    }
                )
                ReactionChip(
                    emoji = "👏",
                    count = checkin.clapCount,
                    isSelected = checkin.userReactedClap,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onReact(checkin.id, "clap")
                    }
                )
                ReactionChip(
                    emoji = "💎",
                    count = checkin.diamondCount,
                    isSelected = checkin.userReactedDiamond,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onReact(checkin.id, "diamond")
                    }
                )
            }
        }
    }
}

@Composable
private fun ReactionChip(
    emoji: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    Surface(
        color = if (isSelected) (if (isDark) GoldPrimary.copy(alpha = 0.25f) else GoldLight.copy(alpha = 0.35f))
        else (if (isDark) DarkCharcoal else LightElevated),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) (if (isDark) GoldLight else tierTheme.goldDark)
            else (if (isDark) DarkBorder else LightBorder)
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 12.sp)
            if (count > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$count",
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) (if (isDark) GoldLight else tierTheme.goldDark) else TextMuted
                )
            }
        }
    }
}

@Composable
private fun CircleDiscoveryCard(
    group: MastermindGroupEntity,
    isCurrent: Boolean,
    onJoin: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    BrushedCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = group.name,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isDark) GoldLight else tierTheme.goldDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    TierBadgeChip(tier = group.targetTier)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "“${group.motto}”",
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = if (isDark) TextSecondary else LightTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "🔥 ${group.groupStreakWeeks} Wks Streak",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmberBright
                    )
                    Text(
                        text = "⚡ ${group.combinedXpThisWeek} XP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) GoldLight else tierTheme.goldDark
                    )
                }
            }

            if (isCurrent) {
                Surface(
                    color = SuccessGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "ACTIVE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            } else {
                Button(
                    onClick = onJoin,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) SurfaceElevated else LightElevated,
                        contentColor = if (isDark) GoldLight else tierTheme.goldDark
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) GoldPrimary.copy(alpha = 0.4f) else LightBorder)
                ) {
                    Text("Join Circle", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun UnenrolledHeroCard(
    onAutoMatch: () -> Unit,
    onJoinCode: () -> Unit,
    onCreateCircle: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    BrushedCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(GoldDark, GoldLight))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Group,
                    contentDescription = null,
                    tint = RichBlack,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The Mastermind Principle",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (isDark) GoldLight else tierTheme.goldDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "“No two minds ever come together without thereby creating a third, invisible, intangible force which may be likened to a third mind.” — Napoleon Hill",
                fontSize = 12.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = if (isDark) TextSecondary else LightTextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            MastermindGoldButton(
                text = "Auto-Match My Level & Tier",
                icon = Icons.Filled.AutoAwesome,
                onClick = onAutoMatch,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onJoinCode,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Enter Invite Code", fontSize = 11.sp, color = if (isDark) GoldLight else tierTheme.goldDark)
                }
                OutlinedButton(
                    onClick = onCreateCircle,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Found New Circle", fontSize = 11.sp, color = if (isDark) GoldLight else tierTheme.goldDark)
                }
            }
        }
    }
}

@Composable
private fun EmptyCheckinsState(
    message: String,
    onInscribe: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    BrushedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 12.sp,
                color = if (isDark) TextSecondary else LightTextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onInscribe) {
                Text("Inscribe Check-In (+75 XP)", fontSize = 11.sp, color = if (isDark) GoldLight else tierTheme.goldDark)
            }
        }
    }
}

// -------------------------------------------------------------
// DIALOGS
// -------------------------------------------------------------

@Composable
private fun SubmitWeeklyCheckinDialog(
    userCheckin: MastermindCheckinEntity?,
    weekNumber: Int,
    onDismiss: () -> Unit,
    onSubmit: (goal: String, status: String, note: String) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    var goalText by remember { mutableStateOf(userCheckin?.goalTitle ?: "") }
    var selectedStatus by remember { mutableStateOf(userCheckin?.status ?: "YES") }
    var noteText by remember { mutableStateOf(userCheckin?.note ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "WEEK $weekNumber ACCOUNTABILITY",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = if (isDark) GoldPrimary else tierTheme.goldDark
                )
                Text(
                    text = "Inscribe Weekly Execution",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isDark) GoldLight else tierTheme.goldDark
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = goalText,
                    onValueChange = { goalText = it },
                    label = { Text("Chief Goal For The Week") },
                    placeholder = { Text("e.g. Inscribe 5 proposals, 7-day affirmations") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDark) GoldLight else tierTheme.goldDark,
                        unfocusedBorderColor = if (isDark) DarkBorder else LightBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Did you achieve your targets?",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) TextSecondary else LightTextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    StatusSelectorChip(
                        label = "YES (100%)",
                        status = "YES",
                        isSelected = selectedStatus == "YES",
                        activeColor = SuccessGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedStatus = "YES" }
                    )
                    StatusSelectorChip(
                        label = "PARTIAL",
                        status = "PARTIAL",
                        isSelected = selectedStatus == "PARTIAL",
                        activeColor = AmberBright,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedStatus = "PARTIAL" }
                    )
                    StatusSelectorChip(
                        label = "NO",
                        status = "NO",
                        isSelected = selectedStatus == "NO",
                        activeColor = Color(0xFFEF5350),
                        modifier = Modifier.weight(1f),
                        onClick = { selectedStatus = "NO" }
                    )
                }

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Weekly Reflection / Notes") },
                    placeholder = { Text("What breakthroughs or lessons did you unlock?") },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDark) GoldLight else tierTheme.goldDark,
                        unfocusedBorderColor = if (isDark) DarkBorder else LightBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (goalText.isNotBlank()) {
                        onSubmit(goalText, selectedStatus, noteText)
                    }
                },
                enabled = goalText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                    contentColor = RichBlack
                )
            ) {
                Text("Seal Check-in (+75 XP)", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = if (isDark) SurfaceElevated else LightElevated
    )
}

@Composable
private fun StatusSelectorChip(
    label: String,
    status: String,
    isSelected: Boolean,
    activeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    Surface(
        color = if (isSelected) activeColor.copy(alpha = 0.2f) else (if (isDark) DarkCharcoal else LightElevated),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) activeColor else (if (isDark) DarkBorder else LightBorder)
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) activeColor else TextMuted
            )
        }
    }
}

@Composable
private fun JoinOrMatchCircleDialog(
    inviteCode: String,
    onInviteCodeChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onJoinByCode: (String) -> Unit,
    onAutoMatch: () -> Unit,
    onCreateNew: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Join a Mastermind Circle",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = if (isDark) GoldLight else tierTheme.goldDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Enter a 4-letter invite code from a fellow sovereign or auto-match with peers at your current tier.",
                    fontSize = 12.sp,
                    color = if (isDark) TextSecondary else LightTextPrimary
                )

                OutlinedTextField(
                    value = inviteCode,
                    onValueChange = { onInviteCodeChange(it.uppercase(Locale.US)) },
                    label = { Text("Invite Code (e.g. ARCH-9104)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDark) GoldLight else tierTheme.goldDark,
                        unfocusedBorderColor = if (isDark) DarkBorder else LightBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { onJoinByCode(inviteCode) },
                    enabled = inviteCode.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                        contentColor = RichBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Join Via Code (+50 XP)", fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(color = if (isDark) DarkBorder else LightBorder)

                OutlinedButton(
                    onClick = onAutoMatch,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Auto-Match By Tier (+50 XP)", color = if (isDark) GoldLight else tierTheme.goldDark)
                }

                TextButton(
                    onClick = onCreateNew,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Found A New Mastermind Circle", color = AmberAccent)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = TextMuted)
            }
        },
        containerColor = if (isDark) SurfaceElevated else LightElevated
    )
}

@Composable
private fun CreateCircleDialog(
    userTier: String,
    onDismiss: () -> Unit,
    onCreate: (name: String, motto: String, tier: String) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    var name by remember { mutableStateOf("") }
    var motto by remember { mutableStateOf("") }
    var selectedTier by remember { mutableStateOf(userTier) }

    val tierOptions = listOf("Novice", "Builder", "Architect", "Sovereign", "Legacy")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Found Mastermind Circle",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = if (isDark) GoldLight else tierTheme.goldDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Circle Name") },
                    placeholder = { Text("e.g. The Sovereign Architects") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDark) GoldLight else tierTheme.goldDark,
                        unfocusedBorderColor = if (isDark) DarkBorder else LightBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = motto,
                    onValueChange = { motto = it },
                    label = { Text("Motto / Guiding Creed") },
                    placeholder = { Text("e.g. Definite Purpose & Sovereign Action") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDark) GoldLight else tierTheme.goldDark,
                        unfocusedBorderColor = if (isDark) DarkBorder else LightBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Target Cohort Tier",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) TextSecondary else LightTextPrimary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tierOptions.forEach { tier ->
                        val isSelected = selectedTier == tier
                        Surface(
                            color = if (isSelected) (if (isDark) GoldPrimary.copy(alpha = 0.25f) else GoldLight.copy(alpha = 0.35f))
                            else (if (isDark) DarkCharcoal else LightElevated),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) (if (isDark) GoldLight else tierTheme.goldDark)
                                else (if (isDark) DarkBorder else LightBorder)
                            ),
                            modifier = Modifier.clickable { selectedTier = tier }
                        ) {
                            Text(
                                text = tier,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) (if (isDark) GoldLight else tierTheme.goldDark) else TextMuted,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onCreate(name, motto, selectedTier)
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                    contentColor = RichBlack
                )
            ) {
                Text("Found Circle (+100 XP)", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = if (isDark) SurfaceElevated else LightElevated
    )
}

@Composable
fun MastermindGoldButton(
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val tierTheme = LocalTierGoldTheme.current
    val isDark = LocalIsDarkTheme.current
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDark) tierTheme.goldPrimary else tierTheme.goldDark,
            contentColor = if (isDark) RichBlack else Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(46.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
