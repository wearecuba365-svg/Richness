package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BadgeEntity
import com.example.data.model.ModuleEntity
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.SectionAchievementInfo
import com.example.data.model.UserProfileEntity
import com.example.data.model.WealthGoalEntity
import com.example.data.model.getSectionForBadgeId
import com.example.ui.components.BrushedCard
import com.example.ui.components.LocalWindowSizeInfo
import com.example.ui.components.ThemeSelectorCard
import com.example.ui.components.TierBadgeChip
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TierArchitect
import com.example.ui.theme.TierBuilder
import com.example.ui.theme.TierLegacy
import com.example.ui.theme.TierNovice
import com.example.ui.theme.TierSovereign
import com.example.ui.theme.getTierGoldIntensity
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.data.remote.firebase.AuthUserState
import com.example.data.remote.firebase.CloudSyncState
import com.example.data.remote.firebase.CloudSyncStatus
import com.example.ui.components.EditAgeDialog

@Composable
fun ProfileBadgesScreen(
    userProfile: UserProfileEntity?,
    badges: List<BadgeEntity>,
    modules: List<ModuleEntity>,
    notebookEntries: List<NotebookEntryEntity>,
    authUserState: AuthUserState = AuthUserState(),
    cloudSyncState: CloudSyncState = CloudSyncState(),
    themeMode: AppThemeMode = AppThemeMode.DARK,
    isFloatingMoneyBubblesEnabled: Boolean = true,
    onSignInGoogle: () -> Unit = {},
    onSignOut: () -> Unit = {},
    onSyncCloud: () -> Unit = {},
    onSetThemeMode: (AppThemeMode) -> Unit = {},
    onToggleFloatingMoneyBubbles: () -> Unit = {},
    onUpdateBirthDate: (Int, Int, Int) -> Unit = { _, _, _ -> },
    onNavigateToAssessment: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onOpenPaywall: () -> Unit,
    onNavigateToNotebookExport: () -> Unit = {},
    onOpenSectionAchievement: (SectionAchievementInfo) -> Unit = {},
    onEditChiefAim: () -> Unit = {},
    wealthGoal: WealthGoalEntity? = null,
    onEditWealthGoal: () -> Unit = {},
    onNavigateToWealthGoalTracker: () -> Unit = {},
    onNavigateToMoneyBlueprint: () -> Unit = {},
    onToggleLeaderboardOptIn: (Boolean) -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {}
) {
    var showEditAgeDialog by remember { mutableStateOf(false) }
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val windowInfo = LocalWindowSizeInfo.current
    val completedCount = modules.count { it.isCompleted }

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    if (windowInfo.isTabletOrFoldable) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("profile_badges_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left Column: Identity, Auth, Theme, Stats, Actions
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ThemeSelectorCard(
                            currentThemeMode = themeMode,
                            onThemeChange = onSetThemeMode,
                            isFloatingMoneyBubblesEnabled = isFloatingMoneyBubblesEnabled,
                            onToggleFloatingMoneyBubbles = onToggleFloatingMoneyBubbles
                        )

                        LeaderboardPrivacySettingsCard(
                            isOptedIn = userProfile?.isLeaderboardOptedIn ?: true,
                            onToggleOptIn = onToggleLeaderboardOptIn,
                            onNavigateToLeaderboard = onNavigateToLeaderboard
                        )

                        CloudSyncProfileCard(
                            authUserState = authUserState,
                            cloudSyncState = cloudSyncState,
                            onSignInGoogle = onSignInGoogle,
                            onSignOut = onSignOut,
                            onSyncCloud = onSyncCloud
                        )

                        UserIdentityHeaderCard(
                            userProfile = userProfile,
                            onOpenPaywall = onOpenPaywall
                        )

                        LifetimeTelemetryCard(
                            completedCount = completedCount,
                            userProfile = userProfile,
                            notebookEntries = notebookEntries
                        )

                        DefiniteChiefAimProfileCard(
                            userProfile = userProfile,
                            onEditChiefAim = onEditChiefAim
                        )

                        WealthGoalProfileCard(
                            wealthGoal = wealthGoal,
                            onEditWealthGoal = onEditWealthGoal,
                            onNavigateToWealthGoalTracker = onNavigateToWealthGoalTracker
                        )

                        ProfileActionsSection(
                            onNavigateToNotebookExport = onNavigateToNotebookExport,
                            onNavigateToAssessment = onNavigateToAssessment,
                            onNavigateToMoneyBlueprint = onNavigateToMoneyBlueprint,
                            onNavigateToAdmin = onNavigateToAdmin,
                            onEditTimeline = { showEditAgeDialog = true },
                            onNavigateToWealthTracker = onNavigateToWealthGoalTracker
                        )
                    }

                    // Right Column: 5 Tiers Roadmap & Badges Collection
                    Column(
                        modifier = Modifier.weight(1.05f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        TiersRoadmapCard(userProfile = userProfile)

                        BadgesCollectionCard(
                            badges = badges,
                            onOpenSectionAchievement = onOpenSectionAchievement
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("profile_badges_screen"),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            item {
                ThemeSelectorCard(
                    currentThemeMode = themeMode,
                    onThemeChange = onSetThemeMode,
                    isFloatingMoneyBubblesEnabled = isFloatingMoneyBubblesEnabled,
                    onToggleFloatingMoneyBubbles = onToggleFloatingMoneyBubbles
                )
            }

            item {
                LeaderboardPrivacySettingsCard(
                    isOptedIn = userProfile?.isLeaderboardOptedIn ?: true,
                    onToggleOptIn = onToggleLeaderboardOptIn,
                    onNavigateToLeaderboard = onNavigateToLeaderboard
                )
            }

            item {
                CloudSyncProfileCard(
                    authUserState = authUserState,
                    cloudSyncState = cloudSyncState,
                    onSignInGoogle = onSignInGoogle,
                    onSignOut = onSignOut,
                    onSyncCloud = onSyncCloud
                )
            }

            item {
                UserIdentityHeaderCard(
                    userProfile = userProfile,
                    onOpenPaywall = onOpenPaywall
                )
            }

            item {
                LifetimeTelemetryCard(
                    completedCount = completedCount,
                    userProfile = userProfile,
                    notebookEntries = notebookEntries
                )
            }

            item {
                DefiniteChiefAimProfileCard(
                    userProfile = userProfile,
                    onEditChiefAim = onEditChiefAim
                )
            }

            item {
                WealthGoalProfileCard(
                    wealthGoal = wealthGoal,
                    onEditWealthGoal = onEditWealthGoal,
                    onNavigateToWealthGoalTracker = onNavigateToWealthGoalTracker
                )
            }

            item {
                TiersRoadmapCard(userProfile = userProfile)
            }

            item {
                BadgesCollectionCard(
                    badges = badges,
                    onOpenSectionAchievement = onOpenSectionAchievement
                )
            }

            item {
                ProfileActionsSection(
                    onNavigateToNotebookExport = onNavigateToNotebookExport,
                    onNavigateToAssessment = onNavigateToAssessment,
                    onNavigateToMoneyBlueprint = onNavigateToMoneyBlueprint,
                    onNavigateToAdmin = onNavigateToAdmin,
                    onEditTimeline = { showEditAgeDialog = true },
                    onNavigateToWealthTracker = onNavigateToWealthGoalTracker
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }

    if (showEditAgeDialog) {
        EditAgeDialog(
            currentBirthYear = userProfile?.birthYear ?: 1996,
            currentBirthMonth = userProfile?.birthMonth ?: 1,
            currentBirthDay = userProfile?.birthDay ?: 1,
            onDismiss = { showEditAgeDialog = false },
            onSave = { y, m, d ->
                onUpdateBirthDate(y, m, d)
                showEditAgeDialog = false
            }
        )
    }
}

@Composable
private fun CloudSyncProfileCard(
    authUserState: AuthUserState,
    cloudSyncState: CloudSyncState,
    onSignInGoogle: () -> Unit,
    onSignOut: () -> Unit,
    onSyncCloud: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    BrushedCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (authUserState.isAuthenticated) (if (isDark) GoldDark else tierTheme.goldPrimary) else surfaceColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (authUserState.isAuthenticated) Icons.Filled.CloudDone else Icons.Filled.CloudQueue,
                        contentDescription = null,
                        tint = if (authUserState.isAuthenticated) (if (isDark) GoldLight else Color.White) else textMutedColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (authUserState.isAuthenticated) "Google Cloud Synced" else "Local Member Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (authUserState.isAuthenticated) goldAccent else textColor
                    )
                    Text(
                        text = if (authUserState.isAuthenticated) {
                            authUserState.email ?: "Firebase Member Account"
                        } else {
                            "Sign in with Google to backup progress & sync across devices"
                        },
                        fontSize = 11.sp,
                        color = textMutedColor
                    )
                    if (cloudSyncState.status == CloudSyncStatus.SYNCING) {
                        Text(
                            text = "Syncing with Firestore...",
                            fontSize = 10.sp,
                            color = AmberAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (authUserState.isAuthenticated) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onSyncCloud,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) GoldPrimary else tierTheme.goldDark),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("sync_cloud_button")
                    ) {
                        Icon(Icons.Filled.Sync, contentDescription = "Sync", modifier = Modifier.size(14.dp))
                    }

                    OutlinedButton(
                        onClick = onSignOut,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = textMutedColor),
                        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("sign_out_button")
                    ) {
                        Icon(Icons.Filled.Logout, contentDescription = "Sign Out", modifier = Modifier.size(14.dp))
                    }
                }
            } else {
                Button(
                    onClick = onSignInGoogle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                        contentColor = if (isDark) RichBlack else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("google_sign_in_button")
                ) {
                    Icon(Icons.Filled.Login, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Connect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun UserIdentityHeaderCard(
    userProfile: UserProfileEntity?,
    onOpenPaywall: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark

    BrushedCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(if (isDark) GoldDark else tierTheme.goldPrimary.copy(alpha = 0.2f))
                    .border(2.dp, goldAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Diamond,
                    contentDescription = "Avatar",
                    tint = goldAccent,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = userProfile?.name ?: "Sovereign Member",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = textColor
                    )
                    TierBadgeChip(tier = userProfile?.tierName ?: "Novice")
                }

                Text(
                    text = userProfile?.email ?: "member@riches.club",
                    fontSize = 11.sp,
                    color = textMutedColor
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "${userProfile?.xpTotal ?: 0} Total XP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent
                    )
                    Text(
                        text = "•",
                        color = textMutedColor
                    )
                    Text(
                        text = "Score ${userProfile?.mindsetScore ?: 50}/100",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) AmberAccent else tierTheme.goldDark
                    )
                }
            }
        }

        if (userProfile?.isPaidUnlocked != true) {
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onOpenPaywall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) AmberAccent else tierTheme.goldPrimary,
                    contentColor = if (isDark) RichBlack else Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Filled.WorkspacePremium, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "UPGRADE TO LIFETIME SOVEREIGN TIER ($97)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LifetimeTelemetryCard(
    completedCount: Int,
    userProfile: UserProfileEntity?,
    notebookEntries: List<NotebookEntryEntity>
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    BrushedCard {
        Text(
            text = "LIFETIME SOVEREIGN TELEMETRY",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = goldAccent,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(text = "$completedCount", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = goldAccent)
                Text(text = "Vaults Conquered", fontSize = 10.sp, color = textMutedColor, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(text = "${userProfile?.bestStreak ?: 1}", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (isDark) AmberBright else tierTheme.goldDark)
                Text(text = "Best Streak", fontSize = 10.sp, color = textMutedColor, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(text = "${notebookEntries.size}", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = goldAccent)
                Text(text = "Reflections", fontSize = 10.sp, color = textMutedColor, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
            val fearRefCount = notebookEntries.count { it.entryType == NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME }
            if (fearRefCount > 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "$fearRefCount", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (isDark) AmberBright else GoldDark)
                    Text(text = "Fears Converted", fontSize = 10.sp, color = textMutedColor, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
            val decisionsCount = notebookEntries.count { it.entryType == NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG }
            if (decisionsCount > 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "$decisionsCount", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = goldAccent)
                    Text(text = "Decisions Logged", fontSize = 10.sp, color = textMutedColor, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
            val comebacksCount = (userProfile?.comebacksCount ?: 0).coerceAtLeast(notebookEntries.count { it.entryType == NotebookEntryEntity.ENTRY_TYPE_COMEBACK })
            if (comebacksCount > 0) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(text = "$comebacksCount", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if (isDark) AmberBright else GoldDark)
                    Text(text = "Comebacks", fontSize = 10.sp, color = textMutedColor, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        }

        // TODO: Weekly Review / Fear Reframe Aggregation view (Scheduled for a future version)

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
            color = surfaceColor,
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Local Room DB Cached",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
                Text(
                    text = "100% Offline Access Active",
                    fontSize = 9.sp,
                    color = goldAccent
                )
            }
        }
    }
}

@Composable
private fun TiersRoadmapCard(userProfile: UserProfileEntity?) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    BrushedCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "5 SOVEREIGN TIERS & GOLD LUSTER",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = goldAccent,
                letterSpacing = 1.sp
            )
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = if (isDark) AmberBright else tierTheme.goldDark,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = "As you gain XP, the entire UI's metallic gold luster & embers dynamically intensify.",
            fontSize = 11.sp,
            color = textSecColor
        )

        Spacer(modifier = Modifier.height(14.dp))

        val tierRoadmap = listOf(
            TierItem("Novice", "0 XP", "25% Gold Luster • Subtle Amber Embers • Slate Border", TierNovice),
            TierItem("Builder", "500 XP", "45% Gold Luster • Dynamic Progress Arc • Bronze Border", TierBuilder),
            TierItem("Architect", "1,500 XP", "65% Gold Luster • Warm Gold Crest • Amethyst Halo", TierArchitect),
            TierItem("Sovereign", "3,500 XP", "85% Gold Luster • Radiant Header Aura • Imperial Halo", TierSovereign),
            TierItem("Legacy", "7,000+ XP", "100% Gold Luster • Solar Amber Blaze • Transmuted Apex Crest", TierLegacy)
        )

        tierRoadmap.forEach { item ->
            val isCurrent = userProfile?.tierName?.equals(item.name, ignoreCase = true) == true
            val tierItemTheme = getTierGoldIntensity(item.name)

            Surface(
                color = if (isCurrent) tierItemTheme.goldPrimary.copy(alpha = if (isDark) 0.18f else 0.12f) else surfaceColor,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isCurrent) tierItemTheme.badgeBorderWidth else 1.dp,
                    color = if (isCurrent) (if (isDark) tierItemTheme.goldLight else tierItemTheme.goldDark) else cardBorderColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isDark) tierItemTheme.goldDark else tierItemTheme.goldPrimary.copy(alpha = 0.25f))
                            .border(
                                width = if (isCurrent) tierItemTheme.crestBorderWidth else 1.dp,
                                color = if (isCurrent) (if (isDark) tierItemTheme.goldLight else tierItemTheme.goldDark) else item.color.copy(alpha = 0.5f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Diamond,
                            contentDescription = null,
                            tint = if (isCurrent) (if (isDark) tierItemTheme.goldLight else tierItemTheme.goldDark) else item.color,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${item.name} Tier",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isCurrent) (if (isDark) tierItemTheme.goldLight else tierItemTheme.goldDark) else textColor
                                )
                                if (isCurrent) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = tierTheme.goldPrimary.copy(alpha = if (isDark) 0.25f else 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "ACTIVE",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = goldAccent,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = item.xpReq,
                                fontSize = 10.sp,
                                color = if (isCurrent) (if (isDark) AmberAccent else tierTheme.goldDark) else textMutedColor,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        Text(
                            text = item.flair,
                            fontSize = 10.sp,
                            color = if (isCurrent) textSecColor else textMutedColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgesCollectionCard(
    badges: List<BadgeEntity>,
    onOpenSectionAchievement: (SectionAchievementInfo) -> Unit = {}
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    BrushedCard {
        Text(
            text = "SOVEREIGN BADGES VAULT",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = goldAccent,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        badges.forEach { badge ->
            val sectionInfo = getSectionForBadgeId(badge.id)
            val isClickable = sectionInfo != null

            Surface(
                color = surfaceColor,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (badge.isUnlocked) (if (isDark) GoldPrimary.copy(alpha = 0.5f) else tierTheme.goldDark.copy(alpha = 0.4f)) else cardBorderColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .then(
                        if (isClickable) {
                            Modifier.clickable {
                                onOpenSectionAchievement(sectionInfo!!)
                            }
                        } else Modifier
                    )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    if (badge.isUnlocked) (if (isDark) GoldDark.copy(alpha = 0.4f) else tierTheme.goldPrimary.copy(alpha = 0.2f)) else surfaceColor
                                )
                                .border(
                                    1.dp,
                                    if (badge.isUnlocked) goldAccent else textMutedColor.copy(alpha = 0.4f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (badge.isUnlocked) Icons.Filled.WorkspacePremium else Icons.Filled.Lock,
                                contentDescription = badge.title,
                                tint = if (badge.isUnlocked) goldAccent else textMutedColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = badge.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (badge.isUnlocked) textColor else textMutedColor
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = if (badge.isUnlocked) goldAccent.copy(alpha = 0.15f) else cardBorderColor.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "+${badge.xpReward} XP",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (badge.isUnlocked) goldAccent else textMutedColor,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (badge.isUnlocked) "UNLOCKED" else "${badge.tierRequired} Tier",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (badge.isUnlocked) goldAccent else textMutedColor
                                    )
                                }
                            }
                            Text(
                                text = badge.description,
                                fontSize = 11.sp,
                                color = textSecColor,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    if (!badge.isUnlocked && badge.maxProgress > 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Progress: ${badge.progress} / ${badge.maxProgress}",
                                fontSize = 9.sp,
                                color = textMutedColor
                            )
                            val pct = ((badge.progress.toFloat() / badge.maxProgress.toFloat()) * 100).toInt()
                            Text(
                                text = "$pct%",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        LinearProgressIndicator(
                            progress = { (badge.progress.toFloat() / badge.maxProgress.toFloat()).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = goldAccent,
                            trackColor = cardBorderColor.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileActionsSection(
    onNavigateToNotebookExport: () -> Unit,
    onNavigateToAssessment: () -> Unit,
    onNavigateToMoneyBlueprint: () -> Unit = {},
    onNavigateToAdmin: () -> Unit,
    onEditTimeline: () -> Unit = {},
    onNavigateToWealthTracker: () -> Unit = {}
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedButton(
            onClick = onNavigateToWealthTracker,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) GoldPrimary else tierTheme.goldDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("profile_open_wealth_tracker_button")
        ) {
            Icon(imageVector = Icons.Filled.AccountBalance, contentDescription = null, modifier = Modifier.size(16.dp), tint = goldAccent)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "OPEN WEALTH GOAL ACCUMULATION TRACKER", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
        OutlinedButton(
            onClick = onNavigateToMoneyBlueprint,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) GoldPrimary else tierTheme.goldDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("profile_open_money_blueprint_button")
        ) {
            Icon(imageVector = Icons.Filled.Psychology, contentDescription = null, modifier = Modifier.size(16.dp), tint = goldAccent)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "MONEY BLUEPRINT LIMITING BELIEFS QUIZ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
        OutlinedButton(
            onClick = onEditTimeline,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) AmberAccent.copy(alpha = 0.6f) else tierTheme.goldDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Filled.HourglassTop, contentDescription = null, modifier = Modifier.size(16.dp), tint = if (isDark) AmberAccent else tierTheme.goldDark)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "SYNCHRONIZE LIFE TIMELINE / AGE (4,680 WEEKS)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        OutlinedButton(
            onClick = onNavigateToNotebookExport,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) GoldPrimary else tierTheme.goldDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Filled.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "EXPORT SOVEREIGN NOTEBOOK (PDF)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        OutlinedButton(
            onClick = onNavigateToAssessment,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = textSecColor),
            border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Filled.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "RE-CALIBRATE MINDSET ASSESSMENT", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        OutlinedButton(
            onClick = onNavigateToAdmin,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = textSecColor),
            border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Filled.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "OPEN ADMIN / ARCHITECT CONSOLE", fontSize = 11.sp)
        }
    }
}

@Composable
private fun DefiniteChiefAimProfileCard(
    userProfile: UserProfileEntity?,
    onEditChiefAim: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textPrimaryColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark

    val aim = userProfile?.definiteChiefAim?.trim().orEmpty()
    val hasAim = aim.isNotBlank()
    val streak = userProfile?.affirmationStreak ?: 0
    val bestStreak = userProfile?.bestAffirmationStreak ?: 0

    BrushedCard(
        modifier = Modifier.fillMaxWidth().testTag("profile_chief_aim_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isDark) GoldDark.copy(alpha = 0.5f) else tierTheme.goldDark.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Diamond,
                            contentDescription = null,
                            tint = goldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "DEFINITE CHIEF AIM",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Transmutation Goal & Daily Ritual",
                            fontSize = 10.sp,
                            color = textSecColor
                        )
                    }
                }

                Button(
                    onClick = onEditChiefAim,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) GoldDark else tierTheme.goldDark,
                        contentColor = if (isDark) GoldLight else Color.White
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("edit_chief_aim_profile_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (hasAim) "Edit Aim" else "Define Aim",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (hasAim) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) DarkCharcoal.copy(alpha = 0.7f) else LightElevated)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Filled.FormatQuote,
                                contentDescription = null,
                                tint = goldAccent,
                                modifier = Modifier.size(16.dp).padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = aim,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                fontFamily = FontFamily.Serif,
                                color = textPrimaryColor
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No Definite Chief Aim inscribed yet. Establish your clear statement of purpose to anchor your daily morning affirmation ritual.",
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = textSecColor,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocalFireDepartment,
                        contentDescription = null,
                        tint = if (streak > 0) AmberBright else textSecColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Current Ritual Streak: $streak days",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (streak > 0) goldAccent else textSecColor
                    )
                }

                Text(
                    text = "Best: $bestStreak days",
                    fontSize = 10.sp,
                    color = textSecColor
                )
            }
        }
    }
}

