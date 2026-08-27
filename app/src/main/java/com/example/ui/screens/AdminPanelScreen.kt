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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MastermindCheckinEntity
import com.example.data.model.MastermindGroupEntity
import com.example.data.model.MastermindMemberEntity
import com.example.data.model.ModuleEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.components.BrushedCard
import com.example.ui.components.ThemeSelectorCard
import com.example.ui.components.TierBadgeChip
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Calendar

@Composable
fun AdminPanelScreen(
    userProfile: UserProfileEntity?,
    modules: List<ModuleEntity>,
    mastermindGroups: List<MastermindGroupEntity> = emptyList(),
    mastermindMembers: List<MastermindMemberEntity> = emptyList(),
    mastermindCheckins: List<MastermindCheckinEntity> = emptyList(),
    onboardingLogs: List<com.example.data.model.OnboardingStepLogEntity> = emptyList(),
    themeMode: AppThemeMode = AppThemeMode.DARK,
    isFloatingMoneyBubblesEnabled: Boolean = true,
    onBack: () -> Unit,
    onUnlockAllModules: () -> Unit,
    onResetAllProgress: () -> Unit,
    onResetOnboarding: () -> Unit = {},
    onAddXp: (Int) -> Unit,
    onSetThemeMode: (AppThemeMode) -> Unit = {},
    onToggleFloatingMoneyBubbles: () -> Unit = {},
    onSetTier: (String) -> Unit = {}
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("admin_panel_screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { Spacer(modifier = Modifier.height(6.dp)) }

        // --- TOP HEADER ---
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(44.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GoldLight
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "ARCHITECT CONSOLE & SAAS TELEMETRY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Course Curator & Growth Engine",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // --- APP THEME TOGGLE CARD ---
        item {
            ThemeSelectorCard(
                currentThemeMode = themeMode,
                onThemeChange = onSetThemeMode,
                isFloatingMoneyBubblesEnabled = isFloatingMoneyBubblesEnabled,
                onToggleFloatingMoneyBubbles = onToggleFloatingMoneyBubbles
            )
        }

        // --- SAAS GROWTH METRICS ---
        item {
            BrushedCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE PLATFORM TELEMETRY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.TrendingUp,
                        contentDescription = null,
                        tint = AmberAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBox(title = "Total Initiations", value = "14,820", sub = "+18% WoW")
                    MetricBox(title = "Paid Conversion", value = "9.4%", sub = "Free → Sovereign")
                    MetricBox(title = "Avg Mindset Lift", value = "+28.2", sub = "Post Vault 3")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBox(title = "D14 Retention", value = "78.6%", sub = "Daily ritual")
                    MetricBox(title = "Notebook Inscriptions", value = "89.4k", sub = "Saved reflections")
                    MetricBox(title = "ARR Run Rate", value = "$1.43M", sub = "Sovereign Tier")
                }
            }
        }

        // --- OVERRIDE SANDBOX & TESTING TOOLS ---
        item {
            BrushedCard {
                Text(
                    text = "USER STATE SANDBOX (DEV OVERRIDES)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Simulate full course progression, add XP, or reset for QA.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onAddXp(500) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberAccent,
                            contentColor = RichBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "+500 XP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onAddXp(2000) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = RichBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "+2,000 XP", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onUnlockAllModules,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Filled.LockOpen, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Unlock All Vaults", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onResetAllProgress,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Reset Progress", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onResetOnboarding,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberAccent),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberAccent.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Test First-Time Onboarding Flow (Reset to Step 1)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- ONBOARDING FUNNEL & STEP COMPLETION RATES ---
        item {
            BrushedCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ONBOARDING FUNNEL & STEP COMPLETION RATES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Conversion tracking across the 5-step Sovereign Initiation sequence.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Surface(
                        color = if (userProfile?.hasCompletedOnboarding == true) SuccessGreen.copy(alpha = 0.2f) else AmberAccent.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (userProfile?.hasCompletedOnboarding == true) SuccessGreen else AmberAccent)
                    ) {
                        Text(
                            text = if (userProfile?.hasCompletedOnboarding == true) "ONBOARDED (100%)" else "STEP ${userProfile?.onboardingStep ?: 1} / 5",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (userProfile?.hasCompletedOnboarding == true) SuccessGreen else AmberAccent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBox(title = "Funnel Completion", value = "84.8%", sub = "12,567 / 14,820 users")
                    MetricBox(title = "Avg Step Duration", value = "2m 18s", sub = "Fast-track calibration")
                    MetricBox(title = "Aim Inscription Rate", value = "91.4%", sub = "DMP declared")
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "STEP-BY-STEP FUNNEL DROP-OFF ANALYSIS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = GoldLight
                )
                Spacer(modifier = Modifier.height(8.dp))

                val funnelSteps = listOf(
                    Triple("Step 1: The Calling (Monk Motif & Purpose)", "100.0%", 100),
                    Triple("Step 2: Sovereign Identity (Name Inscription)", "96.2%", 96),
                    Triple("Step 3: Definite Chief Aim (DMP & Affirmation)", "91.4%", 91),
                    Triple("Step 4: Mindset Assessment (5 Dimensions)", "88.0%", 88),
                    Triple("Step 5: Sovereign Arsenal & Dashboard Handoff", "84.8%", 85)
                )

                funnelSteps.forEach { (name, percent, progress) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = name,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(DarkCharcoal)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress / 100f)
                                    .height(5.dp)
                                    .background(androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(GoldDark, GoldLight)))
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = percent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            modifier = Modifier.width(42.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.End
                        )
                    }
                }

                if (onboardingLogs.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "RECENT USER STEP LOGS (ROOM PERSISTED):",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    onboardingLogs.takeLast(4).reversed().forEach { log ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "• Step ${log.stepNumber}: ${log.stepName}",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (log.isCompleted) "✓ Complete" else "→ Started",
                                fontSize = 10.sp,
                                color = if (log.isCompleted) SuccessGreen else AmberAccent
                            )
                        }
                    }
                }
            }
        }

        // --- ROOM OFFLINE PERSISTENCE & CACHING ARCHITECTURE ---
        item {
            BrushedCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ROOM OFFLINE CACHING ARCHITECTURE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Local SQLite Room database guarantees 100% offline access.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Surface(
                        color = SuccessGreen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen)
                    ) {
                        Text(
                            text = "OFFLINE READY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val completedCount = modules.count { it.isCompleted }
                val questCount = modules.count { it.isQuestCompleted }
                val worksheetsCount = modules.count { it.savedField1.isNotBlank() }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBox(title = "Modules Cached", value = "${modules.size} / 14", sub = "All Vaults in SQLite")
                    MetricBox(title = "Lessons Finished", value = "$completedCount / 14", sub = "Persisted Locally")
                    MetricBox(title = "Quests Fulfilled", value = "$questCount / 14", sub = "Offline Verification")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBox(title = "Worksheets Cached", value = "$worksheetsCount Saved", sub = "Offline Auto-Save")
                    MetricBox(title = "Active Streak", value = "${userProfile?.currentStreak ?: 1} Days", sub = "Calendar Date Delta")
                    MetricBox(title = "DB Version", value = "v1.0 Room", sub = "riches_protocol_db")
                }
            }
        }

        // --- DYNAMIC TIER GOLD INTENSITY SIMULATOR ---
        item {
            BrushedCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "METALLIC GOLD LUSTER SIMULATOR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = AmberBright,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "Test dynamic gold halo, border lusters, and ember particles in real-time.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                val tierOptions = listOf(
                    Triple("Novice", "25% Luster", "0 XP"),
                    Triple("Builder", "45% Luster", "500 XP"),
                    Triple("Architect", "65% Luster", "1.5k XP"),
                    Triple("Sovereign", "85% Luster", "3.5k XP"),
                    Triple("Legacy", "100% Luster", "7k+ XP")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tierOptions.forEach { (tierName, luster, xp) ->
                        val isSelected = userProfile?.tierName?.equals(tierName, ignoreCase = true) == true
                        Surface(
                            color = if (isSelected) GoldPrimary.copy(alpha = 0.25f) else DarkCharcoal,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) GoldLight else DarkBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSetTier(tierName) }
                                .testTag("tier_sim_${tierName.lowercase()}")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = tierName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) GoldLight else TextPrimary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Text(
                                    text = luster,
                                    fontSize = 8.sp,
                                    color = if (isSelected) AmberBright else TextMuted,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- COURSE VAULTS MANAGEMENT ---
        item {
            BrushedCard {
                Text(
                    text = "THE 13 VAULTS CONTENT REPOSITORY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                modules.forEach { module ->
                    Surface(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Vault ${module.order}: ${module.title}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${module.originalPrinciple} • +${module.xpReward} XP",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (module.isCompleted) {
                                    Surface(
                                        color = SuccessGreen.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "Conquered",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SuccessGreen,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                } else if (module.isUnlocked) {
                                    Surface(
                                        color = GoldPrimary.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "Unlocked",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldLight,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                } else {
                                    Surface(
                                        color = DarkCharcoal,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "Paid Gate",
                                            fontSize = 9.sp,
                                            color = TextMuted,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- MASTERMIND CIRCLES TELEMETRY ---
        item {
            BrushedCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "MASTERMIND CIRCLES TELEMETRY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                color = if (isDark) GoldPrimary else tierTheme.goldDark
                            )
                            Text(
                                text = "Active Groups & Participation Rates",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) TextPrimary else LightTextPrimary
                            )
                        }

                        Surface(
                            color = SuccessGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${mastermindGroups.size} Circles Active",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val cal = Calendar.getInstance()
                    val curWeek = cal.get(Calendar.WEEK_OF_YEAR)
                    val totalMembers = mastermindMembers.size
                    val totalCheckinsThisWeek = mastermindCheckins.filter { it.weekNumber == curWeek }.size
                    val overallParticipation = if (totalMembers > 0) ((totalCheckinsThisWeek.toFloat() / totalMembers) * 100).toInt() else 0

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricBox(
                            title = "Cohorts",
                            value = "${mastermindGroups.size}",
                            sub = "Active"
                        )
                        MetricBox(
                            title = "Members",
                            value = "$totalMembers",
                            sub = "Total"
                        )
                        MetricBox(
                            title = "Wk $curWeek Rate",
                            value = "$overallParticipation%",
                            sub = "$totalCheckinsThisWeek Checked in"
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Active Cohorts Breakdown:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) TextSecondary else LightTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    mastermindGroups.forEach { group ->
                        val groupMembersList = mastermindMembers.filter { it.groupId == group.id }
                        val groupCheckinsThisWeek = mastermindCheckins.filter { it.groupId == group.id && it.weekNumber == curWeek }
                        val rate = if (groupMembersList.isNotEmpty()) ((groupCheckinsThisWeek.size.toFloat() / groupMembersList.size) * 100).toInt() else 0

                        Surface(
                            color = if (isDark) DarkCharcoal else LightElevated,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = group.name,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDark) GoldLight else tierTheme.goldDark
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            TierBadgeChip(tier = group.targetTier)
                                        }
                                        Text(
                                            text = "Code: ${group.inviteCode} • Motto: “${group.motto}”",
                                            fontSize = 10.sp,
                                            color = TextMuted,
                                            maxLines = 1
                                        )
                                    }

                                    Surface(
                                        color = if (rate >= 60) SuccessGreen.copy(alpha = 0.2f) else AmberAccent.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "$rate% Wk Rate",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (rate >= 60) SuccessGreen else AmberBright,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Members: ${groupMembersList.size}/${group.maxMembers}",
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = "Streak: 🔥 ${group.groupStreakWeeks} Wks",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberBright
                                    )
                                    Text(
                                        text = "Combined XP: ⚡ ${group.combinedXpThisWeek} XP",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) GoldLight else tierTheme.goldDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    sub: String
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    Surface(
        color = if (isDark) SurfaceElevated else LightElevated,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
        modifier = Modifier.width(102.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (isDark) GoldLight else tierTheme.goldDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 9.sp,
                color = if (isDark) TextPrimary else LightTextPrimary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sub,
                fontSize = 8.sp,
                color = if (isDark) AmberAccent else tierTheme.goldDark,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
