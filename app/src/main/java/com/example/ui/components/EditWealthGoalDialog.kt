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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun EditWealthGoalDialog(
    currentGoal: WealthGoalEntity?,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        targetAmount: Double,
        startingAmount: Double,
        targetDateEpochMillis: Long,
        currencySymbol: String,
        category: String,
        servicePledge: String
    ) -> Unit
) {
    val initialGoal = currentGoal ?: WealthGoalEntity()

    var title by remember { mutableStateOf(initialGoal.title) }
    var targetAmountText by remember { mutableStateOf(if (initialGoal.targetAmount > 0) String.format(Locale.US, "%.0f", initialGoal.targetAmount) else "100000") }
    var startingAmountText by remember { mutableStateOf(if (initialGoal.startingAmount > 0) String.format(Locale.US, "%.0f", initialGoal.startingAmount) else "0") }
    var currencySymbol by remember { mutableStateOf(initialGoal.currencySymbol) }
    var category by remember { mutableStateOf(initialGoal.category) }
    var targetDateMillis by remember { mutableLongStateOf(initialGoal.targetDateEpochMillis) }
    var servicePledge by remember { mutableStateOf(initialGoal.servicePledge) }

    val currencies = listOf("$", "€", "£", "¥", "₹", "₿")
    val horizonPresets = listOf(
        Pair("6 Months", 182L),
        Pair("1 Year", 365L),
        Pair("2 Years", 730L),
        Pair("3 Years", 1095L),
        Pair("5 Years", 1825L)
    )

    val sdf = remember { SimpleDateFormat("MMMM d, yyyy", Locale.US) }
    val formattedTargetDate = remember(targetDateMillis) {
        sdf.format(Date(targetDateMillis))
    }

    val parsedTarget = targetAmountText.toDoubleOrNull() ?: 0.0
    val parsedStarting = startingAmountText.toDoubleOrNull() ?: 0.0
    val isValid = title.isNotBlank() && parsedTarget > 0 && targetDateMillis > System.currentTimeMillis()

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .border(1.5.dp, Brush.linearGradient(listOf(GoldLight, GoldDark, AmberBright)), RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .testTag("edit_wealth_goal_dialog"),
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
                            imageVector = Icons.Filled.AccountBalance,
                            contentDescription = null,
                            tint = RichBlack,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "WEALTH GOAL TARGET",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Definite Aim Financial Horizon",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp).testTag("close_wealth_goal_dialog")
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
                Text(
                    text = "Napoleon Hill's First Step: 'Fix in your mind the exact amount of money you desire. It is not sufficient merely to say I want plenty of money. Be definite as to the amount.'",
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp,
                    color = TextSecondary,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Currency Symbol Selector
                Text(
                    text = "CURRENCY STANDARD",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    currencies.forEach { curr ->
                        val isSelected = currencySymbol == curr
                        Surface(
                            color = if (isSelected) GoldPrimary else DarkCharcoal,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) AmberBright else DarkBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { currencySymbol = curr }
                                .testTag("currency_select_$curr")
                        ) {
                            Text(
                                text = curr,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) RichBlack else TextPrimary,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .align(Alignment.CenterVertically),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Goal Title
                Text(
                    text = "DEFINITE AIM PURPOSE / TITLE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("e.g. Liquid Capital Reserve, Sovereign Enterprise Fund", color = TextMuted, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("wealth_goal_title_input"),
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

                Spacer(modifier = Modifier.height(14.dp))

                // Target Amount & Starting Balance Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "TARGET AMOUNT ($currencySymbol)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = targetAmountText,
                            onValueChange = { targetAmountText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                            leadingIcon = { Text(currencySymbol, fontWeight = FontWeight.Bold, color = AmberBright, fontSize = 14.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("wealth_target_amount_input"),
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
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "STARTING BASE ($currencySymbol)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = startingAmountText,
                            onValueChange = { startingAmountText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                            leadingIcon = { Text(currencySymbol, fontWeight = FontWeight.Bold, color = TextMuted, fontSize = 14.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("wealth_starting_amount_input"),
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
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Target Date / Deadline Selector
                Text(
                    text = "TARGET DEADLINE: $formattedTargetDate",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    horizonPresets.forEach { (label, days) ->
                        val presetMillis = System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000L)
                        val isPresetActive = kotlin.math.abs(targetDateMillis - presetMillis) < (12 * 60 * 60 * 1000L)

                        Surface(
                            color = if (isPresetActive) GoldDark.copy(alpha = 0.6f) else DarkCharcoal.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isPresetActive) GoldPrimary else DarkBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    targetDateMillis = presetMillis
                                }
                                .testTag("deadline_preset_$label")
                        ) {
                            Text(
                                text = label,
                                fontSize = 9.5.sp,
                                fontWeight = if (isPresetActive) FontWeight.Bold else FontWeight.Normal,
                                color = if (isPresetActive) AmberBright else TextSecondary,
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                                    .align(Alignment.CenterVertically),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Transmutation Pledge
                Text(
                    text = "TRANSMUTATION PLEDGE (SERVICE IN RETURN)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = servicePledge,
                    onValueChange = { servicePledge = it },
                    placeholder = {
                        Text(
                            "What specific value, craftsmanship, or relentless service do you pledge to give in exchange for this accumulation?",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth().testTag("wealth_service_pledge_input"),
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

                // Quick Summary Card
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = SurfaceElevated.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    val daysRemaining = ((targetDateMillis - System.currentTimeMillis()) / (24 * 60 * 60 * 1000L)).coerceAtLeast(1)
                    val remainingAmount = (parsedTarget - parsedStarting).coerceAtLeast(0.0)
                    val dailyPace = if (daysRemaining > 0) remainingAmount / daysRemaining else 0.0

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Time Horizon", fontSize = 9.sp, color = TextMuted)
                            Text(text = "$daysRemaining Days", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Definite Daily Pace", fontSize = 9.sp, color = TextMuted)
                            Text(
                                text = "$currencySymbol${String.format(Locale.US, "%,.2f", dailyPace)} / day",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberBright
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValid) {
                        onSave(
                            title.trim(),
                            parsedTarget,
                            parsedStarting,
                            targetDateMillis,
                            currencySymbol,
                            category.trim(),
                            servicePledge.trim()
                        )
                    }
                },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = RichBlack,
                    disabledContainerColor = DarkCharcoal,
                    disabledContentColor = TextMuted
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_wealth_goal_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Seal Definite Target",
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
