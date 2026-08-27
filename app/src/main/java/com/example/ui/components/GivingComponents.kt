package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GivingGoalEntity
import com.example.data.model.GivingLogEntity
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

/**
 * Returns the corresponding icon for a giving category.
 */
fun getGivingCategoryIcon(category: String): ImageVector {
    return when (category) {
        GivingLogEntity.CATEGORY_CHARITY -> Icons.Filled.Favorite
        GivingLogEntity.CATEGORY_FAMILY -> Icons.Filled.Groups
        GivingLogEntity.CATEGORY_COMMUNITY -> Icons.Filled.Public
        GivingLogEntity.CATEGORY_TIPPING -> Icons.Filled.LocalCafe
        GivingLogEntity.CATEGORY_TIME_MENTORSHIP -> Icons.Filled.School
        GivingLogEntity.CATEGORY_KINDNESS -> Icons.Filled.AutoAwesome
        else -> Icons.Filled.VolunteerActivism
    }
}

/**
 * Returns a distinct accent color for the category.
 */
fun getGivingCategoryColor(category: String, isDark: Boolean): Color {
    return when (category) {
        GivingLogEntity.CATEGORY_CHARITY -> if (isDark) Color(0xFFFF6E81) else Color(0xFFD32F2F)
        GivingLogEntity.CATEGORY_FAMILY -> if (isDark) Color(0xFF64B5F6) else Color(0xFF1976D2)
        GivingLogEntity.CATEGORY_COMMUNITY -> if (isDark) Color(0xFF81C784) else Color(0xFF388E3C)
        GivingLogEntity.CATEGORY_TIPPING -> if (isDark) Color(0xFFFFB74D) else Color(0xFFF57C00)
        GivingLogEntity.CATEGORY_TIME_MENTORSHIP -> if (isDark) Color(0xFFBA68C8) else Color(0xFF7B1FA2)
        GivingLogEntity.CATEGORY_KINDNESS -> if (isDark) Color(0xFFFFD54F) else Color(0xFFFBC02D)
        else -> if (isDark) GoldLight else GoldDark
    }
}

/**
 * Dashboard Card for Gratitude & Giving Tracker
 */
