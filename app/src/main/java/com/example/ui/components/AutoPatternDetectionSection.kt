package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.NotebookEntryEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
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
import com.example.util.AutoPatternDetector
import com.example.util.DetectedPattern
import com.example.util.PatternCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Auto-Pattern Detection Section for the Insights & Sovereign Notebook area.
 * Automatically scans user's existing entries across Journal, Reflections, and Money Mindset
 * and surfaces gentle, supportive observations about recurring themes, words, and emotional tones.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AutoPatternDetectionSection(
    entries: List<NotebookEntryEntity>,
    onOpenNewNote: () -> Unit,
    onFilterByKeyword: (String) -> Unit,
    onSelectEntry: ((NotebookEntryEntity) -> Unit)? = null,
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

    // Automatically recalculate patterns when entries change (no manual refresh needed)
    val detectedPatterns by remember(entries) {
        derivedStateOf {
            AutoPatternDetector.detectPatterns(entries, rollingDays = 30)
        }
    }

    var selectedPatternForModal by remember { mutableStateOf<DetectedPattern?>(null) }
    var isExpanded by remember { mutableStateOf(true) }

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("auto_pattern_detection_section")
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
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = if (isDark) RichBlack else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AUTO-PATTERN DETECTION",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = (if (isDark) GoldPrimary else tierTheme.goldPrimary).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(
                                    0.5.dp,
                                    (if (isDark) GoldLight else tierTheme.goldDark).copy(alpha = 0.4f)
                                )
                            ) {
                                Text(
                                    text = "30-DAY ROLLING",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldAccent,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = if (detectedPatterns.isNotEmpty()) {
                                "${detectedPatterns.size} Recurring Theme${if (detectedPatterns.size > 1) "s" else ""} & Emotional Tone${if (detectedPatterns.size > 1) "s" else ""} Observed"
                            } else {
                                "Mindset Pattern Recognition"
                            },
                            fontSize = 11.sp,
                            color = textSecColor
                        )
                    }
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(32.dp).testTag("toggle_pattern_detection_expanded")
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = goldAccent
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Supportive intro note
                    Surface(
                        color = (if (isDark) SurfaceElevated else LightElevated).copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(0.6.dp, cardBorderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "💡", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Lightweight keyword & theme observations across your Journal, Reflections, and Money Mindset logs.",
                                fontSize = 11.sp,
                                color = textSecColor,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    if (detectedPatterns.isEmpty()) {
                        // Empty State when user has few entries or no clear patterns yet
                        PatternEmptyStateCard(
                            onInscribeClick = onOpenNewNote,
                            isDark = isDark,
                            goldAccent = goldAccent,
                            textColor = textColor,
                            textMutedColor = textMutedColor,
                            surfaceColor = surfaceColor,
                            cardBorderColor = cardBorderColor
                        )
                    } else {
                        // List of Detected Pattern Cards
                        detectedPatterns.forEach { pattern ->
                            PatternCard(
                                pattern = pattern,
                                onViewEntries = {
                                    selectedPatternForModal = pattern
                                },
                                onJumpToFilter = {
                                    onFilterByKeyword(pattern.themeOrKeyword)
                                },
                                isDark = isDark,
                                tierGoldDark = tierTheme.goldDark,
                                goldAccent = goldAccent,
                                textColor = textColor,
                                textSecColor = textSecColor,
                                textMutedColor = textMutedColor,
                                surfaceColor = surfaceColor,
                                cardBorderColor = cardBorderColor
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal to inspect matching entries for a detected pattern
    selectedPatternForModal?.let { pattern ->
        val matchingEntries = remember(pattern, entries) {
            AutoPatternDetector.getMatchingEntries(entries, pattern)
        }

        PatternEntriesModal(
            pattern = pattern,
            matchingEntries = matchingEntries,
            onDismiss = { selectedPatternForModal = null },
            onSelectEntry = { entry ->
                selectedPatternForModal = null
                if (onSelectEntry != null) {
                    onSelectEntry(entry)
                } else {
                    onFilterByKeyword(pattern.themeOrKeyword)
                }
            },
            onFilterInNotebook = {
                selectedPatternForModal = null
                onFilterByKeyword(pattern.themeOrKeyword)
            }
        )
    }
}

/**
 * Individual Pattern Card displaying plain-language observation, count, and jump-to-entries link.
 */
@Composable
private fun PatternCard(
    pattern: DetectedPattern,
    onViewEntries: () -> Unit,
    onJumpToFilter: () -> Unit,
    isDark: Boolean,
    tierGoldDark: Color,
    goldAccent: Color,
    textColor: Color,
    textSecColor: Color,
    textMutedColor: Color,
    surfaceColor: Color,
    cardBorderColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isDark) SurfaceElevated else LightElevated,
        border = BorderStroke(0.8.dp, cardBorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pattern_card_${pattern.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Category Tag & Frequency Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = pattern.emoji, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = pattern.category.displayName.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 0.8.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = (if (isDark) GoldPrimary else tierGoldDark).copy(alpha = 0.15f),
                    border = BorderStroke(0.5.dp, (if (isDark) GoldPrimary else tierGoldDark).copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "${pattern.occurrencesCount} ${if (pattern.occurrencesCount == 1) "Entry" else "Entries"}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Plain-Language Observation Headline
            Text(
                text = pattern.plainLanguageObservation,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                lineHeight = 18.sp
            )

            // Gentle supportive reflection prompt
            Text(
                text = pattern.gentleReflectionPrompt,
                fontSize = 11.sp,
                color = textSecColor,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Action Row: "View matching entries" link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pattern.timePeriodLabel,
                    fontSize = 9.sp,
                    color = textMutedColor
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onViewEntries() }
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View matching entries (${pattern.occurrencesCount})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) AmberBright else tierGoldDark
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = if (isDark) AmberBright else tierGoldDark,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

/**
 * Empty state card shown when user has few entries and no patterns are detected yet.
 */
@Composable
private fun PatternEmptyStateCard(
    onInscribeClick: () -> Unit,
    isDark: Boolean,
    goldAccent: Color,
    textColor: Color,
    textMutedColor: Color,
    surfaceColor: Color,
    cardBorderColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isDark) SurfaceElevated else LightElevated,
        border = BorderStroke(0.8.dp, cardBorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pattern_empty_state_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Insights,
                contentDescription = null,
                tint = goldAccent,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = "Keep journaling — patterns will appear here once you have more entries.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Text(
                text = "As you inscribe reflections, money moments, and decisions, subtle recurring themes and emotional tones will be gently surfaced here automatically over a rolling 30-day window.",
                fontSize = 11.sp,
                color = textMutedColor,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedButton(
                onClick = onInscribeClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                border = BorderStroke(1.dp, goldAccent),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("empty_pattern_inscribe_button")
            ) {
                Icon(imageVector = Icons.Filled.EditNote, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "INSCRIBE A REFLECTION", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Drilldown Modal Dialog showing all entries matching the selected pattern.
 */
@Composable
fun PatternEntriesModal(
    pattern: DetectedPattern,
    matchingEntries: List<NotebookEntryEntity>,
    onDismiss: () -> Unit,
    onSelectEntry: (NotebookEntryEntity) -> Unit,
    onFilterInNotebook: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.US) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("pattern_entries_modal"),
            shape = RoundedCornerShape(18.dp),
            color = if (isDark) RichBlack else Color.White,
            border = BorderStroke(1.dp, if (isDark) GoldPrimary.copy(alpha = 0.5f) else tierTheme.goldDark.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
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
                        Text(text = pattern.emoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = pattern.title.uppercase(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "${matchingEntries.size} Recorded Entries • ${pattern.timePeriodLabel}",
                                fontSize = 11.sp,
                                color = textSecColor
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_pattern_entries_modal")
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = textMutedColor)
                    }
                }

                // Observation Summary Callout
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) SurfaceElevated else LightElevated,
                    border = BorderStroke(0.6.dp, cardBorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = pattern.plainLanguageObservation,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                        Text(
                            text = pattern.gentleReflectionPrompt,
                            fontSize = 11.sp,
                            color = textSecColor,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                Text(
                    text = "ENTRIES WHERE THIS THEME APPEARED:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent,
                    letterSpacing = 1.sp
                )

                // List of Entries
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(matchingEntries, key = { it.id }) { entry ->
                        MatchingEntryCard(
                            entry = entry,
                            highlightKeyword = pattern.themeOrKeyword,
                            dateFormat = dateFormat,
                            onSelect = { onSelectEntry(entry) },
                            isDark = isDark,
                            goldAccent = goldAccent,
                            textColor = textColor,
                            textSecColor = textSecColor,
                            textMutedColor = textMutedColor,
                            surfaceColor = surfaceColor,
                            cardBorderColor = cardBorderColor
                        )
                    }
                }

                // Bottom Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onFilterInNotebook,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                        border = BorderStroke(1.dp, goldAccent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Insights, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "FILTER IN NOTEBOOK", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) GoldPrimary else tierTheme.goldPrimary,
                            contentColor = if (isDark) RichBlack else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "DONE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Card representing a single entry matched in the pattern modal.
 */
@Composable
private fun MatchingEntryCard(
    entry: NotebookEntryEntity,
    highlightKeyword: String,
    dateFormat: SimpleDateFormat,
    onSelect: () -> Unit,
    isDark: Boolean,
    goldAccent: Color,
    textColor: Color,
    textSecColor: Color,
    textMutedColor: Color,
    surfaceColor: Color,
    cardBorderColor: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = surfaceColor,
        border = BorderStroke(0.6.dp, cardBorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("matching_entry_card_${entry.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    val entryBadgeLabel = when (entry.entryType) {
                        NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET -> "🪙 Money Mindset"
                        NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG -> "👑 Decision Log"
                        NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME -> "⚡ Fear Reframe"
                        NotebookEntryEntity.ENTRY_TYPE_COMEBACK -> "🛡️ Comeback"
                        else -> "✍️ Reflection"
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = (if (isDark) GoldPrimary else goldAccent).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = entryBadgeLabel,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    if (entry.moduleTitle.isNotBlank() && entry.moduleTitle != "Freeform Reflection") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${entry.moduleTitle}",
                            fontSize = 9.sp,
                            color = textMutedColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Text(
                    text = dateFormat.format(Date(entry.timestamp)),
                    fontSize = 9.sp,
                    color = textMutedColor
                )
            }

            // Entry Title
            Text(
                text = entry.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Content excerpt preview
            val previewText = remember(entry) {
                when {
                    entry.content.isNotBlank() -> entry.content
                    entry.fearText.isNotBlank() -> "Fear: ${entry.fearText} → Action: ${entry.actionTodayText}"
                    entry.decisionText.isNotBlank() -> "Decision: ${entry.decisionText} (Rationale: ${entry.decisionRationale})"
                    entry.comebackObstacle.isNotBlank() -> "Obstacle: ${entry.comebackObstacle} → Plan: ${entry.comebackPlan}"
                    else -> "No text excerpt."
                }
            }

            Text(
                text = previewText,
                fontSize = 11.sp,
                color = textSecColor,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )

            // Tags row if present
            if (entry.tags.isNotBlank()) {
                Text(
                    text = "Tags: ${entry.tags}",
                    fontSize = 9.sp,
                    color = textMutedColor
                )
            }
        }
    }
}