@Composable
private fun WealthGoalProfileCard(
    wealthGoal: WealthGoalEntity?,
    onEditWealthGoal: () -> Unit,
    onNavigateToWealthGoalTracker: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textPrimaryColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    val goal = wealthGoal ?: WealthGoalEntity()
    val currency = goal.currencySymbol
    val target = goal.targetAmount.coerceAtLeast(1.0)
    val current = goal.currentAmount
    val progress = (current / target).toFloat().coerceIn(0f, 1f)
    val pct = (progress * 100).toInt()

    val now = System.currentTimeMillis()
    val daysLeft = ((goal.targetDateEpochMillis - now) / (24 * 60 * 60 * 1000L)).coerceAtLeast(0)

    BrushedCard(
        modifier = Modifier.fillMaxWidth().testTag("profile_wealth_goal_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isDark) GoldDark.copy(alpha = 0.5f) else tierTheme.goldDark.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountBalance,
                            contentDescription = null,
                            tint = goldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "PRIMARY WEALTH GOAL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = goal.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textPrimaryColor
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = onEditWealthGoal,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) GoldDark else tierTheme.goldDark,
                            contentColor = if (isDark) GoldLight else Color.White
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp).testTag("profile_edit_wealth_goal_button")
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Goal", modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Target", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onNavigateToWealthGoalTracker,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) GoldPrimary else tierTheme.goldPrimary,
                            contentColor = if (isDark) RichBlack else Color.White
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp).testTag("profile_view_wealth_tracker_button")
                    ) {
                        Text(text = "Tracker", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = surfaceColor,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$currency${String.format(Locale.US, "%,.2f", current)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) AmberBright else tierTheme.goldDark
                        )
                        Text(
                            text = "Target: $currency${String.format(Locale.US, "%,.0f", target)} ($pct%)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textSecColor
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (isDark) GoldPrimary else tierTheme.goldDark,
                        trackColor = cardBorderColor.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Category: ${goal.category}",
                            fontSize = 10.sp,
                            color = textSecColor
                        )
                        Text(
                            text = if (daysLeft > 0) "$daysLeft days left" else "Target Date Reached",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LeaderboardPrivacySettingsCard(
    isOptedIn: Boolean,
    onToggleOptIn: (Boolean) -> Unit,
    onNavigateToLeaderboard: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    BrushedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_privacy_settings_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (isOptedIn) {
                                    if (isDark) GoldDark.copy(alpha = 0.5f) else tierTheme.goldPrimary.copy(alpha = 0.2f)
                                } else {
                                    if (isDark) DarkCharcoal else LightBorder.copy(alpha = 0.4f)
                                }
                            )
                            .border(
                                1.dp,
                                if (isOptedIn) goldAccent else (if (isDark) DarkBorder else LightBorder),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isOptedIn) Icons.Filled.EmojiEvents else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = if (isOptedIn) goldAccent else textSecColor,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "LEADERBOARD VISIBILITY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = if (isOptedIn) "Public Standing (Opted-in)" else "Private Ghost Mode (Opted-out)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }

                Switch(
                    checked = isOptedIn,
                    onCheckedChange = { onToggleOptIn(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = if (isDark) GoldLight else Color.White,
                        checkedTrackColor = if (isDark) GoldDark else tierTheme.goldDark,
                        uncheckedThumbColor = textSecColor,
                        uncheckedTrackColor = if (isDark) DarkCharcoal else LightBorder
                    ),
                    modifier = Modifier.testTag("leaderboard_opt_in_switch")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isOptedIn) {
                    "Your alias, tier, and metrics (XP, streaks, and module counts) appear on weekly and all-time leaderboards. Sensitive personal details are never shared."
                } else {
                    "You are in Private Ghost Mode. You can still view full leaderboard rankings and track your own position privately, but other members will not see your profile or alias."
                },
                fontSize = 11.sp,
                color = textSecColor,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = surfaceColor,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(0.6.dp, cardBorderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToLeaderboard() }
                    .testTag("settings_open_leaderboard_link")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.EmojiEvents,
                            contentDescription = null,
                            tint = goldAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "View Sovereign Leaderboard",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = goldAccent,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

private data class TierItem(
    val name: String,
    val xpReq: String,
    val flair: String,
    val color: Color
)
