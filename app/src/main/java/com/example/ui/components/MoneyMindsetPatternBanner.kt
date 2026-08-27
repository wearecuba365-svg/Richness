package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoneyMindsetPatternBanner(
    entries: List<NotebookEntryEntity>,
    onOpenMoneyMindsetDialog: () -> Unit,
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

    val moneyEntries = remember(entries) {
        entries.filter {
            it.entryType == NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET ||
                    it.tags.contains("Money Mindset", ignoreCase = true)
        }
    }

    // Emotion frequency calculation
    val emotionCounts = remember(moneyEntries) {
        val map = mutableMapOf<String, Int>()
        DEFAULT_MONEY_EMOTIONS.forEach { emotionOpt ->
            val count = moneyEntries.count { it.tags.contains(emotionOpt.label, ignoreCase = true) || it.content.contains("Emotion: ${emotionOpt.label}", ignoreCase = true) }
            if (count > 0) {
                map[emotionOpt.label] = count
            }
        }
        map.toList().sortedByDescending { it.second }
    }

    val mostFrequentEmotion = emotionCounts.firstOrNull()

    // Decision types calculation
    val savingsCount = moneyEntries.count { it.tags.contains("Saving", ignoreCase = true) }
    val expensesCount = moneyEntries.count { it.tags.contains("Expense", ignoreCase = true) }
    val incomeCount = moneyEntries.count { it.tags.contains("Income", ignoreCase = true) }
    val investmentCount = moneyEntries.count { it.tags.contains("Investment", ignoreCase = true) }

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("money_mindset_pattern_banner")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
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
                        Icon(
                            imageVector = Icons.Filled.Insights,
                            contentDescription = null,
                            tint = if (isDark) RichBlack else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "FINANCIAL MINDSET PATTERNS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${moneyEntries.size} Recorded Decisions & Emotional Triggers",
                            fontSize = 10.sp,
                            color = textSecColor
                        )
                    }
                }

                Button(
                    onClick = onOpenMoneyMindsetDialog,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                        contentColor = if (isDark) RichBlack else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp).testTag("banner_log_money_moment_button")
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Log Moment", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Recurring Theme Callout Note
            if (mostFrequentEmotion != null) {
                Surface(
                    color = (if (isDark) GoldPrimary else tierTheme.goldDark).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.8.dp, (if (isDark) AmberBright else tierTheme.goldDark).copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💡", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Recurring Theme: You've logged '${mostFrequentEmotion.first}' ${mostFrequentEmotion.second} time${if (mostFrequentEmotion.second > 1) "s" else ""}. Tracking these patterns builds conscious sovereign wealth.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor
                        )
                    }
                }
            }

            // Emotion Frequency Chips
            if (emotionCounts.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "EMOTIONAL TRIGGERS & STATES",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = textMutedColor,
                        letterSpacing = 0.5.sp
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        emotionCounts.forEach { (emotion, count) ->
                            val emoji = DEFAULT_MONEY_EMOTIONS.firstOrNull { it.label.equals(emotion, ignoreCase = true) }?.emoji ?: "✨"
                            Surface(
                                color = surfaceColor,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(0.8.dp, cardBorderColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = emoji, fontSize = 10.sp)
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "$emotion: $count",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isDark) GoldLight else tierTheme.goldDark
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Decision Type Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    Triple("Savings", savingsCount, Color(0xFF4CAF50)),
                    Triple("Expenses", expensesCount, if (isDark) AmberAccent else tierTheme.goldDark),
                    Triple("Income", incomeCount, Color(0xFFFFB300)),
                    Triple("Investment", investmentCount, Color(0xFF00BCD4))
                ).forEach { (label, count, color) ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = surfaceColor,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(0.5.dp, cardBorderColor)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = count.toString(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                            Text(
                                text = label,
                                fontSize = 9.sp,
                                color = textSecColor
                            )
                        }
                    }
                }
            }
        }
    }
}
