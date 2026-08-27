package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WealthGoalEntity
import com.example.data.model.WealthGoalLogEntity
import com.example.ui.components.EditWealthGoalDialog
import com.example.ui.components.LogContributionDialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WealthGoalTrackerScreen(
    goal: WealthGoalEntity?,
    logs: List<WealthGoalLogEntity>,
    onBack: () -> Unit,
    onSaveGoal: (
        title: String,
        targetAmount: Double,
        startingAmount: Double,
        targetDateEpochMillis: Long,
        currencySymbol: String,
        category: String,
        servicePledge: String
    ) -> Unit,
    onLogContribution: (
        amount: Double,
        isMilestoneOnly: Boolean,
        title: String,
        note: String,
        saveToNotebook: Boolean
    ) -> Unit,
    onDeleteLog: (Long) -> Unit,
    onNavigateToIncomeIdeaExplorer: () -> Unit = {}
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showLogDialog by remember { mutableStateOf(false) }
    var logDialogInitialTab by remember { mutableIntStateOf(0) }
    var logToDelete by remember { mutableStateOf<WealthGoalLogEntity?>(null) }
    var selectedFilterTab by remember { mutableIntStateOf(0) } // 0: All, 1: Inflows, 2: Milestones

    val activeGoal = goal ?: WealthGoalEntity()
    val currency = activeGoal.currencySymbol
    val target = activeGoal.targetAmount.coerceAtLeast(1.0)
    val current = activeGoal.currentAmount
    val progressFraction = (current / target).toFloat().coerceIn(0f, 1f)
    val percentage = (progressFraction * 100).toInt()

    val now = System.currentTimeMillis()
    val daysRemaining = ((activeGoal.targetDateEpochMillis - now) / (24 * 60 * 60 * 1000L)).coerceAtLeast(0)
    val remainingAmount = (target - current).coerceAtLeast(0.0)
    val dailyPace = if (daysRemaining > 0) remainingAmount / daysRemaining else 0.0
    val weeklyPace = dailyPace * 7

    val sdf = remember { SimpleDateFormat("MMMM d, yyyy", Locale.US) }
    val formattedTargetDate = remember(activeGoal.targetDateEpochMillis) {
        sdf.format(Date(activeGoal.targetDateEpochMillis))
    }

    val totalContributionsLogged = logs.filter { !it.isMilestoneOnly }.sumOf { it.amount }
    val totalMilestonesCount = logs.count { it.isMilestoneOnly }

    val filteredLogs = when (selectedFilterTab) {
        1 -> logs.filter { !it.isMilestoneOnly }
        2 -> logs.filter { it.isMilestoneOnly }
        else -> logs
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "WEALTH GOAL TRACKER",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Definite Aim Accumulation Horizon",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("wealth_tracker_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GoldLight
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.testTag("wealth_tracker_edit_goal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Goal",
                            tint = GoldPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RichBlack)
            )
        },
        containerColor = RichBlack
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("wealth_goal_tracker_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HERO ACCUMULATION CARD
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.5.dp,
                            Brush.linearGradient(listOf(GoldPrimary, AmberBright, GoldDark)),
                            RoundedCornerShape(22.dp)
                        )
                        .clip(RoundedCornerShape(22.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkCharcoal.copy(alpha = 0.95f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = RichBlack.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                            ) {
                                Text(
                                    text = activeGoal.category.uppercase(Locale.US),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberBright,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.HourglassTop,
                                    contentDescription = null,
                                    tint = GoldLight,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (daysRemaining > 0) "$daysRemaining days left" else "Target Date Reached",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = GoldLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Large Circular Radial Gauge
                        Box(
                            modifier = Modifier.size(160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(145.dp)) {
                                val strokeWidth = 14.dp.toPx()
                                val arcSize = size.width - strokeWidth
                                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                                // Background track
                                drawArc(
                                    color = SurfaceElevated,
                                    startAngle = 135f,
                                    sweepAngle = 270f,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )

                                // Foreground gradient gold arc
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(GoldDark, AmberBright, GoldLight, GoldPrimary)
                                    ),
                                    startAngle = 135f,
                                    sweepAngle = 270f * progressFraction,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$percentage%",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberBright,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = "TRANSMUTED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = activeGoal.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "$currency${String.format(Locale.US, "%,.2f", current)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "/ $currency${String.format(Locale.US, "%,.0f", target)}",
                                fontSize = 14.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Horizon details bar
                        Surface(
                            color = RichBlack.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "Target Date", fontSize = 9.sp, color = TextMuted)
                                    Text(text = formattedTargetDate, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "Remaining To Accumulate", fontSize = 9.sp, color = TextMuted)
                                    Text(
                                        text = "$currency${String.format(Locale.US, "%,.0f", remainingAmount)}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberBright
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // TRANSMUTATION PLEDGE CARD
            if (activeGoal.servicePledge.isNotBlank()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkCharcoal.copy(alpha = 0.7f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = AmberBright,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "SOVEREIGN TRANSMUTATION PLEDGE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "\"${activeGoal.servicePledge}\"",
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            // PACING & STATS GRID
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Daily Pace Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkCharcoal.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.TrendingUp, contentDescription = null, tint = AmberBright, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Daily Pace", fontSize = 10.sp, color = TextMuted)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$currency${String.format(Locale.US, "%,.0f", dailyPace)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                            Text(text = "needed per day", fontSize = 9.sp, color = TextMuted)
                        }
                    }

                    // Weekly Pace Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkCharcoal.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = GoldLight, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Weekly Pace", fontSize = 10.sp, color = TextMuted)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$currency${String.format(Locale.US, "%,.0f", weeklyPace)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                            Text(text = "needed per week", fontSize = 9.sp, color = TextMuted)
                        }
                    }

                    // Inflows Count Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkCharcoal.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Flag, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Milestones", fontSize = 10.sp, color = TextMuted)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$totalMilestonesCount",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberBright
                            )
                            Text(text = "inscribed to date", fontSize = 9.sp, color = TextMuted)
                        }
                    }
                }
            }

            // ACTION BUTTONS
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            logDialogInitialTab = 0
                            showLogDialog = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("record_inflow_screen_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = RichBlack
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Record Inflow", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            logDialogInitialTab = 1
                            showLogDialog = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("inscribe_milestone_screen_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberBright),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberBright.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.Flag, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Inscribe Milestone", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // INCOME IDEA EXPLORER SHORTCUT
            item {
                Surface(
                    color = DarkCharcoal.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onNavigateToIncomeIdeaExplorer() }
                        .testTag("wealth_goal_income_ideas_shortcut")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(GoldPrimary.copy(alpha = 0.15f), CircleShape)
                                .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Lightbulb,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "EXPEDITE YOUR PACE: EXPLORE INCOME VEHICLES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Browse skills, digital products, and cash flow models mapped to Hill's principles.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // TIMELINE SECTION HEADER & FILTER CHIPS
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TRANSMUTATION TIMELINE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${logs.size} Entries",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Filter chips row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("All Entries", "Inflows", "Milestones").forEachIndexed { index, label ->
                            val isSelected = selectedFilterTab == index
                            Surface(
                                color = if (isSelected) GoldPrimary else DarkCharcoal,
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) AmberBright else DarkBorder
                                ),
                                modifier = Modifier
                                    .clickable { selectedFilterTab = index }
                                    .testTag("timeline_filter_$index")
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) RichBlack else TextSecondary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // TIMELINE ITEMS
            if (filteredLogs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkCharcoal.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MonetizationOn,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Inflows Logged Yet",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Record your first cash accumulation or milestone to track your definite progress.",
                                fontSize = 11.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredLogs, key = { it.id }) { log ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkCharcoal.copy(alpha = 0.85f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
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
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (log.isMilestoneOnly) AmberAccent.copy(alpha = 0.2f)
                                                else GoldPrimary.copy(alpha = 0.2f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (log.isMilestoneOnly) Icons.Filled.Flag else Icons.Filled.MonetizationOn,
                                            contentDescription = null,
                                            tint = if (log.isMilestoneOnly) AmberBright else GoldLight,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = log.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = log.dateFormatted,
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (!log.isMilestoneOnly) {
                                        Text(
                                            text = "+$currency${String.format(Locale.US, "%,.2f", log.amount)}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AmberBright
                                        )
                                    } else {
                                        Surface(
                                            color = AmberAccent.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "MILESTONE",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AmberBright,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { logToDelete = log },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete entry",
                                            tint = TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            if (log.note.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = RichBlack.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = log.note,
                                        fontSize = 11.5.sp,
                                        color = TextSecondary,
                                        lineHeight = 16.sp,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            if (!log.isMilestoneOnly) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "Accumulated Total: $currency${String.format(Locale.US, "%,.2f", log.resultingTotal)}",
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // PHILOSOPHICAL FOOTER NOTE
            item {
                Surface(
                    color = SurfaceElevated.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = GoldLight, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "THE LAW OF FINANCIAL TRANSMUTATION",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Riches come in response to definite demands, based upon the application of definite principles, and not by chance or luck. Read your definite aim aloud every morning and night until you see and feel and believe yourself already in possession of the money.",
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = TextSecondary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }

    // Edit Wealth Goal Dialog
    if (showEditDialog) {
        EditWealthGoalDialog(
            currentGoal = activeGoal,
            onDismiss = { showEditDialog = false },
            onSave = { title, targetAmount, startingAmount, targetDateEpochMillis, currencySymbol, category, servicePledge ->
                onSaveGoal(title, targetAmount, startingAmount, targetDateEpochMillis, currencySymbol, category, servicePledge)
                showEditDialog = false
            }
        )
    }

    // Log Contribution Dialog
    if (showLogDialog) {
        LogContributionDialog(
            goal = activeGoal,
            initialTab = logDialogInitialTab,
            onDismiss = { showLogDialog = false },
            onLog = { amount, isMilestoneOnly, title, note, saveToNotebook ->
                onLogContribution(amount, isMilestoneOnly, title, note, saveToNotebook)
                showLogDialog = false
            }
        )
    }

    // Delete Log Confirmation Dialog
    if (logToDelete != null) {
        val entry = logToDelete!!
        AlertDialog(
            onDismissRequest = { logToDelete = null },
            containerColor = RichBlack,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(text = "Delete Inflow Entry?", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "Are you sure you want to delete '${entry.title}'? The goal's accumulated balance will be recalibrated.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteLog(entry.id)
                        logToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberBright, contentColor = RichBlack),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { logToDelete = null }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}
