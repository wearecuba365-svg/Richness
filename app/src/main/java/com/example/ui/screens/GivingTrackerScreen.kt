package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GivingGoalEntity
import com.example.data.model.GivingLogEntity
import com.example.ui.components.GivingGoalSettingsDialog
import com.example.ui.components.GivingLogItemCard
import com.example.ui.components.LogGivingActDialog
import com.example.ui.components.getGivingCategoryColor
import com.example.ui.components.getGivingCategoryIcon
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
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
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GivingTrackerScreen(
    goal: GivingGoalEntity?,
    logs: List<GivingLogEntity>,
    streakWeeks: Int,
    bestStreakWeeks: Int,
    selectedCategoryFilter: String = "ALL",
    isAmountsHidden: Boolean = false,
    onBack: () -> Unit,
    onLogGivingAct: (
        title: String,
        amount: Double?,
        currencySymbol: String,
        category: String,
        recipientName: String,
        note: String,
        saveToNotebook: Boolean
    ) -> Unit,
    onUpdateGivingGoal: (
        goalType: String,
        targetAmount: Double,
        targetPercentage: Double,
        targetActsCount: Int,
        currencySymbol: String,
        serviceMotto: String
    ) -> Unit,
    onDeleteLog: (Long) -> Unit,
    onUpdateLog: (GivingLogEntity) -> Unit,
    onSetCategoryFilter: (String) -> Unit = {},
    onToggleAmountsHidden: () -> Unit = {}
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldColor = if (isDark) GoldLight else tierTheme.goldDark
    val primaryText = if (isDark) TextPrimary else LightTextPrimary
    val secondaryText = if (isDark) TextSecondary else LightTextSecondary
    val mutedText = if (isDark) TextMuted else LightTextMuted
    val containerBg = if (isDark) RichBlack else Color(0xFFF9F9FB)
    val cardBorder = if (isDark) DarkBorder else LightBorder
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated

    var showLogDialog by remember { mutableStateOf(false) }
    var showGoalDialog by remember { mutableStateOf(false) }
    var logToEdit by remember { mutableStateOf<GivingLogEntity?>(null) }
    var logToDelete by remember { mutableStateOf<GivingLogEntity?>(null) }
    var isPrinciplesExpanded by remember { mutableStateOf(false) }

    val activeGoal = goal ?: GivingGoalEntity()
    val totalActs = logs.size
    val totalMonetaryAmount = logs.filter { it.isMonetary }.sumOf { it.amount ?: 0.0 }
    val currency = logs.firstOrNull { it.isMonetary && it.currencySymbol.isNotBlank() }?.currencySymbol ?: activeGoal.currencySymbol

    // Month Progress
    val now = System.currentTimeMillis()
    val cal = Calendar.getInstance().apply { timeInMillis = now }
    val currentMonth = cal.get(Calendar.MONTH)
    val currentYear = cal.get(Calendar.YEAR)

    val thisMonthLogs = remember(logs, currentMonth, currentYear) {
        logs.filter { log ->
            val c = Calendar.getInstance().apply { timeInMillis = log.timestamp }
            c.get(Calendar.MONTH) == currentMonth && c.get(Calendar.YEAR) == currentYear
        }
    }
    val thisMonthActsCount = thisMonthLogs.size
    val thisMonthTotalAmount = thisMonthLogs.filter { it.isMonetary }.sumOf { it.amount ?: 0.0 }

    // Filtered logs
    val filteredLogs = remember(logs, selectedCategoryFilter) {
        if (selectedCategoryFilter == "ALL") {
            logs
        } else {
            logs.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "GRATITUDE & GIVING",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldColor,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Circulating Abundance & Increasing Returns",
                            fontSize = 11.sp,
                            color = mutedText
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("giving_tracker_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = goldColor
                        )
                    }
                },
                actions = {
                    // Privacy Eye Toggle
                    IconButton(
                        onClick = onToggleAmountsHidden,
                        modifier = Modifier.testTag("giving_privacy_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isAmountsHidden) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (isAmountsHidden) "Show Amounts" else "Hide Amounts",
                            tint = if (isAmountsHidden) (if (isDark) AmberBright else GoldDark) else mutedText
                        )
                    }

                    // Goal Settings
                    IconButton(
                        onClick = { showGoalDialog = true },
                        modifier = Modifier.testTag("giving_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Goal Settings",
                            tint = goldColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) DarkCharcoal else LightElevated
                )
            )
        },
        containerColor = containerBg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .testTag("giving_tracker_screen"),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HERO / PHILOSOPHY BANNER
            item {
                Surface(
                    color = surfaceColor,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, goldColor.copy(alpha = if (isDark) 0.35f else 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(goldColor.copy(alpha = 0.2f))
                                        .border(1.dp, goldColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.VolunteerActivism,
                                        contentDescription = null,
                                        tint = goldColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "THE LAW OF INCREASING RETURNS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldColor,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            Surface(
                                color = goldColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "+35 XP / ACT",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "\"Riches do not respond to scarcity or hoarding. The master key to boundless wealth is to render more and better service than you are paid for. Conscious giving expands the channels through which infinite supply flows.\"",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = secondaryText
                        )

                        if (activeGoal.serviceMotto.isNotBlank()) {
                            Surface(
                                color = (if (isDark) RichBlack else Color.White).copy(alpha = 0.6f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(0.5.dp, cardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Personal Intention: \"${activeGoal.serviceMotto}\"",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = primaryText,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // METRICS GRID (Streak, Total Acts, Goal Status)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Giving Streak Card
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = surfaceColor,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(0.8.dp, cardBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = if (isDark) AmberBright else GoldDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "GIVING STREAK",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = mutedText,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Text(
                                    text = if (streakWeeks > 0) "$streakWeeks ${if (streakWeeks == 1) "Week" else "Weeks"}" else "0 Weeks",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    color = if (streakWeeks > 0) goldColor else primaryText
                                )
                                Text(
                                    text = if (bestStreakWeeks > 0) "Best: $bestStreakWeeks weeks" else "Consecutive weeks active",
                                    fontSize = 10.sp,
                                    color = mutedText
                                )
                            }
                        }

                        // Total Acts Card
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = surfaceColor,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(0.8.dp, cardBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        tint = goldColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "BENEVOLENCE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = mutedText,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Text(
                                    text = "$totalActs Acts",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    color = primaryText
                                )
                                Text(
                                    text = "$thisMonthActsCount this month",
                                    fontSize = 10.sp,
                                    color = goldColor
                                )
                            }
                        }
                    }

                    // Goal Status Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = surfaceColor,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(0.8.dp, cardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "INTENTIONAL TARGET",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = mutedText,
                                    letterSpacing = 0.5.sp
                                )

                                TextButton(
                                    onClick = { showGoalDialog = true },
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.height(24.dp)
                                ) {
                                    Text(
                                        text = if (activeGoal.isGoalActive) "Adjust Goal" else "Set Target",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = goldColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            when (activeGoal.goalType) {
                                GivingGoalEntity.GOAL_TYPE_FIXED_MONTHLY -> {
                                    val target = activeGoal.targetAmount.coerceAtLeast(1.0)
                                    val progress = (thisMonthTotalAmount / target).toFloat().coerceIn(0f, 1f)
                                    val pct = (progress * 100).toInt()

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isAmountsHidden) "Monthly Fixed Goal: •••• / ••••" else "Monthly Target: $currency${String.format(Locale.US, "%,.0f", thisMonthTotalAmount)} / $currency${String.format(Locale.US, "%,.0f", target)}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = primaryText
                                        )
                                        Text(
                                            text = "$pct%",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = goldColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = goldColor,
                                        trackColor = cardBorder
                                    )
                                }
                                GivingGoalEntity.GOAL_TYPE_ACTS_COUNT_MONTHLY -> {
                                    val target = activeGoal.targetActsCount.coerceAtLeast(1)
                                    val progress = (thisMonthActsCount.toFloat() / target.toFloat()).coerceIn(0f, 1f)
                                    val pct = (progress * 100).toInt()

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Cadence Target: $thisMonthActsCount of $target acts completed this month",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = primaryText
                                        )
                                        Text(
                                            text = "$pct%",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = goldColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = goldColor,
                                        trackColor = cardBorder
                                    )
                                }
                                GivingGoalEntity.GOAL_TYPE_PERCENTAGE_INCOME -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Tithing / Income % Target:",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = primaryText
                                        )
                                        Text(
                                            text = "${activeGoal.targetPercentage}% of Inflows",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = goldColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Circulating a tenth of incoming wealth back into service and creation.",
                                        fontSize = 11.sp,
                                        color = secondaryText
                                    )
                                }
                                else -> {
                                    Text(
                                        text = "Free Giving Mode (No target required)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryText
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Log acts freely whenever inspired. You can optionally set a recurring monthly or percentage goal anytime.",
                                        fontSize = 11.sp,
                                        color = secondaryText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // PRIMARY ACTION BUTTONS
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            logToEdit = null
                            showLogDialog = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("log_giving_act_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                            contentColor = RichBlack
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (isDark) RichBlack else Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LOG ACT OF GIVING",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = if (isDark) RichBlack else Color.White
                        )
                    }
                }
            }

            // CATEGORY FILTER BAR
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GIVING CHRONICLES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = mutedText,
                            letterSpacing = 0.8.sp
                        )

                        Text(
                            text = "${filteredLogs.size} ${if (filteredLogs.size == 1) "entry" else "entries"}",
                            fontSize = 11.sp,
                            color = mutedText
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            val isAll = selectedCategoryFilter == "ALL"
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isAll) goldColor.copy(alpha = if (isDark) 0.25f else 0.15f) else surfaceColor,
                                border = BorderStroke(
                                    if (isAll) 1.2.dp else 0.6.dp,
                                    if (isAll) goldColor else cardBorder
                                ),
                                modifier = Modifier.clickable { onSetCategoryFilter("ALL") }
                            ) {
                                Text(
                                    text = "All (${logs.size})",
                                    fontSize = 11.sp,
                                    fontWeight = if (isAll) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isAll) goldColor else secondaryText,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        items(GivingLogEntity.ALL_CATEGORIES) { cat ->
                            val isSelected = selectedCategoryFilter.equals(cat, ignoreCase = true)
                            val catCount = logs.count { it.category.equals(cat, ignoreCase = true) }
                            val catColor = getGivingCategoryColor(cat, isDark)
                            val catIcon = getGivingCategoryIcon(cat)

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) catColor.copy(alpha = if (isDark) 0.25f else 0.15f) else surfaceColor,
                                border = BorderStroke(
                                    if (isSelected) 1.2.dp else 0.6.dp,
                                    if (isSelected) catColor else cardBorder
                                ),
                                modifier = Modifier.clickable { onSetCategoryFilter(cat) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = catIcon,
                                        contentDescription = null,
                                        tint = if (isSelected) catColor else mutedText,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "$cat ($catCount)",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) catColor else secondaryText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // GIVING HISTORY LIST
            if (filteredLogs.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = surfaceColor,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(0.8.dp, cardBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(goldColor.copy(alpha = 0.15f))
                                    .border(1.dp, goldColor.copy(alpha = 0.5f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.VolunteerActivism,
                                    contentDescription = null,
                                    tint = goldColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                text = if (selectedCategoryFilter == "ALL") "No Giving Acts Logged Yet" else "No Acts in '$selectedCategoryFilter'",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Text(
                                text = "Every seed of benevolence planted in the world enriches your spirit and expands your capacity for abundance.",
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp,
                                color = secondaryText,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = {
                                    logToEdit = null
                                    showLogDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                                    contentColor = RichBlack
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "+ Log First Act",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (isDark) RichBlack else Color.White
                                )
                            }
                        }
                    }
                }
            } else {
                items(filteredLogs, key = { it.id }) { log ->
                    GivingLogItemCard(
                        log = log,
                        isAmountsHidden = isAmountsHidden,
                        onEdit = {
                            logToEdit = log
                            showLogDialog = true
                        },
                        onDelete = {
                            logToDelete = log
                        }
                    )
                }
            }

            // NAPOLEON HILL WISDOM PRINCIPLES CARD
            item {
                Surface(
                    color = surfaceColor,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(0.8.dp, cardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isPrinciplesExpanded = !isPrinciplesExpanded },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Psychology,
                                    contentDescription = null,
                                    tint = goldColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "3 SOVEREIGN LAWS OF GENEROSITY",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldColor,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Icon(
                                imageVector = if (isPrinciplesExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = null,
                                tint = mutedText,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        if (isPrinciplesExpanded) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Surface(
                                    color = (if (isDark) RichBlack else Color.White).copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(0.5.dp, cardBorder)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "1. The Law of Increasing Returns",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryText
                                        )
                                        Text(
                                            text = "Always render more value and more service than you are compensated for. The compound interest of goodwill and excellence inevitably returns in multiplying forms.",
                                            fontSize = 10.5.sp,
                                            lineHeight = 15.sp,
                                            color = secondaryText
                                        )
                                    }
                                }

                                Surface(
                                    color = (if (isDark) RichBlack else Color.White).copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(0.5.dp, cardBorder)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "2. Circulation vs. Stagnation",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryText
                                        )
                                        Text(
                                            text = "Wealth is like a river — holding onto every droplet in fear turns water stagnant. Circulating value with intentionality signals unwavering confidence in infinite source.",
                                            fontSize = 10.5.sp,
                                            lineHeight = 15.sp,
                                            color = secondaryText
                                        )
                                    }
                                }

                                Surface(
                                    color = (if (isDark) RichBlack else Color.White).copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(0.5.dp, cardBorder)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "3. Spirit of Joyful Freedom",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryText
                                        )
                                        Text(
                                            text = "Give without guilt or obligation. The emotional frequency with which you give — joy and gratitude — dictates the harvest you reap.",
                                            fontSize = 10.5.sp,
                                            lineHeight = 15.sp,
                                            color = secondaryText
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // MODAL DIALOGS

    // Log / Edit Giving Act Dialog
    if (showLogDialog) {
        LogGivingActDialog(
            initialLog = logToEdit,
            onDismiss = {
                showLogDialog = false
                logToEdit = null
            },
            onSave = { title, amount, cur, cat, rec, note, toNotebook ->
                if (logToEdit == null) {
                    onLogGivingAct(title, amount, cur, cat, rec, note, toNotebook)
                } else {
                    val updated = logToEdit!!.copy(
                        title = title,
                        amount = amount,
                        isMonetary = (amount != null && amount > 0),
                        currencySymbol = cur,
                        category = cat,
                        recipientName = rec,
                        note = note
                    )
                    onUpdateLog(updated)
                }
                showLogDialog = false
                logToEdit = null
            }
        )
    }

    // Goal Setup Dialog
    if (showGoalDialog) {
        GivingGoalSettingsDialog(
            currentGoal = activeGoal,
            onDismiss = { showGoalDialog = false },
            onSaveGoal = { type, amount, pct, acts, cur, motto ->
                onUpdateGivingGoal(type, amount, pct, acts, cur, motto)
                showGoalDialog = false
            }
        )
    }

    // Delete Log Dialog
    if (logToDelete != null) {
        val item = logToDelete!!
        AlertDialog(
            onDismissRequest = { logToDelete = null },
            containerColor = if (isDark) DarkCharcoal else LightElevated,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = "Delete Giving Record?",
                    color = primaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove '${item.title}' from your giving chronicles?",
                    color = secondaryText,
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteLog(item.id)
                        logToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6E81),
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { logToDelete = null }) {
                    Text("Cancel", color = mutedText)
                }
            }
        )
    }
}
