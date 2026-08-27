package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.components.BrushedCard
import com.example.ui.components.DecisionLogDialog
import com.example.ui.components.DecisionLogNotebookCard
import com.example.ui.components.DecisionsDueToRevisitDashboardBanner
import com.example.ui.components.LocalWindowSizeInfo
import com.example.ui.components.RevisitDecisionDialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
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
import java.util.Locale

/**
 * Dedicated Decision Log Screen:
 * - Lists all decisions with status (Pending 30-day review vs. Resolved)
 * - Shows original confidence level (1-5) and 30-day reflection outcomes
 * - Filter by status (All / Pending 30-Day Review / Resolved)
 * - Quick 30-second decision logging (+50 XP) & 30-day revisit (+25 XP)
 * - Neutral & non-judgmental framing on developing decisiveness and reflection habits
 */
@Composable
fun DecisionLogScreen(
    notebookEntries: List<NotebookEntryEntity>,
    userProfile: UserProfileEntity?,
    showDecisionLogDialog: Boolean,
    activeDecisionForRevisit: NotebookEntryEntity?,
    onOpenDecisionLog: () -> Unit,
    onCloseDecisionLog: () -> Unit,
    onSaveDecisionLog: (decisionText: String, confidence: Int, timestamp: Long, rationale: String) -> Unit,
    onOpenRevisitDecision: (NotebookEntryEntity) -> Unit,
    onCloseRevisitDecision: () -> Unit,
    onSaveRevisitDecision: (id: Long, outcomeText: String, outcomeTag: String) -> Unit,
    onEditDecision: (NotebookEntryEntity) -> Unit,
    onDeleteDecision: (Long) -> Unit,
    onToggleFavorite: (NotebookEntryEntity) -> Unit,
    onBack: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val windowInfo = LocalWindowSizeInfo.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    var selectedStatusFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    // Filter for decision log entries only
    val allDecisionEntries = remember(notebookEntries) {
        notebookEntries.filter {
            it.entryType == NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG ||
                    it.tags.contains("Decision Log", ignoreCase = true)
        }
    }

    val thirtyDaysMillis = 30L * 24 * 60 * 60 * 1000L
    val now = System.currentTimeMillis()

    val pendingEntries = remember(allDecisionEntries) {
        allDecisionEntries.filter { !it.isRevisited }
    }

    val dueForRevisitEntries = remember(pendingEntries) {
        pendingEntries.filter { (now - it.timestamp) >= thirtyDaysMillis }
    }

    val resolvedEntries = remember(allDecisionEntries) {
        allDecisionEntries.filter { it.isRevisited }
    }

    // Filter by status & search
    val filteredEntries = remember(allDecisionEntries, selectedStatusFilter, searchQuery) {
        allDecisionEntries.filter { entry ->
            val matchesFilter = when (selectedStatusFilter) {
                "Pending 30-Day Review" -> !entry.isRevisited
                "Resolved" -> entry.isRevisited
                "Due Now (30+ Days)" -> !entry.isRevisited && (now - entry.timestamp >= thirtyDaysMillis)
                else -> true
            }

            val matchesSearch = searchQuery.isBlank() ||
                    entry.title.contains(searchQuery, ignoreCase = true) ||
                    entry.decisionText.contains(searchQuery, ignoreCase = true) ||
                    entry.decisionRationale.contains(searchQuery, ignoreCase = true) ||
                    entry.outcomeText.contains(searchQuery, ignoreCase = true) ||
                    entry.outcomeTag.contains(searchQuery, ignoreCase = true)

            matchesFilter && matchesSearch
        }
    }

    val avgConfidence = remember(allDecisionEntries) {
        if (allDecisionEntries.isEmpty()) 0.0
        else {
            val total = allDecisionEntries.sumOf { if (it.confidenceLevel > 0) it.confidenceLevel else 3 }
            total.toDouble() / allDecisionEntries.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("decision_log_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Top Header Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(surfaceColor)
                                .border(1.dp, cardBorderColor, CircleShape)
                                .testTag("decision_log_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = goldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "DECISION LOG",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = textColor,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = goldAccent.copy(alpha = if (isDark) 0.2f else 0.12f),
                                    shape = RoundedCornerShape(4.dp),
                                    border = BorderStroke(0.6.dp, goldAccent.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "30-DAY REVIEW",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = goldAccent,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Prompt Decision Protocol & Wisdom Ledger",
                                fontSize = 11.5.sp,
                                color = textSecColor
                            )
                        }
                    }

                    // Quick Log Button
                    Button(
                        onClick = onOpenDecisionLog,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = goldAccent,
                            contentColor = RichBlack
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("header_log_decision_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.FlashOn, contentDescription = null, modifier = Modifier.size(14.dp))
                            Text(text = "LOG DECISION", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Principle Card (Napoleon Hill quote & neutral framing)
            item {
                BrushedCard {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⚖️", fontSize = 22.sp, modifier = Modifier.padding(end = 10.dp))
                        Column {
                            Text(
                                text = "\"Decisive people reach decisions promptly and change them, if ever, very slowly.\"",
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic,
                                fontSize = 12.sp,
                                color = textColor,
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Napoleon Hill • Think and Grow Rich (Principle 7: Decision)",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = goldAccent
                            )
                        }
                    }
                }
            }

            // Metrics Summary Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Total Logged
                    MetricCard(
                        title = "TOTAL LOGGED",
                        value = "${allDecisionEntries.size}",
                        icon = "👑",
                        color = goldAccent,
                        modifier = Modifier.weight(1f)
                    )

                    // Pending 30-Day Review
                    MetricCard(
                        title = "PENDING REVIEW",
                        value = "${pendingEntries.size}",
                        icon = "⏳",
                        color = if (dueForRevisitEntries.isNotEmpty()) AmberBright else textMutedColor,
                        modifier = Modifier.weight(1f)
                    )

                    // Resolved
                    MetricCard(
                        title = "RESOLVED",
                        value = "${resolvedEntries.size}",
                        icon = "✓",
                        color = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )

                    // Avg Confidence
                    MetricCard(
                        title = "AVG CONVICTION",
                        value = if (allDecisionEntries.isEmpty()) "—" else String.format(Locale.US, "%.1f★", avgConfidence),
                        icon = "⚡",
                        color = goldAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 30-Day Due Banner (if any entries are ready for review)
            if (dueForRevisitEntries.isNotEmpty()) {
                item {
                    DecisionsDueToRevisitDashboardBanner(
                        dueEntries = dueForRevisitEntries,
                        onRevisitEntry = onOpenRevisitDecision
                    )
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search decisions, rationale, or hindsight outcomes...",
                            color = textMutedColor,
                            fontSize = 12.sp
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = goldAccent)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear", tint = textMutedColor)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("decision_log_search_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldAccent,
                        unfocusedBorderColor = cardBorderColor,
                        focusedContainerColor = surfaceColor,
                        unfocusedContainerColor = surfaceColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Status Filter Chips
            item {
                val filters = listOf(
                    "All" to "${allDecisionEntries.size}",
                    "Pending 30-Day Review" to "${pendingEntries.size}",
                    "Resolved" to "${resolvedEntries.size}"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filters) { (filterKey, count) ->
                        val isSelected = selectedStatusFilter == filterKey
                        Surface(
                            color = if (isSelected) goldAccent else surfaceColor,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) goldAccent else cardBorderColor
                            ),
                            modifier = Modifier
                                .clickable { selectedStatusFilter = filterKey }
                                .testTag("filter_status_$filterKey")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = filterKey,
                                    color = if (isSelected) RichBlack else textSecColor,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isSelected) RichBlack.copy(alpha = 0.2f) else goldAccent.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = count,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) RichBlack else goldAccent,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Decision Entries List
            if (filteredEntries.isEmpty()) {
                item {
                    BrushedCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp, horizontal = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "👑", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (searchQuery.isNotBlank()) "No decisions match your search."
                                else when (selectedStatusFilter) {
                                    "Pending 30-Day Review" -> "No pending 30-day reviews."
                                    "Resolved" -> "No resolved decisions yet."
                                    else -> "No decisions inscribed yet."
                                },
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Decisiveness is a master habit. Log a quick decision in under 30 seconds to start building your conviction archive.",
                                fontSize = 11.5.sp,
                                color = textMutedColor,
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onOpenDecisionLog,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = goldAccent,
                                    contentColor = RichBlack
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("empty_state_log_decision_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Text(text = "LOG YOUR FIRST DECISION (+50 XP)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            } else if (windowInfo.isTabletOrFoldable) {
                items(filteredEntries.chunked(2), key = { pair -> pair.map { it.id }.joinToString("_") }) { rowEntries ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        rowEntries.forEach { entry ->
                            Box(modifier = Modifier.weight(1f)) {
                                DecisionLogNotebookCard(
                                    entry = entry,
                                    onEdit = { onEditDecision(entry) },
                                    onDelete = { onDeleteDecision(entry.id) },
                                    onToggleFavorite = { onToggleFavorite(entry) },
                                    onRevisit = { onOpenRevisitDecision(entry) }
                                )
                            }
                        }
                        if (rowEntries.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            } else {
                items(filteredEntries, key = { it.id }) { entry ->
                    DecisionLogNotebookCard(
                        entry = entry,
                        onEdit = { onEditDecision(entry) },
                        onDelete = { onDeleteDecision(entry.id) },
                        onToggleFavorite = { onToggleFavorite(entry) },
                        onRevisit = { onOpenRevisitDecision(entry) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }

        // Decision Log Quick-Entry Dialog
        if (showDecisionLogDialog) {
            DecisionLogDialog(
                onDismiss = onCloseDecisionLog,
                onSaveDecision = onSaveDecisionLog
            )
        }

        // 30-Day Decision Revisit Dialog
        if (activeDecisionForRevisit != null) {
            RevisitDecisionDialog(
                entry = activeDecisionForRevisit,
                onDismiss = onCloseRevisitDecision,
                onSaveRevisit = { outcomeText, outcomeTag ->
                    onSaveRevisitDecision(activeDecisionForRevisit.id, outcomeText, outcomeTag)
                }
            )
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder
    val textMutedColor = if (isDark) TextMuted else LightTextMuted

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = surfaceColor,
        border = BorderStroke(0.8.dp, cardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(text = icon, fontSize = 10.sp)
                Text(
                    text = title,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = textMutedColor,
                    letterSpacing = 0.4.sp,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
