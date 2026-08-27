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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CommitmentContractEntity
import com.example.data.model.UserProfileEntity
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CreateCommitmentContractDialog(
    userProfile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onConfirm: (goalStatement: String, whyItMatters: String, deadlineMillis: Long, signatureName: String) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val chiefAim = userProfile?.definiteChiefAim?.trim().orEmpty()
    val initialName = userProfile?.name?.takeIf { it.isNotBlank() && it != "Sovereign Initiate" } ?: "Sovereign Initiate"

    var goalStatement by remember { mutableStateOf(chiefAim) }
    var whyItMatters by remember { mutableStateOf("") }
    var signatureName by remember { mutableStateOf(initialName) }

    // Deadline presets (days from now)
    var selectedPresetDays by remember { mutableStateOf(30) } // Default 30 days
    var customDeadlineMillis by remember {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 30)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        mutableLongStateOf(cal.timeInMillis)
    }

    fun updateDeadlineDays(days: Int) {
        selectedPresetDays = days
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, days)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        customDeadlineMillis = cal.timeInMillis
    }

    val formattedDeadline = remember(customDeadlineMillis) {
        val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        sdf.format(Date(customDeadlineMillis))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .heightIn(max = 720.dp)
                .testTag("create_commitment_contract_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) SurfaceElevated else Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.15f))
                                .border(1.dp, GoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📜", fontSize = 18.sp)
                        }
                        Column {
                            Text(
                                text = "SOVEREIGN COMMITMENT COVENANT",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldPrimary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Formalize Definiteness of Purpose",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Optional 1-Click Import Chief Aim Banner
                if (chiefAim.isNotBlank() && goalStatement != chiefAim) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldDark.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { goalStatement = chiefAim }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "1-Click: Pre-fill from Definite Chief Aim",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // 1. Goal Statement Input
                Text(
                    text = "1. Specific Goal Statement",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = goalStatement,
                    onValueChange = { goalStatement = it },
                    placeholder = {
                        Text(
                            "e.g. By December 31, 2026, I will accumulate $100,000 in liquid capital through my sovereign consulting enterprise...",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = if (isDark) TextPrimary else RichBlack,
                        unfocusedTextColor = if (isDark) TextPrimary else RichBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contract_goal_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Why It Matters (Stakes & Emotional Purpose)
                Text(
                    text = "2. Sacred Purpose & Stakes (Why It Matters)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = whyItMatters,
                    onValueChange = { whyItMatters = it },
                    placeholder = {
                        Text(
                            "e.g. This will establish generational security for my family, eliminate debt, and prove the power of persistence...",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    },
                    minLines = 2,
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = if (isDark) TextPrimary else RichBlack,
                        unfocusedTextColor = if (isDark) TextPrimary else RichBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contract_why_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Target Deadline Selection
                Text(
                    text = "3. Target Deadline: $formattedDeadline",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Preset Chips Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val presets = listOf(
                        14 to "14d",
                        30 to "30d",
                        60 to "60d",
                        90 to "90d",
                        180 to "6mo",
                        365 to "1yr"
                    )
                    presets.forEach { (days, label) ->
                        val isSelected = selectedPresetDays == days
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) GoldPrimary else if (isDark) DarkCharcoal else Color(0xFFF1F5F9),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) GoldPrimary else DarkBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { updateDeadlineDays(days) }
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) RichBlack else if (isDark) TextPrimary else RichBlack,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. Formal Signature Name
                Text(
                    text = "4. Sovereign Signature",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = signatureName,
                    onValueChange = { signatureName = it },
                    placeholder = { Text("Your full name to sign this covenant", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = if (isDark) TextPrimary else RichBlack,
                        unfocusedTextColor = if (isDark) TextPrimary else RichBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contract_signature_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Solemn Covenant Box
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) DarkCharcoal.copy(alpha = 0.6f) else Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "IRREVOCABLE SOVEREIGN COVENANT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Text(
                            text = "“I hold myself unconditionally accountable. No excuses, no retreat. I will transmute this burning desire into its physical counterpart through unrelenting daily execution.”",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Serif,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                val isFormValid = goalStatement.trim().isNotBlank() && whyItMatters.trim().isNotBlank()

                Button(
                    onClick = {
                        if (isFormValid) {
                            onConfirm(
                                goalStatement.trim(),
                                whyItMatters.trim(),
                                customDeadlineMillis,
                                signatureName.trim()
                            )
                        }
                    },
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = RichBlack,
                        disabledContainerColor = DarkBorder,
                        disabledContentColor = TextMuted
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("commit_contract_submit_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "I Commit to This (Seal Covenant +75 XP)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun RenewCommitmentContractDialog(
    contract: CommitmentContractEntity,
    onDismiss: () -> Unit,
    onConfirm: (newDeadlineMillis: Long, renewalNotes: String) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    var renewalNotes by remember { mutableStateOf("") }
    var selectedPresetDays by remember { mutableStateOf(30) }

    var newDeadlineMillis by remember {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 30)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        mutableLongStateOf(cal.timeInMillis)
    }

    fun updateDeadlineDays(days: Int) {
        selectedPresetDays = days
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, days)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        newDeadlineMillis = cal.timeInMillis
    }

    val formattedNewDeadline = remember(newDeadlineMillis) {
        val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        sdf.format(Date(newDeadlineMillis))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .testTag("renew_commitment_contract_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) SurfaceElevated else Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(AmberAccent.copy(alpha = 0.2f))
                                .border(1.dp, AmberBright, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Autorenew,
                                contentDescription = null,
                                tint = AmberBright,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "RENEW & EXTEND COVENANT",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AmberBright,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Calibrate Timeline with Unshakable Persistence",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Current Aim Context
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) DarkCharcoal else Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "ACTIVE COMMITMENT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "“${contract.goalStatement}”",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) TextPrimary else RichBlack
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Original Deadline: ${contract.getFormattedDeadline()}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // New Deadline Picker
                Text(
                    text = "New Target Deadline: $formattedNewDeadline",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val presets = listOf(
                        14 to "+14d",
                        30 to "+30d",
                        60 to "+60d",
                        90 to "+90d",
                        180 to "+6mo"
                    )
                    presets.forEach { (days, label) ->
                        val isSelected = selectedPresetDays == days
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) GoldPrimary else if (isDark) DarkCharcoal else Color(0xFFF1F5F9),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) GoldPrimary else DarkBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { updateDeadlineDays(days) }
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) RichBlack else if (isDark) TextPrimary else RichBlack,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Strategic Calibration Notes
                Text(
                    text = "Strategic Calibration & Learnings",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = renewalNotes,
                    onValueChange = { renewalNotes = it },
                    placeholder = {
                        Text(
                            "What strategy or habits are you adjusting to guarantee victory by the new deadline?",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    },
                    minLines = 3,
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = if (isDark) TextPrimary else RichBlack,
                        unfocusedTextColor = if (isDark) TextPrimary else RichBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contract_renewal_notes_input")
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onConfirm(newDeadlineMillis, renewalNotes.trim())
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("confirm_renewal_contract_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Autorenew,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Seal Renewal (+50 XP) 🔄",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CompleteCommitmentContractDialog(
    contract: CommitmentContractEntity,
    onDismiss: () -> Unit,
    onConfirm: (completionNotes: String) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    var completionNotes by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .testTag("complete_commitment_contract_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) SurfaceElevated else Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(2.dp, GoldPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Trophy
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(GoldLight, GoldDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = RichBlack,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "COVENANT FULFILLED",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldPrimary,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Victory Transmuted Into Physical Reality",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // The Accomplished Aim
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) DarkCharcoal else Color(0xFFF1F5F9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "“${contract.goalStatement}”",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) TextPrimary else RichBlack,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Target Deadline: ${contract.getFormattedDeadline()}",
                            fontSize = 11.sp,
                            color = GoldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Victory Reflections / Learnings Input
                Text(
                    text = "Victory Reflection & Insights",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = completionNotes,
                    onValueChange = { completionNotes = it },
                    placeholder = {
                        Text(
                            "What breakthroughs, habits, or decisions enabled you to accomplish this aim? (Recorded to your Sovereign Notebook)",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = if (isDark) TextPrimary else RichBlack,
                        unfocusedTextColor = if (isDark) TextPrimary else RichBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contract_completion_notes_input")
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onConfirm(completionNotes.trim())
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_complete_contract_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Seal Victory & Claim +150 XP 🏆",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextSecondary
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel", fontSize = 12.sp)
                }
            }
        }
    }
}
