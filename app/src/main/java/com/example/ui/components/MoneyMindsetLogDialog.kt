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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMetallic
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
import java.util.Calendar

data class EmotionOption(
    val label: String,
    val emoji: String,
    val category: String // "Constructive", "Challenging", "Neutral"
)

val DEFAULT_MONEY_EMOTIONS = listOf(
    EmotionOption("Proud", "🌟", "Constructive"),
    EmotionOption("Abundant", "💎", "Constructive"),
    EmotionOption("Disciplined", "⚖️", "Constructive"),
    EmotionOption("Grateful", "🙏", "Constructive"),
    EmotionOption("Empowered", "💡", "Constructive"),
    EmotionOption("Guilty", "🛡️", "Challenging"),
    EmotionOption("Anxious", "⚡", "Challenging"),
    EmotionOption("Regretful", "🍃", "Challenging"),
    EmotionOption("Impulsive", "🔥", "Challenging"),
    EmotionOption("Mindful", "🧘", "Neutral")
)

val DAILY_MONEY_PROMPTS = listOf(
    "What financial decision did you make today, and what emotion drove it?",
    "Where did you practice financial discipline or conscious accumulation today?",
    "Did an expense bring genuine value and fulfillment, or was it a fleeting reaction?",
    "What belief about wealth or scarcity surfaced in your choices today?",
    "What is one money moment today that reinforced your identity as a sovereign builder?"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoneyMindsetLogDialog(
    onDismiss: () -> Unit,
    onSave: (decisionType: String, actionText: String, emotion: String, beliefText: String, amount: String, promptQuestion: String) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val dayOfYear = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
    val promptIndex = dayOfYear % DAILY_MONEY_PROMPTS.size
    val dailyPrompt = DAILY_MONEY_PROMPTS[promptIndex]

    var selectedType by remember { mutableStateOf("Expense") }
    var actionText by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var selectedEmotion by remember { mutableStateOf("Proud") }
    var customEmotionText by remember { mutableStateOf("") }
    var beliefText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder
    val bgGradient = if (isDark) {
        listOf(RichBlack, SurfaceElevated)
    } else {
        listOf(LightElevated, Color.White)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .widthIn(max = 560.dp)
                .padding(vertical = 16.dp)
                .testTag("money_mindset_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            border = BorderStroke(
                1.5.dp,
                Brush.verticalGradient(
                    listOf(
                        if (isDark) GoldPrimary else tierTheme.goldPrimary,
                        if (isDark) GoldDark else tierTheme.goldDark
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(bgGradient))
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                                .size(38.dp)
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
                            Text(text = "🪙", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "MONEY MINDSET JOURNAL",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Log a Financial Decision & Belief Pattern",
                                fontSize = 11.sp,
                                color = textSecColor
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("money_mindset_dialog_close")
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = textMutedColor)
                    }
                }

                // Daily Prompt Card Banner
                Surface(
                    color = (if (isDark) GoldDark else tierTheme.goldDark).copy(alpha = if (isDark) 0.25f else 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, (if (isDark) GoldPrimary else tierTheme.goldDark).copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = null,
                            tint = if (isDark) AmberBright else tierTheme.goldDark,
                            modifier = Modifier.size(18.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "DAILY CONSCIOUSNESS PROMPT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dailyPrompt,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Italic,
                                color = textColor,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                // 1. Decision Type Selector (Expense, Saving, Income, Investment)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "1. NATURE OF FINANCIAL MOMENT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 0.5.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val decisionTypes = listOf(
                            Triple("Expense", "💸", Icons.Filled.ShoppingCart),
                            Triple("Saving", "🛡️", Icons.Filled.Savings),
                            Triple("Income", "📈", Icons.Filled.TrendingUp),
                            Triple("Investment", "💎", Icons.Filled.AccountBalance)
                        )
                        decisionTypes.forEach { (type, emoji, icon) ->
                            val isSelected = selectedType == type
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedType = type }
                                    .testTag("money_type_$type"),
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) {
                                    if (isDark) GoldPrimary else tierTheme.goldDark
                                } else {
                                    surfaceColor
                                },
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) (if (isDark) AmberBright else tierTheme.goldPrimary) else cardBorderColor
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = emoji, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = type,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) (if (isDark) RichBlack else Color.White) else textSecColor
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. What Happened + Optional Amount
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "2. WHAT HAPPENED? (DECISION / EVENT)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 0.5.sp
                    )
                    OutlinedTextField(
                        value = actionText,
                        onValueChange = { actionText = it; showError = false },
                        placeholder = {
                            Text(
                                text = when (selectedType) {
                                    "Expense" -> "e.g. Bought an expensive takeout dinner after a long day..."
                                    "Saving" -> "e.g. Skipped impulse retail purchase and transferred to reserve..."
                                    "Income" -> "e.g. Closed client invoice or received project milestone payout..."
                                    else -> "e.g. Enrolled in course, bought books, or invested in equipment..."
                                },
                                color = textMutedColor,
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("money_action_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = goldAccent,
                            unfocusedBorderColor = cardBorderColor,
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2,
                        maxLines = 4
                    )

                    // Optional Amount / Impact
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = amountText,
                            onValueChange = { amountText = it },
                            placeholder = { Text("Amount / Impact (optional, e.g. $45)", color = textMutedColor, fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("money_amount_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = goldAccent,
                                unfocusedBorderColor = cardBorderColor,
                                focusedContainerColor = surfaceColor,
                                unfocusedContainerColor = surfaceColor,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                // 3. Emotion / Feeling Behind It
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "3. EMOTION BEHIND THE DECISION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Selected: $selectedEmotion",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) AmberBright else tierTheme.goldDark
                        )
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DEFAULT_MONEY_EMOTIONS.forEach { emotionOpt ->
                            val isSelected = selectedEmotion == emotionOpt.label && customEmotionText.isBlank()
                            Surface(
                                modifier = Modifier
                                    .clickable {
                                        selectedEmotion = emotionOpt.label
                                        customEmotionText = ""
                                    }
                                    .testTag("emotion_chip_${emotionOpt.label}"),
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) {
                                    if (isDark) AmberAccent else tierTheme.goldDark
                                } else {
                                    surfaceColor
                                },
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) (if (isDark) AmberBright else tierTheme.goldPrimary) else cardBorderColor
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = emotionOpt.emoji, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = emotionOpt.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) (if (isDark) RichBlack else Color.White) else textSecColor
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. Belief / Thinking Pattern Behind It
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "4. THE BELIEF OR SELF-TALK BEHIND IT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 0.5.sp
                    )
                    OutlinedTextField(
                        value = beliefText,
                        onValueChange = { beliefText = it; showError = false },
                        placeholder = {
                            Text(
                                text = "Why did you make this decision? What thought or belief surfaced? (e.g. 'Felt proud prioritizing long-term freedom', 'Realized I buy out of stress', 'Believed my service generates endless abundance')",
                                color = textMutedColor,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("money_belief_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = goldAccent,
                            unfocusedBorderColor = cardBorderColor,
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2,
                        maxLines = 4
                    )
                }

                // Error Message
                AnimatedVisibility(visible = showError, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        text = "Please write what happened and the thinking/belief behind it.",
                        color = Color(0xFFEF5350),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, cardBorderColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = textSecColor)
                    ) {
                        Text(text = "Cancel", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            if (actionText.isBlank() || beliefText.isBlank()) {
                                showError = true
                            } else {
                                val effectiveEmotion = if (customEmotionText.isNotBlank()) customEmotionText.trim() else selectedEmotion
                                onSave(
                                    selectedType,
                                    actionText.trim(),
                                    effectiveEmotion,
                                    beliefText.trim(),
                                    amountText.trim(),
                                    dailyPrompt
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(2f)
                            .testTag("submit_money_mindset_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) GoldPrimary else tierTheme.goldDark,
                            contentColor = if (isDark) RichBlack else Color.White
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "INSCRIBE MOMENT • +50 XP",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
