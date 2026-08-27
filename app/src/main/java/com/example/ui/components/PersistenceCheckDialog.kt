package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldMetallic
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

/**
 * Persistence Streak Recovery Dialog ("Persistence Check").
 * Rooted in Napoleon Hill's principle:
 * "Defeat is a temporary condition. Successful people are defined by how they come back."
 *
 * Reframes a broken streak into a deliberate comeback moment.
 * Two short fields:
 * 1. "What broke the streak?" (free text, optional/skippable)
 * 2. "What's the plan for tomorrow?" (free text: small concrete commitment)
 *
 * User can dismiss/skip without penalty.
 */
@Composable
fun PersistenceCheckDialog(
    initialStreakType: String = "Daily Sovereign Ritual",
    onDismiss: () -> Unit,
    onLogComeback: (streakType: String, obstacle: String, tomorrowPlan: String) -> Unit,
    onExplorePersistenceTitans: (() -> Unit)? = null
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

    val availableStreakTypes = listOf(
        "Daily Sovereign Ritual",
        "Think & Grow Rich Habits",
        "Daily Affirmation",
        "Vision Contemplation",
        "Journal Ritual"
    )

    var selectedStreakType by remember {
        mutableStateOf(
            if (initialStreakType in availableStreakTypes) initialStreakType
            else availableStreakTypes.first()
        )
    }

    var obstacleText by remember { mutableStateOf("") }
    var tomorrowPlanText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .widthIn(max = 520.dp)
                .padding(vertical = 16.dp)
                .border(
                    BorderStroke(1.2.dp, goldAccent.copy(alpha = 0.7f)),
                    RoundedCornerShape(20.dp)
                )
                .testTag("persistence_check_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(bgGradient)
                    .padding(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // --- HEADER ---
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
                                                AmberAccent.copy(alpha = 0.35f),
                                                GoldDark.copy(alpha = 0.2f)
                                            )
                                        )
                                    )
                                    .border(1.dp, AmberBright.copy(alpha = 0.6f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Loop,
                                    contentDescription = "Comeback Loop",
                                    tint = if (isDark) AmberBright else GoldDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "PERSISTENCE CHECK",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.6.sp,
                                    color = goldAccent
                                )
                                Text(
                                    text = "Streak Recovery Protocol",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isDark) SurfaceHighlight else LightHighlight)
                                .testTag("persistence_check_close_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = mutedText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // --- PRINCIPLE PHILOSOPHY CALLOUT ---
                    Surface(
                        color = if (isDark) DarkCharcoal.copy(alpha = 0.85f) else LightElevated,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, cardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = AmberBright,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "HILL'S LAW OF PERSISTENCE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = if (isDark) AmberAccent else GoldDark
                                )
                            }
                            Text(
                                text = "“Defeat is merely a temporary condition. The mastery of riches belongs not to those who never miss, but to those who immediately initiate their comeback.”",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                color = secondaryText,
                                lineHeight = 17.sp
                            )

                            if (onExplorePersistenceTitans != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onExplorePersistenceTitans() }
                                        .padding(top = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "Read Thomas Edison & Abraham Lincoln Case Study →",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) AmberBright else GoldDark
                                    )
                                }
                            }
                        }
                    }

                    // --- STREAK TYPE SELECTOR ---
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "WHICH STREAK ARE YOU RECOVERING?",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = goldAccent
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            availableStreakTypes.forEach { type ->
                                val isSelected = selectedStreakType == type
                                val chipBorder = if (isSelected) {
                                    if (isDark) AmberBright else GoldDark
                                } else cardBorder

                                val chipBg = if (isSelected) {
                                    if (isDark) AmberAccent.copy(alpha = 0.22f) else GoldChampagne.copy(alpha = 0.4f)
                                } else {
                                    if (isDark) SurfaceElevated else LightElevated
                                }

                                Surface(
                                    color = chipBg,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, chipBorder),
                                    modifier = Modifier.clickable { selectedStreakType = type }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Filled.AutoAwesome,
                                                contentDescription = null,
                                                tint = if (isDark) AmberBright else GoldDark,
                                                modifier = Modifier.size(11.dp)
                                            )
                                        }
                                        Text(
                                            text = type,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) {
                                                if (isDark) AmberBright else GoldDark
                                            } else secondaryText
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // --- FIELD 1: WHAT BROKE THE STREAK (OPTIONAL) ---
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "1. WHAT BROKE THE STREAK?",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = primaryText
                            )
                            Text(
                                text = "Optional / Skippable",
                                fontSize = 10.sp,
                                color = mutedText,
                                fontStyle = FontStyle.Italic
                            )
                        }

                        OutlinedTextField(
                            value = obstacleText,
                            onValueChange = { obstacleText = it },
                            placeholder = {
                                Text(
                                    text = "e.g., Unexpected schedule disruption, forgot during travel, low energy...",
                                    fontSize = 12.sp,
                                    color = mutedText
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("comeback_obstacle_input"),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2,
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = goldAccent,
                                unfocusedBorderColor = cardBorder,
                                focusedContainerColor = if (isDark) SurfaceElevated else LightSurface,
                                unfocusedContainerColor = if (isDark) SurfaceElevated else LightSurface,
                                focusedTextColor = primaryText,
                                unfocusedTextColor = primaryText
                            )
                        )
                    }

                    // --- FIELD 2: WHAT'S THE PLAN FOR TOMORROW ---
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "2. WHAT'S THE PLAN FOR TOMORROW?",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = primaryText
                            )
                            Text(
                                text = "Concrete Micro-Commitment",
                                fontSize = 10.sp,
                                color = if (isDark) AmberAccent else GoldDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedTextField(
                            value = tomorrowPlanText,
                            onValueChange = { tomorrowPlanText = it },
                            placeholder = {
                                Text(
                                    text = "e.g., Set an 8:00 AM alarm, do 5 minutes before leaving bed, stack with morning coffee...",
                                    fontSize = 12.sp,
                                    color = mutedText
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("comeback_plan_input"),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2,
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = goldAccent,
                                unfocusedBorderColor = cardBorder,
                                focusedContainerColor = if (isDark) SurfaceElevated else LightSurface,
                                unfocusedContainerColor = if (isDark) SurfaceElevated else LightSurface,
                                focusedTextColor = primaryText,
                                unfocusedTextColor = primaryText
                            )
                        )
                    }

                    // --- ACTION BUTTONS ---
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                onLogComeback(
                                    selectedStreakType,
                                    obstacleText.trim(),
                                    tomorrowPlanText.trim()
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) AmberAccent else GoldPrimary,
                                contentColor = if (isDark) RichBlack else Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("log_comeback_submit_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "LOG COMEBACK (+35 XP)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("skip_comeback_button")
                        ) {
                            Text(
                                text = "Skip for now (no penalty)",
                                fontSize = 11.sp,
                                color = mutedText
                            )
                        }
                    }
                }
            }
        }
    }
}