@Composable
fun GivingDashboardWidget(
    goal: GivingGoalEntity?,
    logs: List<GivingLogEntity>,
    streakWeeks: Int,
    onLogGivingClick: () -> Unit,
    onOpenTrackerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldColor = if (isDark) GoldLight else tierTheme.goldDark
    val primaryText = if (isDark) TextPrimary else LightTextPrimary
    val secondaryText = if (isDark) TextSecondary else LightTextSecondary
    val mutedText = if (isDark) TextMuted else LightTextMuted
    val cardBorder = if (isDark) DarkBorder else LightBorder
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated

    var isExpanded by remember { mutableStateOf(false) }
    val totalActs = logs.size
    val activeGoal = goal ?: GivingGoalEntity()

    // Calculate this month's progress if a goal is active
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

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dashboard_giving_tracker_widget")
    ) {
        Column(
            modifier = Modifier.animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        goldColor.copy(alpha = if (isDark) 0.35f else 0.25f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(1.2.dp, goldColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.VolunteerActivism,
                            contentDescription = "Giving & Abundance",
                            tint = goldColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "GRATITUDE & GIVING",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            letterSpacing = 1.sp,
                            color = primaryText
                        )
                        Text(
                            text = if (streakWeeks > 0) "🔥 $streakWeeks-Week Giving Streak Active" else "Law of Increasing Returns",
                            fontSize = 11.sp,
                            color = if (streakWeeks > 0) (if (isDark) AmberBright else GoldDark) else secondaryText,
                            fontWeight = if (streakWeeks > 0) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }

                Surface(
                    color = goldColor.copy(alpha = if (isDark) 0.15f else 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.6.dp, goldColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "$totalActs ${if (totalActs == 1) "ACT" else "ACTS"}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = goldColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Key Metrics Pill Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Streak Card
                Surface(
                    modifier = Modifier.weight(1f),
                    color = surfaceColor,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.8.dp, cardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = null,
                                tint = if (isDark) AmberBright else GoldDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "GIVING STREAK",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = mutedText
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (streakWeeks > 0) "$streakWeeks ${if (streakWeeks == 1) "Week" else "Weeks"}" else "Starting",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (streakWeeks > 0) goldColor else primaryText
                        )
                    }
                }

                // Total Acts Card
                Surface(
                    modifier = Modifier.weight(1f),
                    color = surfaceColor,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.8.dp, cardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = goldColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "BENEVOLENCE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = mutedText
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$totalActs Logged",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                    }
                }
            }

            // Goal Progress Section (if goal is set)
            if (activeGoal.isGoalActive) {
                Surface(
                    color = surfaceColor,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.8.dp, cardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
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
                                        text = "Monthly Giving Goal: ${activeGoal.currencySymbol}${String.format(Locale.US, "%,.0f", thisMonthTotalAmount)} / ${activeGoal.currencySymbol}${String.format(Locale.US, "%,.0f", target)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = primaryText
                                    )
                                    Text(
                                        text = "$pct%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = goldColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = goldColor,
                                    trackColor = cardBorder.copy(alpha = 0.5f)
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
                                        text = "Monthly Cadence: $thisMonthActsCount of $target acts completed",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = primaryText
                                    )
                                    Text(
                                        text = "$pct%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = goldColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = goldColor,
                                    trackColor = cardBorder.copy(alpha = 0.5f)
                                )
                            }
                            GivingGoalEntity.GOAL_TYPE_PERCENTAGE_INCOME -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Tithing & Giving Target:",
                                        fontSize = 11.sp,
                                        color = secondaryText
                                    )
                                    Text(
                                        text = "${activeGoal.targetPercentage}% of Inflows",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = goldColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Recent Act Preview (if available)
            val mostRecent = logs.firstOrNull()
            if (mostRecent != null) {
                Surface(
                    color = surfaceColor.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.5.dp, cardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val catIcon = getGivingCategoryIcon(mostRecent.category)
                        val catColor = getGivingCategoryColor(mostRecent.category, isDark)
                        Icon(
                            imageVector = catIcon,
                            contentDescription = null,
                            tint = catColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Latest: ${mostRecent.title}",
                            fontSize = 11.sp,
                            color = primaryText,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        if (mostRecent.isMonetary && mostRecent.amount != null) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${mostRecent.currencySymbol}${String.format(Locale.US, "%,.0f", mostRecent.amount)}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = goldColor
                            )
                        }
                    }
                }
            }

            // Expandable Philosophy Quote
            if (isExpanded) {
                Surface(
                    color = surfaceColor,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.5.dp, cardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.FormatQuote,
                                contentDescription = null,
                                tint = goldColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "NAPOLEON HILL ON GIVING",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldColor,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"The man who does more than he is paid for will soon be paid for more than he does. Giving freely affirms your subconscious belief in infinite supply.\"",
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = secondaryText,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onLogGivingClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_log_giving_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (isDark) RichBlack else Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "LOG GIVING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) RichBlack else Color.White
                    )
                }

                OutlinedButton(
                    onClick = onOpenTrackerClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_open_giving_tracker_button"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = goldColor
                    ),
                    border = BorderStroke(1.dp, goldColor.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "VIEW TRACKER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = goldColor
                    )
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "Toggle Details",
                        tint = mutedText
                    )
                }
            }
        }
    }
}

