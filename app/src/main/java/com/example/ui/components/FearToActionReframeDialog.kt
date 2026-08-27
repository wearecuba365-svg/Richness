package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.example.ui.theme.LightIvory
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
 * Six Ghosts of Fear metadata based on Napoleon Hill's philosophy
 */
data class GhostOfFearCategory(
    val key: String,
    val name: String,
    val description: String,
    val icon: ImageVector
)

val SIX_GHOSTS_OF_FEAR = listOf(
    GhostOfFearCategory("poverty", "Poverty & Loss", "Fear of scarcity, financial ruin, or inadequate capital", Icons.Filled.AccountBalance),
    GhostOfFearCategory("criticism", "Criticism & Judgement", "Fear of disapproval, public scrutiny, or rejection", Icons.Filled.Forum),
    GhostOfFearCategory("health", "Ill Health & Fatigue", "Fear of physical breakdown, exhaustion, or illness", Icons.Filled.Favorite),
    GhostOfFearCategory("love", "Loss of Belonging", "Fear of alienation, abandonment, or envy", Icons.Filled.Shield),
    GhostOfFearCategory("age", "Time & Old Age", "Fear of running out of time, irrelevance, or decline", Icons.Filled.HourglassEmpty),
    GhostOfFearCategory("defeat", "Catastrophe & Defeat", "Fear of permanent failure or unknown adversity", Icons.Filled.Psychology)
)

/**
 * 3-Step Guided Modal with History View: Fear-to-Action Reframe Tool
 *
 * Guided Flow:
 * 1. Name the fear: "What are you afraid of right now?"
 * 2. Name the worst case: "What's the actual worst-case outcome if it happened?"
 * 3. Name one action: "What's one small action you can take today, regardless?"
 * 4. Summary Screen: Displays all three responses together framed encouragingly:
 *    "You named it. Here's your one step forward."
 */
