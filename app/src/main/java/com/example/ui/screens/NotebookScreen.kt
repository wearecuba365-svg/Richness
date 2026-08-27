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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.components.AutoPatternDetectionSection
import com.example.ui.components.BrushedCard
import com.example.ui.components.ComebackNotebookCard
import com.example.ui.components.CommitmentNotebookCard
import com.example.ui.components.DecisionLogDialog
import com.example.ui.components.DecisionLogNotebookCard
import com.example.ui.components.DecisionLogShortcutBanner
import com.example.ui.components.FearToActionReframeDialog
import com.example.ui.components.FearToActionShortcutBanner
import com.example.ui.components.FearReframeNotebookCard
import com.example.ui.components.LocalWindowSizeInfo
import com.example.ui.components.MoneyMindsetLogDialog
import com.example.ui.components.MoneyMindsetNotebookCard
import com.example.ui.components.MoneyMindsetPatternBanner
import com.example.ui.components.PersistenceCheckDialog
import com.example.ui.components.RevisitDecisionDialog
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
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.LightIvory
import com.example.ui.viewmodel.PdfExportUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotebookScreen(
    entries: List<NotebookEntryEntity>,
    searchQuery: String,
    selectedTag: String,
    showNewNoteDialog: Boolean,
    activeNoteForEdit: NotebookEntryEntity?,
    showFearReframeDialog: Boolean = false,
    showDecisionLogDialog: Boolean = false,
    showMoneyMindsetDialog: Boolean = false,
    activeDecisionForRevisit: NotebookEntryEntity? = null,
    pdfExportState: PdfExportUiState = PdfExportUiState(),
    userProfile: UserProfileEntity? = null,
    onSearchChange: (String) -> Unit,
    onTagSelect: (String) -> Unit,
    onOpenNewNote: () -> Unit,
    onCloseDialog: () -> Unit,
    onOpenFearReframe: () -> Unit = {},
    onCloseFearReframe: () -> Unit = {},
    onSaveFearReframe: (String, String, String, String, Boolean) -> Unit = { _, _, _, _, _ -> },
    onToggleFearActionCompleted: (Long, Boolean) -> Unit = { _, _ -> },
    onOpenDecisionLog: () -> Unit = {},
    onCloseDecisionLog: () -> Unit = {},
    onSaveDecisionLog: (String, Int, Long, String) -> Unit = { _, _, _, _ -> },
    onOpenMoneyMindsetDialog: () -> Unit = {},
    onCloseMoneyMindsetDialog: () -> Unit = {},
    onSaveMoneyMindset: (String, String, String, String, String, String) -> Unit = { _, _, _, _, _, _ -> },
    onOpenRevisitDecision: (NotebookEntryEntity) -> Unit = {},
    onCloseRevisitDecision: () -> Unit = {},
    onSaveRevisitDecision: (Long, String, String) -> Unit = { _, _, _ -> },
    onSaveNote: (Int?, String, String, String, String, String, Boolean) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onToggleFavorite: (NotebookEntryEntity) -> Unit,
    onEditNote: (NotebookEntryEntity) -> Unit,
    onOpenExportDialog: () -> Unit = {},
    onDismissExportDialog: () -> Unit = {},
    onExportPdf: (List<NotebookEntryEntity>?, String) -> Unit = { _, _ -> },
    onSharePdf: () -> Unit = {},
    onViewPdf: () -> Unit = {}
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val context = LocalContext.current
    val windowInfo = LocalWindowSizeInfo.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    val tags = listOf("All", "Patterns", "Money Mindset", "Favorites", "Decisions", "Comebacks", "Fear Reframes", "Vault Prompts", "Mindset", "Ritual", "Worksheet")

    val filteredEntries = entries.filter { entry ->
        val matchesSearch = searchQuery.isBlank() ||
                entry.title.contains(searchQuery, ignoreCase = true) ||
                entry.content.contains(searchQuery, ignoreCase = true) ||
                entry.tags.contains(searchQuery, ignoreCase = true) ||
                entry.fearText.contains(searchQuery, ignoreCase = true) ||
                entry.worstCaseText.contains(searchQuery, ignoreCase = true) ||
                entry.actionTodayText.contains(searchQuery, ignoreCase = true) ||
                entry.decisionText.contains(searchQuery, ignoreCase = true) ||
                entry.outcomeText.contains(searchQuery, ignoreCase = true) ||
                entry.outcomeTag.contains(searchQuery, ignoreCase = true) ||
                entry.comebackObstacle.contains(searchQuery, ignoreCase = true) ||
                entry.comebackPlan.contains(searchQuery, ignoreCase = true) ||
                entry.comebackStreakType.contains(searchQuery, ignoreCase = true)

        val matchesTag = when (selectedTag) {
            "All" -> true
            "Patterns" -> true
            "Money Mindset" -> entry.entryType == NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET || entry.tags.contains("Money Mindset", ignoreCase = true)
            "Favorites" -> entry.isFavorite
            "Decisions" -> entry.entryType == NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG || entry.tags.contains("Decision Log", ignoreCase = true)
            "Comebacks" -> entry.entryType == NotebookEntryEntity.ENTRY_TYPE_COMEBACK || entry.tags.contains("Comeback", ignoreCase = true) || entry.tags.contains("Persistence Check", ignoreCase = true)
            "Fear Reframes" -> entry.entryType == NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME || entry.tags.contains("Fear Reframe", ignoreCase = true)
            "Vault Prompts" -> entry.moduleId != null
            else -> entry.tags.contains(selectedTag, ignoreCase = true)
        }

        matchesSearch && matchesTag
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("notebook_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // --- HEADER WITH EXPORT PDF, FEAR REFRAME & INSCRIBE BUTTONS ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SOVEREIGN NOTEBOOK",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = tierTheme.goldPrimary.copy(alpha = if (isDark) 0.15f else 0.1f),
                                shape = RoundedCornerShape(4.dp),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, if (isDark) GoldLight.copy(alpha = 0.4f) else tierTheme.goldDark.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "ROOM OFFLINE CACHE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldAccent,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "${entries.size} Inscribed Reflections • 100% Offline Ready",
                            fontSize = 12.sp,
                            color = textSecColor
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // MONEY MINDSET QUICK BUTTON
                        OutlinedButton(
                            onClick = onOpenMoneyMindsetDialog,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isDark) AmberAccent else tierTheme.goldDark
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) AmberAccent.copy(alpha = 0.7f) else tierTheme.goldDark.copy(alpha = 0.7f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("money_mindset_header_button")
                        ) {
                            Text(text = "🪙", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "MOMENT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // DECISION LOG QUICK BUTTON
                        OutlinedButton(
                            onClick = onOpenDecisionLog,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = goldAccent
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, goldAccent.copy(alpha = 0.7f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("decision_log_header_button")
                        ) {
                            Text(text = "👑", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "DECIDE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // FEAR TO ACTION REFRAME QUICK BUTTON
                        OutlinedButton(
                            onClick = onOpenFearReframe,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isDark) AmberBright else GoldDark
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) AmberAccent.copy(alpha = 0.6f) else GoldDark.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("fear_reframe_header_button")
                        ) {
                            Text(text = "⚡", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "FACE A FEAR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // EXPORT PDF BUTTON
                        OutlinedButton(
                            onClick = onOpenExportDialog,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = goldAccent
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) GoldPrimary else tierTheme.goldDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("export_pdf_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PictureAsPdf,
                                contentDescription = "Export PDF",
                                modifier = Modifier.size(14.dp),
                                tint = goldAccent
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "PDF",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // INSCRIBE BUTTON
                        Button(
                            onClick = onOpenNewNote,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                                contentColor = if (isDark) RichBlack else Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("inscribe_button")
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "INSCRIBE", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // --- AUTO-PATTERN DETECTION SECTION (INSIGHTS & JOURNAL SCAN) ---
            if (selectedTag == "All" || selectedTag == "Patterns") {
                item {
                    AutoPatternDetectionSection(
                        entries = entries,
                        onOpenNewNote = onOpenNewNote,
                        onFilterByKeyword = { keyword ->
                            onSearchChange(keyword)
                        },
                        onSelectEntry = { entry ->
                            onEditNote(entry)
                        }
                    )
                }
            }

            // --- SHORTCUT BANNERS (MONEY MINDSET, DECISION LOG & FEAR REFRAME) ---
            if (selectedTag == "All" || selectedTag == "Money Mindset") {
                item {
                    MoneyMindsetPatternBanner(
                        entries = entries,
                        onOpenMoneyMindsetDialog = onOpenMoneyMindsetDialog
                    )
                }
            }

            if (selectedTag == "All" || selectedTag == "Decisions") {
                item {
                    DecisionLogShortcutBanner(
                        onOpenDecisionLog = onOpenDecisionLog
                    )
                }
            }

            if (selectedTag == "All" || selectedTag == "Fear Reframes") {
                item {
                    FearToActionShortcutBanner(
                        onOpenReframe = onOpenFearReframe
                    )
                }
            }

            // --- SEARCH BAR ---
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search reflections, prompts, tags...", color = textMutedColor, fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = goldAccent)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear", tint = textMutedColor)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("notebook_search_bar"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldAccent,
                        unfocusedBorderColor = cardBorderColor,
                        focusedContainerColor = surfaceColor,
                        unfocusedContainerColor = surfaceColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            // --- FILTER TAGS ROW ---
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(tags) { tag ->
                        val isSelected = selectedTag == tag
                        Surface(
                            color = if (isSelected) (if (isDark) AmberAccent else tierTheme.goldDark) else surfaceColor,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) (if (isDark) AmberBright else tierTheme.goldPrimary) else cardBorderColor
                            ),
                            modifier = Modifier.clickable { onTagSelect(tag) }
                        ) {
                            Text(
                                text = tag,
                                color = if (isSelected) (if (isDark) RichBlack else Color.White) else textSecColor,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // --- NOTEBOOK ENTRIES LIST ---
            if (filteredEntries.isEmpty()) {
                item {
                    BrushedCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EditNote,
                                contentDescription = null,
                                tint = if (isDark) GoldPrimary else tierTheme.goldDark,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (searchQuery.isNotBlank()) "No matching reflections found." else "The Sovereign Notebook is empty.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Inscribe your Definite Purpose, reflections from the 13 Vaults, or nocturnal affirmations.",
                                fontSize = 12.sp,
                                color = textMutedColor,
                                textAlign = TextAlign.Center
                            )
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
                                when (entry.entryType) {
                                    NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG -> {
                                        DecisionLogNotebookCard(
                                            entry = entry,
                                            onEdit = { onEditNote(entry) },
                                            onDelete = { onDeleteNote(entry.id) },
                                            onToggleFavorite = { onToggleFavorite(entry) },
                                            onRevisit = { onOpenRevisitDecision(entry) }
                                        )
                                    }
                                    NotebookEntryEntity.ENTRY_TYPE_COMEBACK -> {
                                        ComebackNotebookCard(
                                            entry = entry,
                                            onEdit = { onEditNote(entry) },
                                            onDelete = { onDeleteNote(entry.id) },
                                            onToggleFavorite = { onToggleFavorite(entry) }
                                        )
                                    }
                                    NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET -> {
                                        MoneyMindsetNotebookCard(
                                            entry = entry,
                                            onEdit = { onEditNote(entry) },
                                            onDelete = { onDeleteNote(entry.id) },
                                            onToggleFavorite = { onToggleFavorite(entry) }
                                        )
                                    }
                                    NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME -> {
                                        FearReframeNotebookCard(
                                            entry = entry,
                                            onEdit = { onEditNote(entry) },
                                            onDelete = { onDeleteNote(entry.id) },
                                            onToggleFavorite = { onToggleFavorite(entry) },
                                            onToggleActionCompleted = { isDone -> onToggleFearActionCompleted(entry.id, isDone) }
                                        )
                                    }
                                    NotebookEntryEntity.ENTRY_TYPE_COMMITMENT_CONTRACT -> {
                                        CommitmentNotebookCard(
                                            entry = entry,
                                            onEdit = { onEditNote(entry) },
                                            onDelete = { onDeleteNote(entry.id) },
                                            onToggleFavorite = { onToggleFavorite(entry) }
                                        )
                                    }
                                    else -> {
                                        NotebookEntryCard(
                                            entry = entry,
                                            onEdit = { onEditNote(entry) },
                                            onDelete = { onDeleteNote(entry.id) },
                                            onToggleFavorite = { onToggleFavorite(entry) }
                                        )
                                    }
                                }
                            }
                        }
                        if (rowEntries.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            } else {
                items(filteredEntries, key = { it.id }) { entry ->
                    when (entry.entryType) {
                        NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG -> {
                            DecisionLogNotebookCard(
                                entry = entry,
                                onEdit = { onEditNote(entry) },
                                onDelete = { onDeleteNote(entry.id) },
                                onToggleFavorite = { onToggleFavorite(entry) },
                                onRevisit = { onOpenRevisitDecision(entry) }
                            )
                        }
                        NotebookEntryEntity.ENTRY_TYPE_COMEBACK -> {
                            ComebackNotebookCard(
                                entry = entry,
                                onEdit = { onEditNote(entry) },
                                onDelete = { onDeleteNote(entry.id) },
                                onToggleFavorite = { onToggleFavorite(entry) }
                            )
                        }
                        NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET -> {
                            MoneyMindsetNotebookCard(
                                entry = entry,
                                onEdit = { onEditNote(entry) },
                                onDelete = { onDeleteNote(entry.id) },
                                onToggleFavorite = { onToggleFavorite(entry) }
                            )
                        }
                        NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME -> {
                            FearReframeNotebookCard(
                                entry = entry,
                                onEdit = { onEditNote(entry) },
                                onDelete = { onDeleteNote(entry.id) },
                                onToggleFavorite = { onToggleFavorite(entry) },
                                onToggleActionCompleted = { isDone -> onToggleFearActionCompleted(entry.id, isDone) }
                            )
                        }
                        NotebookEntryEntity.ENTRY_TYPE_COMMITMENT_CONTRACT -> {
                            CommitmentNotebookCard(
                                entry = entry,
                                onEdit = { onEditNote(entry) },
                                onDelete = { onDeleteNote(entry.id) },
                                onToggleFavorite = { onToggleFavorite(entry) }
                            )
                        }
                        else -> {
                            NotebookEntryCard(
                                entry = entry,
                                onEdit = { onEditNote(entry) },
                                onDelete = { onDeleteNote(entry.id) },
                                onToggleFavorite = { onToggleFavorite(entry) }
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }

        // Money Mindset Quick-Entry Dialog
        if (showMoneyMindsetDialog) {
            MoneyMindsetLogDialog(
                onDismiss = onCloseMoneyMindsetDialog,
                onSave = onSaveMoneyMindset
            )
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

        // Fear-to-Action Guided 3-Step Reframe Dialog
        if (showFearReframeDialog) {
            FearToActionReframeDialog(
                pastEntries = entries,
                onDismiss = onCloseFearReframe,
                onSaveReframe = onSaveFearReframe,
                onToggleActionCompleted = onToggleFearActionCompleted,
                onDeleteEntry = onDeleteNote
            )
        }

        // New / Edit Note Dialog
        if (showNewNoteDialog) {
            NotebookEditorDialog(
                initialNote = activeNoteForEdit,
                onDismiss = onCloseDialog,
                onSave = onSaveNote
            )
        }

        // Stylized Typography PDF Export Dialog
        if (pdfExportState.showExportDialog) {
            NotebookPdfExportDialog(
                entries = entries,
                filteredEntries = filteredEntries,
                userProfile = userProfile,
                pdfExportState = pdfExportState,
                onDismiss = onDismissExportDialog,
                onExport = { selectedEntries, title -> onExportPdf(selectedEntries, title) },
                onShare = onSharePdf,
                onView = onViewPdf
            )
        }
    }
}

@Composable
private fun NotebookEntryCard(
    entry: NotebookEntryEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) SurfaceElevated else LightElevated

    val dateStr = remember(entry.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
        sdf.format(Date(entry.timestamp))
    }

    BrushedCard(
        modifier = Modifier.testTag("notebook_card_${entry.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = tierTheme.goldPrimary.copy(alpha = if (isDark) 0.25f else 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = entry.moduleTitle,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = dateStr,
                    fontSize = 10.sp,
                    color = textMutedColor
                )
            }

            Row {
                IconButton(onClick = onToggleFavorite, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = if (entry.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (entry.isFavorite) (if (isDark) AmberBright else tierTheme.goldDark) else textMutedColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = textSecColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = textMutedColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = entry.title,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = textColor
        )

        if (entry.promptQuestion.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Prompt: ${entry.promptQuestion}",
                fontSize = 11.sp,
                color = if (isDark) GoldPrimary else tierTheme.goldDark,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = entry.content,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = textSecColor
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            color = surfaceColor,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = entry.tags,
                fontSize = 10.sp,
                color = textMutedColor,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun NotebookEditorDialog(
    initialNote: NotebookEntryEntity?,
    onDismiss: () -> Unit,
    onSave: (Int?, String, String, String, String, String, Boolean) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    var title by remember { mutableStateOf(initialNote?.title ?: "") }
    var content by remember { mutableStateOf(initialNote?.content ?: "") }
    var tags by remember { mutableStateOf(initialNote?.tags ?: "Mindset, Ritual") }
    var isFavorite by remember { mutableStateOf(initialNote?.isFavorite ?: false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth()
                .border(1.5.dp, if (isDark) GoldLight else tierTheme.goldDark, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialNote != null) "EDIT REFLECTION" else "INSCRIBE NEW REFLECTION",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 1.sp
                    )

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = textMutedColor)
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Reflection Title...", color = textMutedColor, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldAccent,
                        unfocusedBorderColor = cardBorderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Inscribe your unyielding thoughts, realizations, strategic plans, or autosuggestion decrees...", color = textMutedColor, fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .testTag("note_content_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldAccent,
                        unfocusedBorderColor = cardBorderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    placeholder = { Text("Tags (comma separated)...", color = textMutedColor, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldAccent,
                        unfocusedBorderColor = cardBorderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isFavorite = !isFavorite }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if (isFavorite) (if (isDark) AmberBright else tierTheme.goldDark) else textMutedColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mark as Favorite",
                            fontSize = 11.sp,
                            color = if (isFavorite) (if (isDark) AmberBright else tierTheme.goldDark) else if (isDark) TextSecondary else LightTextSecondary
                        )
                    }

                    Button(
                        onClick = {
                            if (title.isNotBlank() || content.isNotBlank()) {
                                onSave(
                                    initialNote?.moduleId,
                                    initialNote?.moduleTitle ?: "Freeform Reflection",
                                    if (title.isBlank()) "Sovereign Inscription" else title,
                                    content,
                                    initialNote?.promptQuestion ?: "",
                                    tags,
                                    isFavorite
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                            contentColor = if (isDark) RichBlack else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("save_note_button")
                    ) {
                        Text(text = "SAVE (+75 XP)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotebookPdfExportDialog(
    entries: List<NotebookEntryEntity>,
    filteredEntries: List<NotebookEntryEntity>,
    userProfile: UserProfileEntity?,
    pdfExportState: PdfExportUiState,
    onDismiss: () -> Unit,
    onExport: (List<NotebookEntryEntity>?, String) -> Unit,
    onShare: () -> Unit,
    onView: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    var documentTitle by remember { mutableStateOf("THE SOVEREIGN RITUAL NOTEBOOK") }
    var exportScope by remember { mutableStateOf("ALL") } // ALL, FILTERED, FAVORITES

    val targetEntries = when (exportScope) {
        "FILTERED" -> filteredEntries
        "FAVORITES" -> entries.filter { it.isFavorite }
        else -> entries
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth()
                .border(1.5.dp, if (isDark) GoldLight else tierTheme.goldDark, RoundedCornerShape(20.dp))
                .testTag("pdf_export_dialog"),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(tierTheme.goldPrimary.copy(alpha = if (isDark) 0.25f else 0.18f))
                                .border(1.dp, goldAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PictureAsPdf,
                                contentDescription = null,
                                tint = goldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "PDF MANUSCRIPT EXPORT",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Typography-Focused Permanent Archive",
                                fontSize = 10.sp,
                                color = textMutedColor
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = textMutedColor)
                    }
                }

                // Document Metadata Card
                Surface(
                    color = if (isDark) RichBlack.copy(alpha = 0.6f) else LightIvory,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "ARCHIVIST",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textMutedColor,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = userProfile?.name ?: "Sovereign Member",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    color = textColor
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "TIER & XP",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textMutedColor,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${userProfile?.tierName ?: "Novice"} (${userProfile?.xpTotal ?: 0} XP)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Stylized typography format with A4 proportions, Napoleon Hill serif typeface, Roman numeral inscription cards, prompt quotations, and archival verification stamp.",
                            fontSize = 10.sp,
                            color = textSecColor,
                            lineHeight = 14.sp
                        )
                    }
                }

                // Scope Selector Options
                Text(
                    text = "SELECT EXPORT SCOPE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent,
                    letterSpacing = 0.8.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    // Option 1: All Entries
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { exportScope = "ALL" }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = exportScope == "ALL",
                            onClick = { exportScope = "ALL" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = goldAccent,
                                unselectedColor = textMutedColor
                            )
                        )
                        Text(
                            text = "All Inscribed Entries (${entries.size})",
                            fontSize = 12.sp,
                            fontWeight = if (exportScope == "ALL") FontWeight.Bold else FontWeight.Normal,
                            color = textColor
                        )
                    }

                    // Option 2: Filtered (if active)
                    if (filteredEntries.size != entries.size) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { exportScope = "FILTERED" }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = exportScope == "FILTERED",
                                onClick = { exportScope = "FILTERED" },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = goldAccent,
                                    unselectedColor = textMutedColor
                                )
                            )
                            Text(
                                text = "Current Filtered View (${filteredEntries.size})",
                                fontSize = 12.sp,
                                fontWeight = if (exportScope == "FILTERED") FontWeight.Bold else FontWeight.Normal,
                                color = textColor
                            )
                        }
                    }

                    // Option 3: Favorites
                    val favCount = entries.count { it.isFavorite }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { exportScope = "FAVORITES" }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = exportScope == "FAVORITES",
                            onClick = { exportScope = "FAVORITES" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = goldAccent,
                                unselectedColor = textMutedColor
                            )
                        )
                        Text(
                            text = "Starred Favorites Only ($favCount)",
                            fontSize = 12.sp,
                            fontWeight = if (exportScope == "FAVORITES") FontWeight.Bold else FontWeight.Normal,
                            color = textColor
                        )
                    }
                }

                // Document Title Input
                OutlinedTextField(
                    value = documentTitle,
                    onValueChange = { documentTitle = it },
                    label = { Text("Manuscript Title", fontSize = 11.sp, color = textMutedColor) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pdf_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldAccent,
                        unfocusedBorderColor = cardBorderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Error Display (if any)
                if (pdfExportState.errorMessage != null) {
                    Surface(
                        color = Color(0xFF4A1A1A),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Filled.Error, contentDescription = null, tint = Color(0xFFFF8080), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = pdfExportState.errorMessage, fontSize = 11.sp, color = Color(0xFFFF8080))
                        }
                    }
                }

                // Success Result Actions (if PDF is already generated)
                if (pdfExportState.exportResult != null) {
                    Surface(
                        color = tierTheme.goldPrimary.copy(alpha = if (isDark) 0.2f else 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, goldAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = goldAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Manuscript Ready (${pdfExportState.exportResult.pageCount} Pages • ${pdfExportState.exportResult.totalEntries} Entries)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = onView,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                                        contentColor = if (isDark) RichBlack else Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("view_pdf_button")
                                ) {
                                    Icon(imageVector = Icons.Filled.Visibility, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "VIEW PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = onShare,
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, goldAccent),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("share_pdf_button")
                                ) {
                                    Icon(imageVector = Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "SHARE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Primary Generate Button
                Button(
                    onClick = {
                        onExport(targetEntries, documentTitle.ifBlank { "THE SOVEREIGN RITUAL NOTEBOOK" })
                    },
                    enabled = !pdfExportState.isExporting && targetEntries.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                        contentColor = if (isDark) RichBlack else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("generate_pdf_button")
                ) {
                    if (pdfExportState.isExporting) {
                        CircularProgressIndicator(
                            color = if (isDark) RichBlack else Color.White,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "TYPESETTING MANUSCRIPT...", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "GENERATE STYLIZED PDF (${targetEntries.size} ENTRIES)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