/**
 * Dialog for Quick-Logging an Act of Generosity
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LogGivingActDialog(
    initialLog: GivingLogEntity? = null,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        amount: Double?,
        currencySymbol: String,
        category: String,
        recipientName: String,
        note: String,
        saveToNotebook: Boolean
    ) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldColor = if (isDark) GoldLight else tierTheme.goldDark
    val primaryText = if (isDark) TextPrimary else LightTextPrimary
    val secondaryText = if (isDark) TextSecondary else LightTextSecondary
    val mutedText = if (isDark) TextMuted else LightTextMuted
    val containerBg = if (isDark) DarkCharcoal else LightElevated
    val cardBorder = if (isDark) DarkBorder else LightBorder

    var title by remember { mutableStateOf(initialLog?.title ?: "") }
    var amountStr by remember { mutableStateOf(initialLog?.amount?.toString() ?: "") }
    var currencySymbol by remember { mutableStateOf(initialLog?.currencySymbol ?: "$") }
    var selectedCategory by remember { mutableStateOf(initialLog?.category ?: GivingLogEntity.CATEGORY_CHARITY) }
    var recipientName by remember { mutableStateOf(initialLog?.recipientName ?: "") }
    var note by remember { mutableStateOf(initialLog?.note ?: "") }
    var saveToNotebook by remember { mutableStateOf(false) }

    var isTitleError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = containerBg,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(goldColor.copy(alpha = 0.2f))
                        .border(1.dp, goldColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolunteerActivism,
                        contentDescription = null,
                        tint = goldColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = if (initialLog == null) "LOG AN ACT OF GIVING" else "EDIT GIVING RECORD",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = primaryText,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Seed of Abundance (+35 XP)",
                        fontSize = 11.sp,
                        color = goldColor
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Description (Required)
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) isTitleError = false
                    },
                    label = { Text("Act Description (Required)") },
                    placeholder = { Text("e.g., Donated winter coats / Helped friend") },
                    isError = isTitleError,
                    supportingText = if (isTitleError) {
                        { Text("Please enter a short description", color = Color(0xFFFF6E81)) }
                    } else null,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldColor,
                        unfocusedBorderColor = cardBorder,
                        focusedLabelColor = goldColor,
                        cursorColor = goldColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("giving_description_input")
                )

                // Optional Amount (Clearly privacy-friendly)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = amountStr,
                        onValueChange = { amountStr = it },
                        label = { Text("Amount (Optional)") },
                        placeholder = { Text("Leave blank for non-monetary") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        leadingIcon = {
                            Text(
                                text = currencySymbol,
                                fontWeight = FontWeight.Bold,
                                color = goldColor,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = goldColor,
                            unfocusedBorderColor = cardBorder,
                            focusedLabelColor = goldColor,
                            cursorColor = goldColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("giving_amount_input")
                    )
                }

                // Category Selection Chips
                Text(
                    text = "Category / Recipient Realm:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = secondaryText
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GivingLogEntity.ALL_CATEGORIES.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        val catColor = getGivingCategoryColor(cat, isDark)
                        val catIcon = getGivingCategoryIcon(cat)

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) catColor.copy(alpha = 0.25f) else containerBg,
                            border = BorderStroke(
                                width = if (isSelected) 1.2.dp else 0.6.dp,
                                color = if (isSelected) catColor else cardBorder
                            ),
                            modifier = Modifier.clickable { selectedCategory = cat }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = catIcon,
                                    contentDescription = null,
                                    tint = if (isSelected) catColor else mutedText,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = cat,
                                    fontSize = 10.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) primaryText else secondaryText
                                )
                            }
                        }
                    }
                }

                // Optional Recipient / Cause
                OutlinedTextField(
                    value = recipientName,
                    onValueChange = { recipientName = it },
                    label = { Text("Recipient / Cause Name (Optional)") },
                    placeholder = { Text("e.g. Local Shelter, John, Mentee") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldColor,
                        unfocusedBorderColor = cardBorder,
                        focusedLabelColor = goldColor,
                        cursorColor = goldColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("giving_recipient_input")
                )

                // Optional Gratitude & Abundance Reflection
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Gratitude Reflection (Optional)") },
                    placeholder = { Text("How did this giving expand your feeling of abundance?") },
                    minLines = 2,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldColor,
                        unfocusedBorderColor = cardBorder,
                        focusedLabelColor = goldColor,
                        cursorColor = goldColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("giving_note_input")
                )

                // Checkbox to inscribe into Sovereign Notebook
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { saveToNotebook = !saveToNotebook }
                ) {
                    Checkbox(
                        checked = saveToNotebook,
                        onCheckedChange = { saveToNotebook = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = goldColor,
                            checkmarkColor = RichBlack
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Inscribe in Sovereign Notebook (+15 XP)",
                        fontSize = 11.sp,
                        color = secondaryText
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        isTitleError = true
                    } else {
                        val parsedAmount = amountStr.toDoubleOrNull()
                        onSave(
                            title.trim(),
                            parsedAmount,
                            currencySymbol.trim().ifBlank { "$" },
                            selectedCategory,
                            recipientName.trim(),
                            note.trim(),
                            saveToNotebook
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("giving_dialog_save_button")
            ) {
                Text(
                    text = if (initialLog == null) "SEAL GIVING ACT" else "UPDATE RECORD",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (isDark) RichBlack else Color.White
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = mutedText, fontSize = 12.sp)
            }
        }
    )
}

/**
 * Dialog for Setting or Modifying the Giving / Tithing Goal
 */