@Composable
fun FearToActionReframeDialog(
    onDismiss: () -> Unit,
    onSaveReframe: (fearText: String, worstCaseText: String, actionTodayText: String, fearCategory: String, addToDailyHabits: Boolean) -> Unit,
    pastEntries: List<NotebookEntryEntity> = emptyList(),
    onToggleActionCompleted: ((Long, Boolean) -> Unit)? = null,
    onDeleteEntry: ((Long) -> Unit)? = null
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    // Tab state: 0 = Guided Flow, 1 = History View
    var selectedTab by remember { mutableIntStateOf(0) }

    var currentStep by remember { mutableIntStateOf(1) } // 1: Name Fear, 2: Worst Case, 3: Action Today, 4: Summary Screen
    var selectedGhostCategory by remember { mutableStateOf(SIX_GHOSTS_OF_FEAR.first().name) }
    var fearText by remember { mutableStateOf("") }
    var worstCaseText by remember { mutableStateOf("") }
    var actionTodayText by remember { mutableStateOf("") }
    var addToDailyHabits by remember { mutableStateOf(true) }

    // Filter past fear reframe entries
    val fearEntries = remember(pastEntries) {
        pastEntries.filter { it.entryType == NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME }
            .sortedByDescending { it.timestamp }
    }

    // Heuristic: Check if action might be too broad
    val isActionPotentiallyTooBroad = remember(actionTodayText) {
        val lower = actionTodayText.lowercase(Locale.getDefault())
        actionTodayText.length > 85 ||
                lower.contains("and then") ||
                lower.contains("everything") ||
                lower.contains("entire") ||
                lower.contains("all of") ||
                lower.contains("complete overhaul")
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .widthIn(max = 580.dp)
                .fillMaxWidth()
                .border(1.5.dp, if (isDark) GoldLight else tierTheme.goldDark, RoundedCornerShape(22.dp))
                .testTag("fear_reframe_dialog"),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top Header & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(tierTheme.goldPrimary.copy(alpha = if (isDark) 0.22f else 0.16f))
                                .border(1.dp, goldAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Psychology,
                                contentDescription = null,
                                tint = goldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "FEAR-TO-ACTION REFRAME",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Transmuting Fear into Definite Action",
                                fontSize = 10.sp,
                                color = textMutedColor
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = textMutedColor)
                    }
                }

                // Tab Switcher: [ ⚡ Guided Reframe | 📜 Fear History (count) ]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory)
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Tab 1: Guided Flow
                    Surface(
                        color = if (selectedTab == 0) (if (isDark) AmberAccent else tierTheme.goldDark) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = 0 }
                            .testTag("fear_reframe_tab_guided")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 7.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚡", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "GUIDED REFRAME",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 0) (if (isDark) RichBlack else Color.White) else textMutedColor
                            )
                        }
                    }

                    // Tab 2: History View
                    Surface(
                        color = if (selectedTab == 1) (if (isDark) AmberAccent else tierTheme.goldDark) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = 1 }
                            .testTag("fear_reframe_tab_history")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 7.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = if (selectedTab == 1) (if (isDark) RichBlack else Color.White) else textMutedColor
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "FEAR HISTORY (${fearEntries.size})",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 1) (if (isDark) RichBlack else Color.White) else textMutedColor
                            )
                        }
                    }
                }

                if (selectedTab == 0) {
                    // --- GUIDED 3-STEP + SUMMARY FLOW ---

                    // Step progress bar (1 of 3, 2 of 3, 3 of 3, or Summary)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = when (currentStep) {
                                    1 -> "STEP 1 OF 3 • NAME THE FEAR"
                                    2 -> "STEP 2 OF 3 • NAME THE WORST CASE"
                                    3 -> "STEP 3 OF 3 • NAME ONE ACTION TODAY"
                                    else -> "SUMMARY • YOUR ONE STEP FORWARD"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent
                            )
                            Text(
                                text = if (currentStep <= 3) "$currentStep of 3" else "Complete",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = textMutedColor
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { (currentStep.coerceAtMost(3)) / 3f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = if (isDark) AmberAccent else tierTheme.goldDark,
                            trackColor = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory
                        )
                    }

                    // Animated step views
                    AnimatedContent(
                        targetState = currentStep,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                    slideOutHorizontally { width -> -width } + fadeOut()
                                )
                            } else {
                                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                    slideOutHorizontally { width -> width } + fadeOut()
                                )
                            }
                        },
                        label = "FearReframeStepTransition"
                    ) { step ->
                        when (step) {
                            1 -> StepOneNameFear(
                                selectedCategory = selectedGhostCategory,
                                onSelectCategory = { selectedGhostCategory = it },
                                fearText = fearText,
                                onFearTextChange = { fearText = it },
                                isDark = isDark,
                                textColor = textColor,
                                textSecColor = textSecColor,
                                textMutedColor = textMutedColor,
                                goldAccent = goldAccent,
                                surfaceColor = surfaceColor,
                                cardBorderColor = cardBorderColor
                            )

                            2 -> StepTwoWorstCase(
                                fearText = fearText,
                                category = selectedGhostCategory,
                                worstCaseText = worstCaseText,
                                onWorstCaseChange = { worstCaseText = it },
                                isDark = isDark,
                                textColor = textColor,
                                textSecColor = textSecColor,
                                textMutedColor = textMutedColor,
                                goldAccent = goldAccent,
                                surfaceColor = surfaceColor,
                                cardBorderColor = cardBorderColor
                            )

                            3 -> StepThreeActionToday(
                                fearText = fearText,
                                worstCaseText = worstCaseText,
                                actionTodayText = actionTodayText,
                                onActionChange = { actionTodayText = it },
                                addToDailyHabits = addToDailyHabits,
                                onToggleAddToHabits = { addToDailyHabits = it },
                                isActionPotentiallyTooBroad = isActionPotentiallyTooBroad,
                                isDark = isDark,
                                textColor = textColor,
                                textSecColor = textSecColor,
                                textMutedColor = textMutedColor,
                                goldAccent = goldAccent,
                                surfaceColor = surfaceColor,
                                cardBorderColor = cardBorderColor
                            )

                            else -> StepFourSummaryScreen(
                                fearText = fearText,
                                worstCaseText = worstCaseText,
                                actionTodayText = actionTodayText,
                                category = selectedGhostCategory,
                                addToDailyHabits = addToDailyHabits,
                                isDark = isDark,
                                textColor = textColor,
                                textSecColor = textSecColor,
                                textMutedColor = textMutedColor,
                                goldAccent = goldAccent,
                                surfaceColor = surfaceColor,
                                cardBorderColor = cardBorderColor
                            )
                        }
                    }

                    // Bottom Action Buttons Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentStep > 1) {
                            OutlinedButton(
                                onClick = { currentStep -= 1 },
                                border = BorderStroke(1.dp, cardBorderColor),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("fear_reframe_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier.size(14.dp),
                                    tint = textSecColor
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "BACK", fontSize = 11.sp, color = textSecColor, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Spacer(modifier = Modifier.width(10.dp))
                        }

                        if (currentStep < 3) {
                            val canProceed = when (currentStep) {
                                1 -> fearText.trim().length >= 3
                                2 -> worstCaseText.trim().length >= 3
                                else -> false
                            }

                            Button(
                                onClick = { currentStep += 1 },
                                enabled = canProceed,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                                    contentColor = if (isDark) RichBlack else Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("fear_reframe_next_button")
                            ) {
                                Text(text = "NEXT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else if (currentStep == 3) {
                            val canProceedToSummary = actionTodayText.trim().length >= 3

                            Button(
                                onClick = { currentStep = 4 },
                                enabled = canProceedToSummary,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                                    contentColor = if (isDark) RichBlack else Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("fear_reframe_review_summary_button")
                            ) {
                                Text(text = "REVIEW SUMMARY", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Review",
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else {
                            // Final Submit from Summary Screen
                            Button(
                                onClick = {
                                    onSaveReframe(
                                        fearText.trim(),
                                        worstCaseText.trim(),
                                        actionTodayText.trim(),
                                        selectedGhostCategory,
                                        addToDailyHabits
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                                    contentColor = if (isDark) RichBlack else Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("fear_reframe_submit_button")
                            ) {
                                Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "SEAL REFRAME & SAVE (+75 XP)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    // --- TAB 2: FEAR HISTORY VIEW & GROWTH INDICATOR ---
                    FearHistoryContent(
                        entries = fearEntries,
                        onSwitchToGuided = { selectedTab = 0 },
                        onToggleActionCompleted = onToggleActionCompleted,
                        onDeleteEntry = onDeleteEntry,
                        isDark = isDark,
                        textColor = textColor,
                        textSecColor = textSecColor,
                        textMutedColor = textMutedColor,
                        goldAccent = goldAccent,
                        cardBorderColor = cardBorderColor
                    )
                }
            }
        }
    }
}

/**
 * Step 1: Name the Fear
 * Prompt: "What are you afraid of right now?"
 */
@Composable
private fun StepOneNameFear(
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    fearText: String,
    onFearTextChange: (String) -> Unit,
    isDark: Boolean,
    textColor: Color,
    textSecColor: Color,
    textMutedColor: Color,
    goldAccent: Color,
    surfaceColor: Color,
    cardBorderColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "What are you afraid of right now?",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = textColor,
            lineHeight = 22.sp
        )

        Text(
            text = "A vague fear expands indefinitely in the dark. Giving it precise language strips its irrational, looming power.",
            fontSize = 11.sp,
            color = textSecColor,
            lineHeight = 16.sp
        )

        // Ghost category selector chips
        Text(
            text = "PRIMARY GHOST OF FEAR",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = goldAccent,
            letterSpacing = 0.8.sp
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(SIX_GHOSTS_OF_FEAR) { ghost ->
                val isSelected = selectedCategory == ghost.name
                Surface(
                    color = if (isSelected) goldAccent.copy(alpha = if (isDark) 0.25f else 0.15f) else (if (isDark) RichBlack.copy(alpha = 0.4f) else LightIvory),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (isSelected) goldAccent else cardBorderColor),
                    modifier = Modifier.clickable { onSelectCategory(ghost.name) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ghost.icon,
                            contentDescription = null,
                            tint = if (isSelected) goldAccent else textMutedColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = ghost.name,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) goldAccent else textSecColor
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = fearText,
            onValueChange = onFearTextChange,
            placeholder = {
                Text(
                    text = "e.g., I am afraid of pitching my venture proposal to high-tier partners because I dread being rejected or judged as incompetent...",
                    fontSize = 12.sp,
                    color = textMutedColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .testTag("fear_text_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = goldAccent,
                unfocusedBorderColor = cardBorderColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedContainerColor = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory,
                unfocusedContainerColor = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

/**
 * Step 2: Name the Worst Case
 * Prompt: "What's the actual worst-case outcome if it happened?"
 */
@Composable
private fun StepTwoWorstCase(
    fearText: String,
    category: String,
    worstCaseText: String,
    onWorstCaseChange: (String) -> Unit,
    isDark: Boolean,
    textColor: Color,
    textSecColor: Color,
    textMutedColor: Color,
    goldAccent: Color,
    surfaceColor: Color,
    cardBorderColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "What's the actual worst-case outcome if it happened?",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = textColor,
            lineHeight = 22.sp
        )

        Text(
            text = "Focus on cold, grounded specificity. When defined in concrete terms, the worst case is almost always manageable and finite.",
            fontSize = 11.sp,
            color = textSecColor,
            lineHeight = 16.sp
        )

        // Summary of Step 1 Fear
        Surface(
            color = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(0.5.dp, cardBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(text = "🛡️", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "FEAR NAMED ($category):",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent
                    )
                    Text(
                        text = fearText,
                        fontSize = 11.sp,
                        color = textSecColor,
                        maxLines = 2
                    )
                }
            }
        }

        OutlinedTextField(
            value = worstCaseText,
            onValueChange = onWorstCaseChange,
            placeholder = {
                Text(
                    text = "e.g., They say 'no thanks'. My ego is bruised for 24 hours. But I remain physically safe, retain all assets, learn their exact objection, and refine the pitch for the next candidate...",
                    fontSize = 12.sp,
                    color = textMutedColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .testTag("worst_case_text_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = goldAccent,
                unfocusedBorderColor = cardBorderColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedContainerColor = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory,
                unfocusedContainerColor = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

/**
 * Step 3: Name One Action
 * Prompt: "What's one small action you can take today, regardless?"
 */
@Composable
private fun StepThreeActionToday(
    fearText: String,
    worstCaseText: String,
    actionTodayText: String,
    onActionChange: (String) -> Unit,
    addToDailyHabits: Boolean,
    onToggleAddToHabits: (Boolean) -> Unit,
    isActionPotentiallyTooBroad: Boolean,
    isDark: Boolean,
    textColor: Color,
    textSecColor: Color,
    textMutedColor: Color,
    goldAccent: Color,
    surfaceColor: Color,
    cardBorderColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "What's one small action you can take today, regardless?",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = textColor,
            lineHeight = 22.sp
        )

        Text(
            text = "Action is the sovereign antidote to fear. Keep it small, concrete, and immediately doable within 15 minutes today.",
            fontSize = 11.sp,
            color = textSecColor,
            lineHeight = 16.sp
        )

        // Action Input Field
        OutlinedTextField(
            value = actionTodayText,
            onValueChange = onActionChange,
            placeholder = {
                Text(
                    text = "e.g., Draft a 3-sentence email inquiry and send it to just 1 trusted advisor today...",
                    fontSize = 12.sp,
                    color = textMutedColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .testTag("action_today_text_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = goldAccent,
                unfocusedBorderColor = cardBorderColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedContainerColor = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory,
                unfocusedContainerColor = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Gentle Micro-Action Nudge if user typed a massive project
        AnimatedVisibility(visible = isActionPotentiallyTooBroad) {
            Surface(
                color = AmberAccent.copy(alpha = if (isDark) 0.15f else 0.1f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, AmberAccent.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Royal Guidance: Make sure this is a 15-minute micro-action you can execute today, rather than a multi-week project.",
                        fontSize = 10.sp,
                        color = if (isDark) AmberBright else GoldDark,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Follow-through tracking checkbox
        Surface(
            color = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(0.5.dp, cardBorderColor),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleAddToHabits(!addToDailyHabits) }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = addToDailyHabits,
                    onCheckedChange = onToggleAddToHabits,
                    colors = CheckboxDefaults.colors(
                        checkedColor = goldAccent,
                        uncheckedColor = textMutedColor,
                        checkmarkColor = if (isDark) RichBlack else Color.White
                    ),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Add to Today's Sovereign Ritual Tasks",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = "Enables active daily check-in and follow-through tracking",
                        fontSize = 9.sp,
                        color = textMutedColor
                    )
                }
            }
        }
    }
}

/**
 * Step 4: Summary Screen
 * Encouraging framing: "You named it. Here's your one step forward."
 */
@Composable
private fun StepFourSummaryScreen(
    fearText: String,
    worstCaseText: String,
    actionTodayText: String,
    category: String,
    addToDailyHabits: Boolean,
    isDark: Boolean,
    textColor: Color,
    textSecColor: Color,
    textMutedColor: Color,
    goldAccent: Color,
    surfaceColor: Color,
    cardBorderColor: Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.testTag("fear_reframe_summary_screen")
    ) {
        Text(
            text = "You named it. Here's your one step forward.",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = if (isDark) AmberBright else GoldDark,
            lineHeight = 22.sp
        )

        Text(
            text = "By bringing fear into the clear light and deconstructing the worst case, you strip away its irrational grip. Here is your synthesized blueprint:",
            fontSize = 11.sp,
            color = textSecColor,
            lineHeight = 16.sp
        )

        // Three-part synthesized card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isDark) RichBlack.copy(alpha = 0.55f) else LightIvory)
                .border(1.dp, goldAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Fear Named
            Row(verticalAlignment = Alignment.Top) {
                Text(text = "🛡️", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "FEAR NAMED ($category)",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = fearText,
                        fontSize = 12.sp,
                        color = textColor,
                        lineHeight = 16.sp
                    )
                }
            }

            // 2. Worst Case
            Row(verticalAlignment = Alignment.Top) {
                Text(text = "⚖️", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "WORST CASE DECONSTRUCTED",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = textMutedColor,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = worstCaseText,
                        fontSize = 12.sp,
                        color = textSecColor,
                        lineHeight = 16.sp
                    )
                }
            }

            // 3. Action Today
            Surface(
                color = goldAccent.copy(alpha = if (isDark) 0.15f else 0.1f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(0.8.dp, goldAccent.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚡", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "YOUR ONE ACTION TODAY",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = actionTodayText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Surface(
            color = if (isDark) RichBlack.copy(alpha = 0.35f) else LightElevated,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "✨", fontSize = 11.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Saving records this entry to your Journal, links it with Insights, and awards +75 XP.",
                    fontSize = 10.sp,
                    color = textMutedColor
                )
            }
        }
    }
}

/**
 * Fear History & Growth Indicator View
 */
@Composable
private fun FearHistoryContent(
    entries: List<NotebookEntryEntity>,
    onSwitchToGuided: () -> Unit,
    onToggleActionCompleted: ((Long, Boolean) -> Unit)?,
    onDeleteEntry: ((Long) -> Unit)?,
    isDark: Boolean,
    textColor: Color,
    textSecColor: Color,
    textMutedColor: Color,
    goldAccent: Color,
    cardBorderColor: Color
) {
    val tierTheme = LocalTierGoldTheme.current

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.testTag("fear_history_view")
    ) {
        // Growth Indicator Counter Banner
        Surface(
            color = if (isDark) RichBlack.copy(alpha = 0.6f) else LightIvory,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AmberAccent.copy(alpha = if (isDark) 0.2f else 0.12f))
                            .border(1.dp, goldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🛡️", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (entries.isNotEmpty()) "You've worked through ${entries.size} fear${if (entries.size == 1) "" else "s"}" else "Growth Indicator: 0 fears faced",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = "Each fear dismantled compounds your sovereign courage.",
                            fontSize = 10.sp,
                            color = textMutedColor
                        )
                    }
                }

                Button(
                    onClick = onSwitchToGuided,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                        contentColor = if (isDark) RichBlack else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("fear_history_face_new_fear_button")
                ) {
                    Text(text = "FACE A FEAR", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (entries.isEmpty()) {
            Surface(
                color = if (isDark) RichBlack.copy(alpha = 0.3f) else LightElevated,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(0.5.dp, cardBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🕊️", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No Fears Logged Yet",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Whenever hesitation strikes, step through the 3-step walkthrough to turn fear into one concrete action.",
                        fontSize = 11.sp,
                        color = textMutedColor,
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onSwitchToGuided,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                            contentColor = if (isDark) RichBlack else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Start 3-Step Walkthrough", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries, key = { it.id }) { entry ->
                    val dateStr = remember(entry.timestamp) {
                        val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
                        sdf.format(Date(entry.timestamp))
                    }

                    Surface(
                        color = if (isDark) RichBlack.copy(alpha = 0.5f) else LightIvory,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(0.5.dp, cardBorderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = goldAccent.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = entry.fearCategory.ifBlank { "Fear Reframe" },
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = goldAccent,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = dateStr, fontSize = 9.sp, color = textMutedColor)
                                }

                                if (onDeleteEntry != null) {
                                    IconButton(
                                        onClick = { onDeleteEntry(entry.id) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Delete",
                                            tint = textMutedColor,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }

                            // Fear text
                            Text(
                                text = "🛡️ ${entry.fearText.ifBlank { entry.title }}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textColor,
                                lineHeight = 15.sp
                            )

                            if (entry.worstCaseText.isNotBlank()) {
                                Text(
                                    text = "⚖️ Worst Case: ${entry.worstCaseText}",
                                    fontSize = 10.sp,
                                    color = textSecColor,
                                    lineHeight = 14.sp
                                )
                            }

                            // Action Today row
                            val action = entry.actionTodayText.ifBlank { "Take micro-action today" }
                            Surface(
                                color = if (entry.isActionCompleted) SuccessGreen.copy(alpha = 0.15f) else goldAccent.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(0.5.dp, if (entry.isActionCompleted) SuccessGreen.copy(alpha = 0.5f) else goldAccent.copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onToggleActionCompleted?.invoke(entry.id, !entry.isActionCompleted)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = if (entry.isActionCompleted) Icons.Filled.TaskAlt else Icons.Outlined.RadioButtonUnchecked,
                                            contentDescription = null,
                                            tint = if (entry.isActionCompleted) SuccessGreen else goldAccent,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = action,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (entry.isActionCompleted) textSecColor else textColor
                                        )
                                    }

                                    Text(
                                        text = if (entry.isActionCompleted) "DONE ✓" else "PENDING",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (entry.isActionCompleted) SuccessGreen else goldAccent
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dedicated Card for displaying Fear-to-Action Reframe entries in Notebook / Insights
 */
@Composable
fun FearReframeNotebookCard(
    entry: NotebookEntryEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleActionCompleted: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) SurfaceElevated else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    val dateStr = remember(entry.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
        sdf.format(Date(entry.timestamp))
    }

    BrushedCard(
        modifier = modifier.testTag("fear_reframe_card_${entry.id}")
    ) {
        // Top Badges & Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = AmberAccent.copy(alpha = if (isDark) 0.22f else 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.8.dp, if (isDark) AmberBright else GoldDark)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⚡", fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "FEAR REFRAME",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) AmberBright else GoldDark
                        )
                    }
                }

                if (entry.fearCategory.isNotBlank()) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = tierTheme.goldPrimary.copy(alpha = if (isDark) 0.15f else 0.1f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = entry.fearCategory,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = goldAccent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
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
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "View Details",
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

        // Title
        Text(
            text = entry.title,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = textColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Structured Reframe 3-Part View
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isDark) RichBlack.copy(alpha = 0.45f) else LightIvory)
                .border(0.5.dp, cardBorderColor, RoundedCornerShape(10.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Part 1: Fear
            Row(verticalAlignment = Alignment.Top) {
                Text(text = "🛡️", fontSize = 11.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "FEAR NAMED",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = entry.fearText.ifBlank { entry.content.substringBefore("\n\n").removePrefix("🛡️ GHOST OF FEAR:\n") },
                        fontSize = 11.sp,
                        color = textColor,
                        lineHeight = 16.sp
                    )
                }
            }

            // Part 2: Worst Case
            if (entry.worstCaseText.isNotBlank()) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(text = "⚖️", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "WORST CASE DECONSTRUCTED",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = textMutedColor,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = entry.worstCaseText,
                            fontSize = 11.sp,
                            color = textSecColor,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Part 3: Action Today with Toggle
            val actionText = entry.actionTodayText.ifBlank { "Execute concrete action step" }
            Surface(
                color = if (entry.isActionCompleted) SuccessGreen.copy(alpha = 0.15f) else goldAccent.copy(alpha = if (isDark) 0.12f else 0.08f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(0.8.dp, if (entry.isActionCompleted) SuccessGreen.copy(alpha = 0.6f) else goldAccent.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleActionCompleted(!entry.isActionCompleted) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = if (entry.isActionCompleted) Icons.Filled.TaskAlt else Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = "Action Status",
                            tint = if (entry.isActionCompleted) SuccessGreen else goldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = if (entry.isActionCompleted) "ACTION SEALED (FEAR TRANSMUTED)" else "TODAY'S ANTIDOTE ACTION",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (entry.isActionCompleted) SuccessGreen else goldAccent,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = actionText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (entry.isActionCompleted) textSecColor else textColor
                            )
                        }
                    }

                    Surface(
                        color = if (entry.isActionCompleted) SuccessGreen.copy(alpha = 0.2f) else goldAccent.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (entry.isActionCompleted) "DONE ✓" else "PENDING",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (entry.isActionCompleted) SuccessGreen else goldAccent,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Quick shortcut banner to launch the Fear-to-Action Reframe tool
 */
@Composable
fun FearToActionShortcutBanner(
    onOpenReframe: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenReframe() }
            .testTag("fear_to_action_shortcut_banner")
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
                        .background(AmberAccent.copy(alpha = if (isDark) 0.2f else 0.15f))
                        .border(1.dp, if (isDark) AmberBright else GoldDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⚡", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "FACE A FEAR: TRANSMUTE TO ACTION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = AmberAccent.copy(alpha = if (isDark) 0.2f else 0.12f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "+75 XP",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) AmberBright else GoldDark,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "1. Name fear → 2. Name worst case → 3. Name 1 action today.",
                        fontSize = 11.sp,
                        color = textSecColor,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onOpenReframe,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) AmberAccent else tierTheme.goldDark,
                    contentColor = if (isDark) RichBlack else Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("face_a_fear_quick_button")
            ) {
                Text(text = "FACE A FEAR", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
