package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Quick-Entry Dialog for Logging Decisions.
 * Embodies Napoleon Hill's principle:
 * "Decisive people reach decisions promptly and change them, if ever, very slowly."
 * Under 1 minute to fill out — rewards speed of decision-making.
 */
@Composable
fun DecisionLogDialog(
    onDismiss: () -> Unit,
    onSaveDecision: (decisionText: String, confidence: Int, timestamp: Long, rationale: String) -> Unit
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

    var decisionText by remember { mutableStateOf("") }
    var confidenceLevel by remember { mutableIntStateOf(4) }
    var rationaleText by remember { mutableStateOf("") }
    var selectedTimestamp by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showCustomDateOptions by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()) }

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
                .testTag("decision_log_dialog"),
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
                                        imageVector = Icons.Filled.FlashOn,
                                        contentDescription = null,
                                        tint = goldAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "DECISION LOG",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = primaryText,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Prompt Decision Protocol",
                                    fontSize = 11.sp,
                                    color = goldAccent,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp).testTag("close_decision_dialog")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = mutedText,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Principle Quote Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDark) SurfaceElevated else LightElevated,
                        border = BorderStroke(0.8.dp, cardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚖️",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "\"Decisive people reach decisions promptly and change them, if ever, very slowly.\"",
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic,
                                fontSize = 11.5.sp,
                                color = secondaryText,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Date Pill / Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DATE OF DECISION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = goldAccent
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Quick Today button
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selectedTimestamp >= System.currentTimeMillis() - 86400000L / 2)
                                    goldAccent.copy(alpha = 0.2f) else Color.Transparent,
                                border = BorderStroke(0.8.dp, if (selectedTimestamp >= System.currentTimeMillis() - 86400000L / 2) goldAccent else cardBorder),
                                modifier = Modifier.clickable {
                                    selectedTimestamp = System.currentTimeMillis()
                                }
                            ) {
                                Text(
                                    text = "Today",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selectedTimestamp >= System.currentTimeMillis() - 86400000L / 2) primaryText else mutedText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            // Yesterday button
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selectedTimestamp in (System.currentTimeMillis() - 86400000L * 2)..(System.currentTimeMillis() - 86400000L / 2))
                                    goldAccent.copy(alpha = 0.2f) else Color.Transparent,
                                border = BorderStroke(0.8.dp, cardBorder),
                                modifier = Modifier.clickable {
                                    selectedTimestamp = System.currentTimeMillis() - 86400000L
                                }
                            ) {
                                Text(
                                    text = "Yesterday",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = mutedText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            // 30 Days Ago (Simulation / Past logging)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selectedTimestamp <= System.currentTimeMillis() - 29L * 86400000L)
                                    AmberAccent.copy(alpha = 0.25f) else Color.Transparent,
                                border = BorderStroke(0.8.dp, cardBorder),
                                modifier = Modifier.clickable {
                                    selectedTimestamp = System.currentTimeMillis() - 31L * 86400000L
                                }
                            ) {
                                Text(
                                    text = "-30 Days",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selectedTimestamp <= System.currentTimeMillis() - 29L * 86400000L) AmberBright else mutedText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Recorded for: ${dateFormat.format(Date(selectedTimestamp))}",
                        fontSize = 10.sp,
                        color = mutedText,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Field 1: The Decision Text
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "WHAT DID YOU DECIDE?",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                letterSpacing = 0.5.sp
                            )
                            Text(text = "*", color = AmberBright, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = decisionText,
                            onValueChange = { decisionText = it },
                            placeholder = {
                                Text(
                                    text = "e.g., Committed to launching v1 on Friday / Accepted contract / Cut distraction X...",
                                    fontSize = 12.sp,
                                    color = mutedText.copy(alpha = 0.7f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .testTag("decision_input_field"),
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Field 2: Gut-Check Confidence (1-5)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "GUT-CHECK CONFIDENCE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                letterSpacing = 0.5.sp
                            )
                            val confidenceLabel = when (confidenceLevel) {
                                1 -> "1/5 • Tentative Leap"
                                2 -> "2/5 • Moderate Doubt"
                                3 -> "3/5 • Balanced Conviction"
                                4 -> "4/5 • High Confidence"
                                else -> "5/5 • Absolute Conviction"
                            }
                            Text(
                                text = confidenceLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Star selector row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDark) SurfaceElevated else LightElevated)
                                .border(BorderStroke(0.8.dp, cardBorder), RoundedCornerShape(12.dp))
                                .padding(vertical = 8.dp, horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            (1..5).forEach { level ->
                                val isSelected = level <= confidenceLevel
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { confidenceLevel = level }
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                        contentDescription = "Confidence $level",
                                        tint = if (isSelected) goldAccent else mutedText.copy(alpha = 0.5f),
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Field 3: Optional Context / Rationale
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "CONTEXT & RATIONALE (OPTIONAL)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = mutedText,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = rationaleText,
                            onValueChange = { rationaleText = it },
                            placeholder = {
                                Text(
                                    text = "Why this choice? What made you decide right now?",
                                    fontSize = 11.5.sp,
                                    color = mutedText.copy(alpha = 0.7f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                                .testTag("decision_rationale_field"),
                            shape = RoundedCornerShape(10.dp),
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

                    // 30-Day Reminder Notice
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = goldAccent.copy(alpha = 0.08f),
                        border = BorderStroke(0.6.dp, goldAccent.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⏳", fontSize = 14.sp, modifier = Modifier.padding(end = 6.dp))
                            Text(
                                text = "This entry will automatically prompt you for a 30-day reflection revisit to evaluate outcomes without judgment.",
                                fontSize = 10.5.sp,
                                color = secondaryText,
                                lineHeight = 15.sp
                            )
                        }
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
                                if (decisionText.isNotBlank()) {
                                    onSaveDecision(
                                        decisionText.trim(),
                                        confidenceLevel,
                                        selectedTimestamp,
                                        rationaleText.trim()
                                    )
                                }
                            },
                            enabled = decisionText.isNotBlank(),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(46.dp)
                                .testTag("save_decision_button"),
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
                                    text = "INSCRIBE (+50 XP)",
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
