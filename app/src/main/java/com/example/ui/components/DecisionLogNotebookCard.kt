package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotebookEntryEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightSurface
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Dedicated Card for Decision Log Entries in the Sovereign Notebook and Insights.
 * Highlights:
 * - Decision text with rapid conviction indicators
 * - Initial confidence rating (1-5 stars)
 * - 30-Day revisit countdown or glowing action trigger
 * - Completed reflection outcomes & result tags
 */
@Composable
fun DecisionLogNotebookCard(
    entry: NotebookEntryEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit,
    onRevisit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val cardBorderColor = if (isDark) DarkBorder else LightBorder
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated

    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }
    val formattedDate = remember(entry.timestamp) { dateFormat.format(Date(entry.timestamp)) }

    // Calculate 30-day timeline
    val thirtyDaysMillis = 30L * 24 * 60 * 60 * 1000L
    val elapsedMillis = System.currentTimeMillis() - entry.timestamp
    val daysRemaining = remember(entry.timestamp) {
        val remaining = ((thirtyDaysMillis - elapsedMillis) / (1000L * 60 * 60 * 24)).toInt()
        remaining.coerceAtLeast(0)
    }
    val isReadyForRevisit = !entry.isRevisited && (elapsedMillis >= thirtyDaysMillis)

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("decision_log_card_${entry.id}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Top Bar: Decision Log Badge, Date, Favorite & Delete Icons
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
                        color = goldAccent.copy(alpha = if (isDark) 0.2f else 0.15f),
                        border = BorderStroke(0.8.dp, goldAccent.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "👑", fontSize = 9.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "DECISION LOG",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    Text(
                        text = "• $formattedDate",
                        fontSize = 10.sp,
                        color = textMutedColor
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(28.dp).testTag("fav_decision_${entry.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favorite",
                            tint = if (entry.isFavorite) goldAccent else textMutedColor.copy(alpha = 0.4f),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp).testTag("delete_decision_${entry.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = textMutedColor.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Decision Text
            val displayText = if (entry.decisionText.isNotBlank()) entry.decisionText else entry.title
            Text(
                text = displayText,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = textColor,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            // Confidence Level Rating
            if (entry.confidenceLevel > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "Gut Confidence:",
                        fontSize = 10.5.sp,
                        color = textMutedColor
                    )
                    (1..5).forEach { star ->
                        val isLit = star <= entry.confidenceLevel
                        Icon(
                            imageVector = if (isLit) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if (isLit) goldAccent else textMutedColor.copy(alpha = 0.3f),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Text(
                        text = "(${entry.confidenceLevel}/5)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent
                    )
                }
            }

            // Rationale snippet if exists
            if (entry.decisionRationale.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDark) SurfaceElevated else LightSurface,
                    border = BorderStroke(0.6.dp, cardBorderColor)
                ) {
                    Text(
                        text = "Context: \"${entry.decisionRationale}\"",
                        fontSize = 10.5.sp,
                        fontStyle = FontStyle.Italic,
                        color = textSecColor,
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 30-Day Revisit Section:
            // Case 1: Outcome logged / Revisited
            // Case 2: Ready to Revisit (>= 30 days)
            // Case 3: Pending (< 30 days)
            if (entry.isRevisited) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) DarkCharcoal else LightElevated,
                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "30-DAY REVISITED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            // Outcome tag badge
                            if (entry.outcomeTag.isNotBlank()) {
                                val tagColor = when (entry.outcomeTag) {
                                    NotebookEntryEntity.OUTCOME_GOOD -> SuccessGreen
                                    NotebookEntryEntity.OUTCOME_MIXED -> goldAccent
                                    NotebookEntryEntity.OUTCOME_BAD -> AmberAccent
                                    else -> textMutedColor
                                }
                                Surface(
                                    color = tagColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(0.6.dp, tagColor.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = entry.outcomeTag,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = tagColor,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                                    )
                                }
                            }
                        }

                        if (entry.outcomeText.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Outcome: ${entry.outcomeText}",
                                fontSize = 11.5.sp,
                                color = textColor,
                                lineHeight = 16.sp
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Edit Revisit",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = goldAccent,
                                modifier = Modifier
                                    .clickable { onRevisit() }
                                    .padding(top = 4.dp)
                            )
                        }
                    }
                }
            } else if (isReadyForRevisit) {
                // Glow Alert Banner: Ready to revisit now
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AmberAccent.copy(alpha = if (isDark) 0.18f else 0.12f),
                    border = BorderStroke(1.dp, AmberBright.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⚡", fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "30-DAY REVISIT DUE",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) AmberBright else GoldDark,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Text(
                                text = "30 days elapsed. Reflect on outcome & claim +25 XP.",
                                fontSize = 10.sp,
                                color = textSecColor
                            )
                        }

                        Button(
                            onClick = onRevisit,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) AmberBright else GoldDark,
                                contentColor = RichBlack
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("revisit_due_button_${entry.id}")
                        ) {
                            Text(
                                text = "REVISIT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                // Pending revisit status
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) SurfaceElevated else LightSurface,
                    border = BorderStroke(0.6.dp, cardBorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.HourglassBottom,
                                contentDescription = null,
                                tint = textMutedColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Revisit in $daysRemaining days",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = textMutedColor
                            )
                        }

                        Text(
                            text = "Record Outcome Early",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = goldAccent,
                            modifier = Modifier
                                .clickable { onRevisit() }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Dashboard & Journal Shortcut Banner to open the Decision Log dialog.
 */
@Composable
fun DecisionLogShortcutBanner(
    onOpenDecisionLog: () -> Unit,
    modifier: Modifier = Modifier,
    onViewHistory: (() -> Unit)? = null
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewHistory?.invoke() ?: onOpenDecisionLog() }
            .testTag("decision_log_shortcut_banner")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(goldAccent.copy(alpha = if (isDark) 0.2f else 0.15f))
                        .border(1.dp, goldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👑", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "DECISION LOG",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = goldAccent.copy(alpha = if (isDark) 0.2f else 0.12f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "+50 XP",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "Decide promptly, change mind slowly. Log a decision under 30 seconds.",
                        fontSize = 11.sp,
                        color = textSecColor,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                if (onViewHistory != null) {
                    OutlinedButton(
                        onClick = onViewHistory,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                        border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("shortcut_view_history_button")
                    ) {
                        Text(text = "LOG", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = onOpenDecisionLog,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = goldAccent,
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("shortcut_decide_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.FlashOn, contentDescription = null, modifier = Modifier.size(13.dp))
                        Text(text = "DECIDE", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Dashboard Banner showing decisions ready to revisit at 30 days.
 */
@Composable
fun DecisionsDueToRevisitDashboardBanner(
    dueEntries: List<NotebookEntryEntity>,
    onRevisitEntry: (NotebookEntryEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (dueEntries.isEmpty()) return

    val isDark = LocalIsDarkTheme.current
    val firstDue = dueEntries.first()

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = AmberAccent.copy(alpha = if (isDark) 0.18f else 0.12f),
        border = BorderStroke(1.2.dp, AmberBright.copy(alpha = 0.8f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("decisions_due_banner")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AmberBright.copy(alpha = 0.2f))
                        .border(1.dp, AmberBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⏳", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${dueEntries.size} DECISION${if (dueEntries.size > 1) "S" else ""} READY TO REVISIT",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) AmberBright else GoldDark,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "• 30 Days", fontSize = 9.sp, color = if (isDark) TextMuted else LightTextMuted)
                    }
                    Text(
                        text = "\"${firstDue.decisionText.ifBlank { firstDue.title }.take(35)}...\"",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) TextPrimary else LightTextPrimary,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onRevisitEntry(firstDue) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) AmberBright else GoldDark,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("dashboard_revisit_button")
            ) {
                Text(text = "REVISIT (+25 XP)", fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
