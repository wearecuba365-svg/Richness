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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.NotebookEntryEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightHighlight
import com.example.ui.theme.LightIvory
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 30-Day Revisit Dialog for Decision Logs.
 * Lets the user review their past decision, capture real-world outcomes,
 * and select a result tag ("Good", "Mixed", "Bad", "Too Early to Tell").
 * Rewards flat +25 XP for following through on reflection, independent of outcome.
 */
@Composable
fun RevisitDecisionDialog(
    entry: NotebookEntryEntity,
    onDismiss: () -> Unit,
    onSaveRevisit: (outcomeText: String, outcomeTag: String) -> Unit
) {
    val isDark = androidx.compose.material3.MaterialTheme.colorScheme.background.red < 0.5f

    val bgGradient = if (isDark) {
        Brush.verticalGradient(listOf(DarkCharcoal, RichBlack))
    } else {
        Brush.verticalGradient(listOf(LightIvory, LightSurface))
    }
    val cardBorder = if (isDark) DarkBorder else LightBorder
    val primaryText = if (isDark) TextPrimary else LightTextPrimary
    val secondaryText = if (isDark) TextSecondary else LightTextSecondary
    val mutedText = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldPrimary else GoldDark

    var outcomeText by remember { mutableStateOf(entry.outcomeText) }
    var selectedTag by remember {
        mutableStateOf(
            if (entry.outcomeTag.isNotBlank()) entry.outcomeTag else NotebookEntryEntity.OUTCOME_GOOD
        )
    }

    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }
    val daysSince = remember {
        val diff = System.currentTimeMillis() - entry.timestamp
        (diff / (1000L * 60 * 60 * 24)).toInt().coerceAtLeast(0)
    }

    val tags = listOf(
        NotebookEntryEntity.OUTCOME_GOOD to "🌟 Good Outcome",
        NotebookEntryEntity.OUTCOME_MIXED to "⚖️ Mixed / Neutral",
        NotebookEntryEntity.OUTCOME_BAD to "🔄 Pivoted / Challenging",
        NotebookEntryEntity.OUTCOME_TOO_EARLY to "⏳ Too Early to Tell"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .widthIn(max = 520.dp)
                .padding(vertical = 16.dp)
                .border(BorderStroke(1.2.dp, goldAccent.copy(alpha = 0.7f)), RoundedCornerShape(20.dp))
                .testTag("revisit_decision_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(bgGradient)
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = goldAccent.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.5f)),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.History,
                                        contentDescription = null,
                                        tint = goldAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "30-DAY REVISIT",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = primaryText,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Decision Reflection & Wisdom",
                                    fontSize = 11.sp,
                                    color = goldAccent,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp).testTag("close_revisit_dialog")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = mutedText,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Original Decision Recap Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDark) SurfaceElevated else LightElevated,
                        border = BorderStroke(0.8.dp, cardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ORIGINAL DECISION",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldAccent,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "${dateFormat.format(Date(entry.timestamp))} ($daysSince days ago)",
                                    fontSize = 10.sp,
                                    color = mutedText
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (entry.decisionText.isNotBlank()) entry.decisionText else entry.title,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = primaryText,
                                lineHeight = 19.sp
                            )
                            if (entry.confidenceLevel > 0) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Initial Confidence:",
                                        fontSize = 10.5.sp,
                                        color = mutedText
                                    )
                                    (1..entry.confidenceLevel).forEach {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = null,
                                            tint = goldAccent,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                    Text(
                                        text = "${entry.confidenceLevel}/5",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = goldAccent
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section: Select Result Tag
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "HOW WOULD YOU CHARACTERIZE THE OUTCOME?",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            tags.forEach { (tagKey, tagLabel) ->
                                val isSelected = selectedTag == tagKey
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) goldAccent.copy(alpha = 0.18f) else if (isDark) SurfaceElevated else LightElevated,
                                    border = BorderStroke(1.dp, if (isSelected) goldAccent else cardBorder),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedTag = tagKey }
                                        .testTag("tag_option_$tagKey")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = tagLabel,
                                            fontSize = 12.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) primaryText else secondaryText
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Filled.CheckCircle,
                                                contentDescription = null,
                                                tint = goldAccent,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section: Free Text Outcome
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "OUTCOME & WISDOM (WHAT HAPPENED?)",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = outcomeText,
                            onValueChange = { outcomeText = it },
                            placeholder = {
                                Text(
                                    text = "Reflect freely: What unfolded? Did moving quickly pay off? What did you learn?",
                                    fontSize = 12.sp,
                                    color = mutedText.copy(alpha = 0.7f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .testTag("outcome_input_field"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = goldAccent,
                                unfocusedBorderColor = cardBorder,
                                focusedTextColor = primaryText,
                                unfocusedTextColor = primaryText,
                                focusedContainerColor = if (isDark) SurfaceElevated else LightIvory,
                                unfocusedContainerColor = if (isDark) SurfaceElevated else LightIvory
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Reflection Principle Reminder
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = if (isDark) SurfaceElevated else LightElevated,
                        border = BorderStroke(0.6.dp, cardBorder)
                    ) {
                        Text(
                            text = "💡 Outcome logging is purely reflective. Following through on your 30-day review awards flat XP regardless of whether the result was good, mixed, or bad.",
                            fontSize = 10.5.sp,
                            color = mutedText,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Bottom Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, cardBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = mutedText)
                        ) {
                            Text("Cancel", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = {
                                if (outcomeText.isNotBlank()) {
                                    onSaveRevisit(outcomeText.trim(), selectedTag)
                                }
                            },
                            enabled = outcomeText.isNotBlank(),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(46.dp)
                                .testTag("save_revisit_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = goldAccent,
                                contentColor = RichBlack,
                                disabledContainerColor = if (isDark) SurfaceHighlight else LightHighlight,
                                disabledContentColor = mutedText
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "SEAL REVISIT (+25 XP)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
