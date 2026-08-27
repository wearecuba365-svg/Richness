package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.WealthGoalEntity
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
import java.util.Locale

@Composable
fun LogContributionDialog(
    goal: WealthGoalEntity?,
    initialTab: Int = 0,
    onDismiss: () -> Unit,
    onLog: (
        amount: Double,
        isMilestoneOnly: Boolean,
        title: String,
        note: String,
        saveToNotebook: Boolean
    ) -> Unit
) {
    val currencySymbol = goal?.currencySymbol ?: "$"
    var selectedTab by remember { mutableIntStateOf(initialTab) } // 0: Cash Contribution, 1: Milestone Note

    // Contribution state
    var amountText by remember { mutableStateOf("") }
    var contributionTitle by remember { mutableStateOf("Capital Inflow") }
    var contributionNote by remember { mutableStateOf("") }

    // Milestone state
    var milestoneTitle by remember { mutableStateOf("") }
    var milestoneNote by remember { mutableStateOf("") }

    var saveToNotebook by remember { mutableStateOf(true) }

    val presetAmounts = listOf(100.0, 500.0, 1000.0, 2500.0, 5000.0)
    val milestoneQuickIdeas = listOf(
        "Hit 25% Goal Threshold",
        "Hit 50% Halfway Mark",
        "Opened Sovereign Reserve Account",
        "Secured Major Consulting Contract",
        "Eliminated High-Interest Debt",
        "First Transmutation Inflow"
    )

    val isContributionValid = (amountText.toDoubleOrNull() ?: 0.0) > 0 && contributionTitle.isNotBlank()
    val isMilestoneValid = milestoneTitle.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .border(1.5.dp, Brush.linearGradient(listOf(GoldLight, GoldDark, AmberBright)), RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .testTag("log_contribution_dialog"),
        containerColor = RichBlack,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(AmberBright, GoldDark))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Filled.MonetizationOn else Icons.Filled.Star,
                            contentDescription = null,
                            tint = RichBlack,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (selectedTab == 0) "RECORD WEALTH INFLOW" else "INSCRIBE MILESTONE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = goal?.title ?: "Definite Wealth Target",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp).testTag("close_log_contribution_dialog")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Tab Selection
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkCharcoal,
                    contentColor = GoldPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = GoldPrimary,
                            height = 2.dp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Cash Inflow", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        },
                        selectedContentColor = GoldLight,
                        unselectedContentColor = TextMuted
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Flag, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Milestone", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        },
                        selectedContentColor = AmberBright,
                        unselectedContentColor = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTab == 0) {
                    // --- CASH INFLOW TAB ---
                    Text(
                        text = "CONTRIBUTION AMOUNT ($currencySymbol)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                        leadingIcon = { Text(currencySymbol, fontWeight = FontWeight.Bold, color = AmberBright, fontSize = 16.sp) },
                        placeholder = { Text("0.00", color = TextMuted, fontSize = 16.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("contribution_amount_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkCharcoal.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkCharcoal.copy(alpha = 0.3f),
                            cursorColor = AmberBright
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick Preset Amount Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        presetAmounts.forEach { preset ->
                            Surface(
                                color = DarkCharcoal.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        val currentVal = amountText.toDoubleOrNull() ?: 0.0
                                        amountText = String.format(Locale.US, "%.0f", currentVal + preset)
                                    }
                                    .testTag("preset_amount_${preset.toInt()}")
                            ) {
                                Text(
                                    text = "+$currencySymbol${preset.toInt()}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = GoldLight,
                                    modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .align(Alignment.CenterVertically),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "SOURCE / DESCRIPTION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = contributionTitle,
                        onValueChange = { contributionTitle = it },
                        placeholder = { Text("e.g. Salary Allocation, Consulting Retainer, Business Dividend", color = TextMuted, fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("contribution_title_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkCharcoal.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkCharcoal.copy(alpha = 0.3f),
                            cursorColor = AmberBright
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "NOTES / TRANSMUTATION REFLECTION (OPTIONAL)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = contributionNote,
                        onValueChange = { contributionNote = it },
                        placeholder = { Text("What service or disciplined effort produced this accumulation?", color = TextMuted, fontSize = 12.sp) },
                        minLines = 2,
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth().testTag("contribution_note_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkCharcoal.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkCharcoal.copy(alpha = 0.3f),
                            cursorColor = AmberBright
                        )
                    )
                } else {
                    // --- NON-MONETARY MILESTONE TAB ---
                    Text(
                        text = "MILESTONE TITLE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberBright,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = milestoneTitle,
                        onValueChange = { milestoneTitle = it },
                        placeholder = { Text("e.g. Hit 50% Goal, Passed Series A, Secured Commercial Lease", color = TextMuted, fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("milestone_title_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberBright,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkCharcoal.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkCharcoal.copy(alpha = 0.3f),
                            cursorColor = AmberBright
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Or choose a quick milestone:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldLight
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    milestoneQuickIdeas.take(4).forEach { idea ->
                        Surface(
                            color = if (milestoneTitle == idea) GoldDark.copy(alpha = 0.4f) else DarkCharcoal.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (milestoneTitle == idea) GoldPrimary else DarkBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clickable { milestoneTitle = idea }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = AmberBright, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = idea, fontSize = 11.sp, color = TextPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "MILESTONE REFLECTION & LESSON",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = milestoneNote,
                        onValueChange = { milestoneNote = it },
                        placeholder = { Text("What principle or sovereign action led to this breakthrough?", color = TextMuted, fontSize = 12.sp) },
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth().testTag("milestone_note_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberBright,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkCharcoal.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkCharcoal.copy(alpha = 0.3f),
                            cursorColor = AmberBright
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Checkbox: Record in Sovereign Notebook
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { saveToNotebook = !saveToNotebook }
                        .background(SurfaceElevated.copy(alpha = 0.4f))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = saveToNotebook,
                        onCheckedChange = { saveToNotebook = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = GoldPrimary,
                            uncheckedColor = TextMuted,
                            checkmarkColor = RichBlack
                        ),
                        modifier = Modifier.size(24.dp).testTag("save_to_notebook_checkbox")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Bookmark, contentDescription = null, tint = GoldLight, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Inscribe in Sovereign Notebook", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Text("Adds entry to your persistent reflection journal & PDF export", fontSize = 9.sp, color = TextMuted)
                    }
                }
            }
        },
        confirmButton = {
            val isEnabled = if (selectedTab == 0) isContributionValid else isMilestoneValid
            Button(
                onClick = {
                    if (selectedTab == 0) {
                        val amount = amountText.toDoubleOrNull() ?: 0.0
                        onLog(amount, false, contributionTitle.trim(), contributionNote.trim(), saveToNotebook)
                    } else {
                        onLog(0.0, true, milestoneTitle.trim(), milestoneNote.trim(), saveToNotebook)
                    }
                },
                enabled = isEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = RichBlack,
                    disabledContainerColor = DarkCharcoal,
                    disabledContentColor = TextMuted
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_log_wealth_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (selectedTab == 0) "Inscribe Inflow (+35 XP)" else "Inscribe Milestone (+35 XP)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Text(text = "Cancel", fontSize = 12.sp)
            }
        }
    )
}