@Composable
fun GivingGoalSettingsDialog(
    currentGoal: GivingGoalEntity?,
    onDismiss: () -> Unit,
    onSaveGoal: (
        goalType: String,
        targetAmount: Double,
        targetPercentage: Double,
        targetActsCount: Int,
        currencySymbol: String,
        serviceMotto: String
    ) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldColor = if (isDark) GoldLight else tierTheme.goldDark
    val primaryText = if (isDark) TextPrimary else LightTextPrimary
    val secondaryText = if (isDark) TextSecondary else LightTextSecondary
    val mutedText = if (isDark) TextMuted else LightTextMuted
    val containerBg = if (isDark) DarkCharcoal else LightElevated
    val cardBorder = if (isDark) DarkBorder else LightBorder

    val goal = currentGoal ?: GivingGoalEntity()
    var selectedGoalType by remember { mutableStateOf(goal.goalType) }
    var targetAmountStr by remember { mutableStateOf(if (goal.targetAmount > 0) goal.targetAmount.toInt().toString() else "250") }
    var targetPercentageStr by remember { mutableStateOf(goal.targetPercentage.toInt().toString()) }
    var targetActsCountStr by remember { mutableStateOf(goal.targetActsCount.toString()) }
    var currencySymbol by remember { mutableStateOf(goal.currencySymbol) }
    var serviceMotto by remember { mutableStateOf(goal.serviceMotto) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = containerBg,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(goldColor.copy(alpha = 0.2f))
                        .border(1.dp, goldColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null,
                        tint = goldColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = "GIVING & TITHING TARGET",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = primaryText,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Optional Intentional Circulation",
                        fontSize = 11.sp,
                        color = secondaryText
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Goal Style:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = secondaryText
                )

                // Goal Options Selection
                val goalOptions = listOf(
                    Triple(GivingGoalEntity.GOAL_TYPE_NONE, "Free Giving Mode", "Log acts freely without any fixed target or obligation."),
                    Triple(GivingGoalEntity.GOAL_TYPE_FIXED_MONTHLY, "Fixed Monthly Target", "Set a monthly dollar amount (e.g., $250/month)."),
                    Triple(GivingGoalEntity.GOAL_TYPE_PERCENTAGE_INCOME, "Tithe / Income %", "Set an abundance tithe percentage (e.g., 10% of inflows)."),
                    Triple(GivingGoalEntity.GOAL_TYPE_ACTS_COUNT_MONTHLY, "Monthly Frequency", "Aim for a specific count of giving acts per month (e.g. 4 acts).")
                )

                goalOptions.forEach { (type, label, desc) ->
                    val isSelected = selectedGoalType == type
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) goldColor.copy(alpha = if (isDark) 0.18f else 0.12f) else containerBg,
                        border = BorderStroke(
                            width = if (isSelected) 1.2.dp else 0.6.dp,
                            color = if (isSelected) goldColor else cardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedGoalType = type }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, if (isSelected) goldColor else mutedText, CircleShape)
                                    .background(if (isSelected) goldColor else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = RichBlack,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) (if (isDark) GoldLight else tierTheme.goldDark) else primaryText
                                )
                                Text(
                                    text = desc,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp,
                                    color = secondaryText
                                )
                            }
                        }
                    }
                }

                // Dynamic Input based on Selected Goal Type
                when (selectedGoalType) {
                    GivingGoalEntity.GOAL_TYPE_FIXED_MONTHLY -> {
                        OutlinedTextField(
                            value = targetAmountStr,
                            onValueChange = { targetAmountStr = it },
                            label = { Text("Monthly Dollar Target") },
                            placeholder = { Text("250") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = {
                                Text(
                                    text = currencySymbol,
                                    fontWeight = FontWeight.Bold,
                                    color = goldColor,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = goldColor,
                                unfocusedBorderColor = cardBorder,
                                focusedLabelColor = goldColor,
                                cursorColor = goldColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("giving_target_amount_input")
                        )
                    }
                    GivingGoalEntity.GOAL_TYPE_PERCENTAGE_INCOME -> {
                        OutlinedTextField(
                            value = targetPercentageStr,
                            onValueChange = { targetPercentageStr = it },
                            label = { Text("Tithe / Income Percentage (%)") },
                            placeholder = { Text("10") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            trailingIcon = {
                                Text(
                                    text = "%",
                                    fontWeight = FontWeight.Bold,
                                    color = goldColor,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = goldColor,
                                unfocusedBorderColor = cardBorder,
                                focusedLabelColor = goldColor,
                                cursorColor = goldColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("giving_target_percentage_input")
                        )
                    }
                    GivingGoalEntity.GOAL_TYPE_ACTS_COUNT_MONTHLY -> {
                        OutlinedTextField(
                            value = targetActsCountStr,
                            onValueChange = { targetActsCountStr = it },
                            label = { Text("Target Acts Per Month") },
                            placeholder = { Text("4") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = goldColor,
                                unfocusedBorderColor = cardBorder,
                                focusedLabelColor = goldColor,
                                cursorColor = goldColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("giving_target_acts_input")
                        )
                    }
                    else -> {
                        // NONE: Show positive abundance encouragement
                        Surface(
                            color = goldColor.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(0.5.dp, cardBorder)
                        ) {
                            Text(
                                text = "✨ In Free Giving mode, you can log acts at any pace. Every act expands the habit of doing more than paid for.",
                                fontSize = 10.5.sp,
                                color = secondaryText,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                // Generosity Intention Motto
                OutlinedTextField(
                    value = serviceMotto,
                    onValueChange = { serviceMotto = it },
                    label = { Text("Generosity Intention / Motto") },
                    placeholder = { Text("True wealth begins with the spirit of generosity.") },
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldColor,
                        unfocusedBorderColor = cardBorder,
                        focusedLabelColor = goldColor,
                        cursorColor = goldColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = targetAmountStr.toDoubleOrNull() ?: 250.0
                    val percentage = targetPercentageStr.toDoubleOrNull() ?: 10.0
                    val acts = targetActsCountStr.toIntOrNull() ?: 4
                    onSaveGoal(
                        selectedGoalType,
                        amount,
                        percentage,
                        acts,
                        currencySymbol.trim().ifBlank { "$" },
                        serviceMotto.trim()
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("save_giving_goal_button")
            ) {
                Text(
                    text = "SAVE TARGET",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (isDark) RichBlack else Color.White
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = mutedText, fontSize = 12.sp)
            }
        }
    )
}

/**
 * Individual History Card for a Giving Log
 */
@Composable
fun GivingLogItemCard(
    log: GivingLogEntity,
    isAmountsHidden: Boolean = false,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldColor = if (isDark) GoldLight else tierTheme.goldDark
    val primaryText = if (isDark) TextPrimary else LightTextPrimary
    val secondaryText = if (isDark) TextSecondary else LightTextSecondary
    val mutedText = if (isDark) TextMuted else LightTextMuted
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorder = if (isDark) DarkBorder else LightBorder

    var showMenu by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    val catIcon = getGivingCategoryIcon(log.category)
    val catColor = getGivingCategoryColor(log.category, isDark)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("giving_log_card_${log.id}"),
        color = surfaceColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.8.dp, cardBorder)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize()
        ) {
            // Top Row: Category Badge + Timestamp + Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = catColor.copy(alpha = if (isDark) 0.18f else 0.12f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.6.dp, catColor.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = catIcon,
                                contentDescription = null,
                                tint = catColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = log.category.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = catColor,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Text(
                        text = log.dateFormatted.ifBlank { "Recent" },
                        fontSize = 10.sp,
                        color = mutedText
                    )
                }

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Options",
                            tint = mutedText,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(if (isDark) SurfaceElevated else LightElevated)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Entry", fontSize = 12.sp, color = primaryText) },
                            leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null, tint = goldColor, modifier = Modifier.size(16.dp)) },
                            onClick = {
                                showMenu = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Entry", fontSize = 12.sp, color = Color(0xFFFF6E81)) },
                            leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = null, tint = Color(0xFFFF6E81), modifier = Modifier.size(16.dp)) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Row: Description and Optional Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = log.title,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryText,
                        lineHeight = 18.sp
                    )

                    if (log.recipientName.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "To: ${log.recipientName}",
                            fontSize = 11.sp,
                            color = secondaryText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Amount Pill (if logged and monetary)
                if (log.isMonetary && log.amount != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = goldColor.copy(alpha = if (isDark) 0.15f else 0.12f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(0.6.dp, goldColor.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = if (isAmountsHidden) "••••" else "${log.currencySymbol}${String.format(Locale.US, "%,.2f", log.amount)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Reflection / Note Section (if present)
            if (log.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = (if (isDark) RichBlack else Color.White).copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.5.dp, cardBorder.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FormatQuote,
                            contentDescription = null,
                            tint = goldColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = log.note,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = secondaryText,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
