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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotebookEntryEntity
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Calendar

@Composable
fun MoneyMindsetDashboardWidget(
    notebookEntries: List<NotebookEntryEntity>,
    onLogMoneyMoment: () -> Unit,
    onReviewMindsetLog: () -> Unit,
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

    val dayOfYear = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
    val promptIndex = dayOfYear % DAILY_MONEY_PROMPTS.size
    val dailyPrompt = DAILY_MONEY_PROMPTS[promptIndex]

    // Find entries tagged with Money Mindset
    val moneyEntries = remember(notebookEntries) {
        notebookEntries.filter {
            it.entryType == NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET ||
                    it.tags.contains("Money Mindset", ignoreCase = true)
        }
    }

    // Check if user logged one today (within last 24h)
    val now = System.currentTimeMillis()
    val todayEntry = moneyEntries.firstOrNull { now - it.timestamp < 24 * 60 * 60 * 1000L }

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("money_mindset_dashboard_widget")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
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
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        if (isDark) GoldDark else tierTheme.goldDark,
                                        if (isDark) GoldPrimary else tierTheme.goldPrimary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🪙", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "MONEY MINDSET JOURNAL",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = (if (isDark) AmberAccent else tierTheme.goldDark).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, goldAccent.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "+50 XP",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) AmberBright else tierTheme.goldDark,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = if (moneyEntries.isNotEmpty()) "${moneyEntries.size} Patterns Inscribed" else "Daily Consciousness Practice",
                            fontSize = 11.sp,
                            color = textSecColor
                        )
                    }
                }

                if (moneyEntries.isNotEmpty()) {
                    OutlinedButton(
                        onClick = onReviewMindsetLog,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                        border = BorderStroke(1.dp, if (isDark) GoldPrimary else tierTheme.goldDark),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp).testTag("review_money_mindset_button")
                    ) {
                        Icon(imageVector = Icons.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(12.dp), tint = goldAccent)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Review", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Daily Prompt Box
            Surface(
                color = surfaceColor,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, cardBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Lightbulb,
                                contentDescription = null,
                                tint = if (isDark) AmberBright else tierTheme.goldDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DAILY INQUIRY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 0.5.sp
                            )
                        }

                        if (todayEntry != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Logged Today",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "\"$dailyPrompt\"",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        color = textColor,
                        lineHeight = 17.sp
                    )

                    if (todayEntry != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = (if (isDark) GoldDark else tierTheme.goldDark).copy(alpha = if (isDark) 0.3f else 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(0.5.dp, goldAccent.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Latest: ${todayEntry.title}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDark) GoldLight else tierTheme.goldDark,
                                    maxLines = 1
                                )
                                Text(
                                    text = "✨ Inscribed",
                                    fontSize = 9.sp,
                                    color = textMutedColor
                                )
                            }
                        }
                    }
                }
            }

            // Quick-Action Button ("Log a Money Moment")
            Button(
                onClick = onLogMoneyMoment,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("log_money_moment_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (todayEntry == null) (if (isDark) AmberAccent else tierTheme.goldDark) else (if (isDark) GoldDark else tierTheme.goldDark.copy(alpha = 0.8f)),
                    contentColor = if (todayEntry == null) (if (isDark) RichBlack else Color.White) else (if (isDark) GoldLight else Color.White)
                )
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (todayEntry == null) "LOG A MONEY MOMENT (+50 XP)" else "LOG ANOTHER FINANCIAL DECISION (+50 XP)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
